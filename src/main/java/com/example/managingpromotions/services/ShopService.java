package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.ResourceNotFoundException;
import com.example.managingpromotions.mapper.GroceryListMapper;
import com.example.managingpromotions.mapper.ProductMapper;
import com.example.managingpromotions.model.GroceryElement;
import com.example.managingpromotions.model.GroceryList;
import com.example.managingpromotions.model.Product;
import com.example.managingpromotions.model.repository.GroceryElementRepository;
import com.example.managingpromotions.model.repository.GroceryListRepository;
import com.example.managingpromotions.model.repository.ProductRepository;
import com.example.managingpromotions.services.shopParser.AuchanParser;
import com.example.managingpromotions.services.shopParser.CarrefourParser;
import com.example.managingpromotions.services.shopParser.EleclercParser;
import com.example.managingpromotions.services.shopParser.GroszekParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.CheapestShoppingReponse;
import pl.managingPromotions.api.model.GroceryListProductDTO;
import pl.managingPromotions.api.model.ParsedProductDTO;
import pl.managingPromotions.api.model.ProductDTO;
import pl.managingPromotions.api.model.ProductParsedFromShopDTO;
import pl.managingPromotions.api.model.ShopEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ShopService {

    private final AuchanParser auchanParser;
    private final GroszekParser groszekParser;
    private final ProductMapper productMapper;
    private final EleclercParser eleclercParser;
    private final CarrefourParser carrefourParser;
    private final GroceryListMapper groceryListMapper;
    private final ProductRepository productRepository;
    private final GroceryListRepository groceryListRepository;
    private final GroceryElementRepository groceryElementRepository;

    public void parseProductsFromShops(Long groceryListId) {

        GroceryList groceryList = groceryListRepository.findById(groceryListId)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery list with id: " + groceryListId + " not found"));

        clearParsedProductsForGrocerList(groceryList);

        Set<GroceryElement> syncGroceryElements = Collections.synchronizedSet(groceryList.getGroceryElements());
        invokeProductsParsers(syncGroceryElements, groceryList);
    }

    @Transactional
    private void invokeProductsParsers(Set<GroceryElement> syncGroceryElements, GroceryList groceryList) {

        Thread threadProductParser = new Thread(() -> {
            synchronized (syncGroceryElements) {
                syncGroceryElements.forEach(groceryElement -> {

                    List<Product> productsFromEleclerc = invokeParserForEleclerc(groceryElement);
                    List<Product> productsFromGroszek = invokeParserForGroszek(groceryElement);

                    groceryElement.addAllProducts(productsFromEleclerc);
                    groceryElement.addAllProducts(productsFromGroszek);
                });
            }

            groceryList.setIsProcessed(true);
            groceryListRepository.save(groceryList);
        });

        CompletableFuture.runAsync(threadProductParser);
    }

    private List<Product> invokeParserForEleclerc(GroceryElement groceryElement) {

        ProductParsedFromShopDTO parsedProductFromEleclerc = fetchDataFromEleclerc(groceryElement);
        return mapParsedProductToProduct(parsedProductFromEleclerc);
    }

    private List<Product> invokeParserForGroszek(GroceryElement groceryElement) {

        ProductParsedFromShopDTO parsedProductFromGroszek = fetchDataFromGroszek(groceryElement);
        return mapParsedProductToProduct(parsedProductFromGroszek);
    }

    @Transactional
    private void clearParsedProductsForGrocerList(GroceryList groceryList) {

        Set<GroceryElement> groceryElements = groceryList.getGroceryElements();
        groceryElements.forEach(groceryElement -> groceryElement.getParsedProducts().clear());

        log.info("Deleted products for grocery list with id: {}", groceryList.getId());
    }

    @Transactional
    private List<Product> mapParsedProductToProduct(ProductParsedFromShopDTO parsedFromShopDTO) {

        List<Product> productsToSave = productMapper.mapListParsedProductDTOToListProduct(parsedFromShopDTO.getProducts());
        productsToSave.forEach(product -> product.setShopName(parsedFromShopDTO.getShopName()));

        return productsToSave;
    }

    @Transactional
    public List<ProductParsedFromShopDTO> getParsedProductsFromShops(Long groceryListId) {

        //todo todo
        GroceryList groceryList = groceryListRepository.findById(groceryListId)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery List with id: " + groceryListId + " not found"));

        return Collections.emptyList();
    }

    private ProductParsedFromShopDTO fetchDataFromEleclerc(GroceryElement groceryElement) {

        Document document = eleclercParser.fetchDataFromWeb(groceryElement.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();
        productParsedFromShopDTO.setShopName(ShopEnum.ELECLERC);

        List<ProductDTO> productDTOS = checkProductDTOSize(eleclercParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryElement.getUnit());
            productParsedDTO.amount(groceryElement.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private ProductParsedFromShopDTO fetchDataFromGroszek(GroceryElement groceryElement) {

        Document document = groszekParser.fetchDataFromWeb(groceryElement.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();
        productParsedFromShopDTO.setShopName(ShopEnum.GROSZEK);

        List<ProductDTO> productDTOS = checkProductDTOSize(groszekParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryElement.getUnit());
            productParsedDTO.amount(groceryElement.getAmount());
        });

        productParsedFromShopDTO.setProducts(parsedProductDTOS);

        return productParsedFromShopDTO;
    }

    private ProductParsedFromShopDTO fetchDataFromAuchan(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = auchanParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.AUCHAN);

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
        productParsedFromShopDTO.setShopName(ShopEnum.CARREFOUR);

        List<ProductDTO> productDTOS = checkProductDTOSize(carrefourParser.prepareData(document));
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

        List<ProductParsedFromShopDTO> convertedProductParsedFromShop = prepareData(productParsedFromShopDTOS);

        List<CheapestShoppingReponse> cheapestShoppingResponseDTO = new ArrayList<>();

        convertedProductParsedFromShop.forEach(
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

    private List<ProductParsedFromShopDTO> prepareData(List<ProductParsedFromShopDTO> productParsedFromShopDTOS) {

        List<ProductParsedFromShopDTO> productsParsedAfterConvert = new ArrayList<>();

        List<ShopEnum> shopNamesFromResponse = productParsedFromShopDTOS.stream()
                .map(ProductParsedFromShopDTO::getShopName)
                .distinct()
                .collect(Collectors.toList());

        shopNamesFromResponse.forEach(shopName -> {

            List<ParsedProductDTO> parsedProductDTOS = productParsedFromShopDTOS.stream()
                    .filter(productParsedFromShopDTO -> productParsedFromShopDTO.getShopName().equals(shopName))
                    .map(ProductParsedFromShopDTO::getProducts)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            ProductParsedFromShopDTO productParsedFromShopDTO = ProductParsedFromShopDTO.builder()
                    .shopName(shopName)
                    .products(parsedProductDTOS)
                    .build();

            productsParsedAfterConvert.add(productParsedFromShopDTO);
        });

        return productsParsedAfterConvert;

    }

    private CheapestShoppingReponse createCheapestShoppingResponse(ProductParsedFromShopDTO productParsedFromShopDTO,
                                                                   BigDecimal totalPrice) {
        return CheapestShoppingReponse.builder()
                .shopName(productParsedFromShopDTO.getShopName().getValue())
                .groceryListId(productParsedFromShopDTO.getGroceryListId())
                .products(productParsedFromShopDTO.getProducts())
                .price(totalPrice)
                .build();
    }

    private BigDecimal calculateTotalPrice(List<ParsedProductDTO> parsedProductDTOS) {

        final BigDecimal[] totalPrices = {new BigDecimal("0.00")};

        parsedProductDTOS.forEach(parsedProductDTO -> {
            BigDecimal bigDecimal = parsedProductDTO.getPrice().multiply(parsedProductDTO.getAmount());

            totalPrices[0] = totalPrices[0].add(bigDecimal);
        });

        return totalPrices[0];
    }

    private Map.Entry<String, BigDecimal> choseCheapestShop(Map<String, BigDecimal> valuesOfPurchasesFromShop) {
        return Collections.min(valuesOfPurchasesFromShop.entrySet(), Map.Entry.comparingByValue());
    }
}
