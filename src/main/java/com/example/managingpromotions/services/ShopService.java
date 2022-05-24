package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.ResourceNotFoundException;
import com.example.managingpromotions.mapper.GroceryListMapper;
import com.example.managingpromotions.mapper.ProductMapper;
import com.example.managingpromotions.models.GroceryList;
import com.example.managingpromotions.models.repository.GroceryListRepository;
import com.example.managingpromotions.services.shopParser.AuchanParser;
import com.example.managingpromotions.services.shopParser.CarrefourParser;
import com.example.managingpromotions.services.shopParser.EleclercParser;
import com.example.managingpromotions.services.shopParser.GroszekParser;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.*;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class ShopService {

    private final AuchanParser auchanParser;
    private final GroszekParser groszekParser;
    private final ProductMapper productMapper;
    private final EleclercParser eleclercParser;
    private final CarrefourParser carrefourParser;
    private final GroceryListMapper groceryListMapper;
    private final GroceryListRepository groceryListRepository;

    @Transactional
    public List<ProductParsedFromShopDTO> getCheapestShop(Long groceryListId) {
        List<ProductParsedFromShopDTO> productParsedFromShopDTOS = new ArrayList<>();
        Optional<GroceryList> groceryList = groceryListRepository.findById(groceryListId);

        List<GroceryListProductDTO> groceryListProductDTOS = groceryListMapper.mapSetGroceryElementToGroceryListProductDTO(
                groceryList.orElseThrow(() ->
                        new ResourceNotFoundException(String.valueOf(groceryListId))).getGroceryElements());

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromEleclerc(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromAuchan(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromCarrefour(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromGroszek(productDTO, groceryListId)));

        return productParsedFromShopDTOS;
    }

    private ProductParsedFromShopDTO fetchDataFromEleclerc(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = eleclercParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.ELECLERC.getValue());

        List<ProductDTO> productDTOS = checkProductDTOSize(eleclercParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryListProductDTO.getUnit());
            productParsedDTO.amount(groceryListProductDTO.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private ProductParsedFromShopDTO fetchDataFromAuchan(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = auchanParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.AUCHAN.getValue());

        List<ProductDTO> productDTOS = checkProductDTOSize(auchanParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryListProductDTO.getUnit());
            productParsedDTO.amount(groceryListProductDTO.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private ProductParsedFromShopDTO fetchDataFromCarrefour(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = carrefourParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.CARREFOUR.getValue());

        List<ProductDTO> productDTOS = checkProductDTOSize(carrefourParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryListProductDTO.getUnit());
            productParsedDTO.amount(groceryListProductDTO.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private ProductParsedFromShopDTO fetchDataFromGroszek(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = groszekParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.GROSZEK.getValue());

        List<ProductDTO> productDTOS = checkProductDTOSize(groszekParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryListProductDTO.getUnit());
            productParsedDTO.amount(groceryListProductDTO.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private List<ProductDTO> checkProductDTOSize(List<ProductDTO> productDTOS) {

        if (productDTOS.size() >= 4) {
            return productDTOS.subList(0, 4);
        }
        return productDTOS;
    }

    public List<CheapestShoppingReponse> findCheapestProduct(List<ProductParsedFromShopDTO> productParsedFromShopDTOS) {

        List<CheapestShoppingReponse> cheapestShoppingResponseDTO = new ArrayList<>();

        productParsedFromShopDTOS.forEach(
                productParsedFromShopDTO -> {
                    List<ParsedProductDTO> parsedProductDTOS = productParsedFromShopDTO.getProducts();
                    BigDecimal totalPrice = calculateTotalPrice(parsedProductDTOS);

                    CheapestShoppingReponse cheapestShoppingReponse = createCheapestShoppingResponse(productParsedFromShopDTO, totalPrice);
                    cheapestShoppingResponseDTO.add(cheapestShoppingReponse);
                }
        );

        cheapestShoppingResponseDTO.sort(Comparator.comparing(CheapestShoppingReponse::getPrice));

        return cheapestShoppingResponseDTO;
    }

    private CheapestShoppingReponse createCheapestShoppingResponse(ProductParsedFromShopDTO productParsedFromShopDTO,
                                                                   BigDecimal totalPrice) {
        return CheapestShoppingReponse.builder()
                .shopName(productParsedFromShopDTO.getShopName())
                .groceryListId(productParsedFromShopDTO.getGroceryListId())
                .products(productParsedFromShopDTO.getProducts())
                .price(totalPrice)
                .build();
    }

    private BigDecimal calculateTotalPrice(List<ParsedProductDTO> parsedProductDTOS) {

        final BigDecimal[] totalPrices = {new BigDecimal("0.00")};

        parsedProductDTOS.forEach(parsedProductDTO -> {
            BigDecimal bigDecimal = parsedProductDTO.getPrice().multiply(BigDecimal.valueOf(parsedProductDTO.getAmount()));

            totalPrices[0] = totalPrices[0].add(bigDecimal);
        });

        return totalPrices[0];
    }

    private Map.Entry<String, BigDecimal> choseCheapestShop(Map<String, BigDecimal> valuesOfPurchasesFromShop) {
        return Collections.min(valuesOfPurchasesFromShop.entrySet(), Map.Entry.comparingByValue());
    }
}
