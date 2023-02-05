package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.ResourceNotFoundException;
import com.example.managingpromotions.mapper.GroceryListMapper;
import com.example.managingpromotions.mapper.ProductMapper;
import com.example.managingpromotions.model.GroceryElement;
import com.example.managingpromotions.model.GroceryList;
import com.example.managingpromotions.model.Product;
import com.example.managingpromotions.model.repository.GroceryListRepository;
import com.example.managingpromotions.services.shopParser.AuchanParser;
import com.example.managingpromotions.services.shopParser.CarrefourParser;
import com.example.managingpromotions.services.shopParser.EleclercParser;
import com.example.managingpromotions.services.shopParser.GroszekParser;
import lombok.AllArgsConstructor;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public void parseProductsFromShops(Long groceryListId) {

        GroceryList groceryList = groceryListRepository.findById(groceryListId)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery list with id: " + groceryListId + " not found"));

        Set<GroceryElement> syncGroceryElements = Collections.synchronizedSet(groceryList.getGroceryElements());

        Thread thread1 = new Thread(() -> {
            synchronized (syncGroceryElements) {
                syncGroceryElements.forEach(groceryElement -> {

                    ProductParsedFromShopDTO parsedFromShopDTO = fetchDataFromEleclerc(groceryElement);
                    saveProduct(parsedFromShopDTO, groceryList);
                });
            }
        });

        CompletableFuture.runAsync(thread1);

    }

    private void saveProduct(ProductParsedFromShopDTO parsedFromShopDTO, GroceryList groceryList) {

        Product product;
    }


    @Transactional
    public List<ProductParsedFromShopDTO> getCheapestShop(Long groceryListId) {

        //todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        parseProductsFromShops(groceryListId);

        // todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo todo
        List<ProductParsedFromShopDTO> productParsedFromShopDTOS = Collections.synchronizedList(new ArrayList<>());

        Optional<GroceryList> groceryList = groceryListRepository.findById(groceryListId);

        List<GroceryListProductDTO> groceryListProductDTOS = groceryListMapper.mapSetGroceryElementToGroceryListProductDTO(
                groceryList.orElseThrow(() ->
                        new ResourceNotFoundException(String.valueOf(groceryListId))).getGroceryElements());

        List<GroceryListProductDTO> groceryListProductDTOSSynchronized = Collections.synchronizedList(groceryListProductDTOS);

        /*        Thread thread1 = new Thread(() -> {
            synchronized (groceryListProductDTOSSynchronized) {
                groceryListProductDTOSSynchronized.forEach(
                        productDTO -> productParsedFromShopDTOS.add(fetchDataFromEleclerc(productDTO, groceryListId)));
            }
        });*/

/*        Thread thread2 = new Thread(() -> {
            synchronized (groceryListProductDTOSSynchronized) {
                groceryListProductDTOSSynchronized.forEach(
                        productDTO -> productParsedFromShopDTOS.add(fetchDataFromAuchan(productDTO, groceryListId)));
            }
        });

        Thread thread3 = new Thread(() -> {
            synchronized (groceryListProductDTOSSynchronized) {
                groceryListProductDTOSSynchronized.forEach(
                        productDTO -> productParsedFromShopDTOS.add(fetchDataFromCarrefour(productDTO, groceryListId)));
            }
        });*/

/*        Thread thread4 = new Thread(() -> {
            synchronized (groceryListProductDTOSSynchronized) {
                groceryListProductDTOSSynchronized.forEach(
                        productDTO -> productParsedFromShopDTOS.add(fetchDataFromGroszek(productDTO, groceryListId)));
            }
        });*/

/*        thread1.start();
        //  thread2.start();
        //  thread3.start();
        thread4.start();

        thread1.join();
        //  thread2.join();
        //  thread3.join();
        thread4.join();*/
    /*    thread2.start();
        thread3.start();
        thread4.start();*/


/*        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromEleclerc(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromAuchan(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromCarrefour(productDTO, groceryListId)));

        groceryListProductDTOS.forEach(
                productDTO -> productParsedFromShopDTOS.add(fetchDataFromGroszek(productDTO, groceryListId)));*/

        return productParsedFromShopDTOS;
    }

    private ProductParsedFromShopDTO fetchDataFromEleclerc(GroceryElement groceryElement) {

        Document document = eleclercParser.fetchDataFromWeb(groceryElement.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setShopName(ShopEnum.ELECLERC.getValue());

        List<ProductDTO> productDTOS = checkProductDTOSize(eleclercParser.prepareData(document));
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryElement.getUnit());
            productParsedDTO.amount(Integer.valueOf(String.valueOf(groceryElement.getAmount())));
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

        List<String> shopNamesFromResponse = productParsedFromShopDTOS.stream()
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
