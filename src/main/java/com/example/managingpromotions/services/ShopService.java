package com.example.managingpromotions.services;

import com.example.managingpromotions.exception.ResourceNotFoundException;
import com.example.managingpromotions.mapper.GroceryListMapper;
import com.example.managingpromotions.mapper.ProductMapper;
import com.example.managingpromotions.model.GroceryElement;
import com.example.managingpromotions.model.GroceryList;
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
import java.util.Optional;
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

    @Transactional
    public void parseProductsFromShops(Long groceryListId) {

        GroceryList groceryList = groceryListRepository.findById(groceryListId)
                .orElseThrow(() -> new ResourceNotFoundException("Grocery list with id: " + groceryListId + " not found"));

        clearParsedProductsForGrocerList(groceryList);

        Set<GroceryElement> syncGroceryElements = Collections.synchronizedSet(groceryList.getGroceryElements());
        invokeProductsParsers(syncGroceryElements, groceryList);
    }

    private void invokeProductsParsers(Set<GroceryElement> syncGroceryElements, GroceryList groceryList) {

        invokeParserForEleclerc(syncGroceryElements);
        invokeParserForGroszek(syncGroceryElements, groceryList);
    }

    private void setProcessedGroceryList(GroceryList groceryList) {

        groceryList.setIsProcessed(true);
        groceryListRepository.save(groceryList);
    }

    private void invokeParserForEleclerc(Set<GroceryElement> syncGroceryElements) {

        Thread thread1 = new Thread(() -> {
            synchronized (syncGroceryElements) {
                syncGroceryElements.forEach(groceryElement -> {
                    ProductParsedFromShopDTO parsedFromShopDTO = fetchDataFromEleclerc(groceryElement);
                    saveProduct(parsedFromShopDTO, groceryElement);
                });
            }
        });

        CompletableFuture.runAsync(thread1);
    }

    private void invokeParserForGroszek(Set<GroceryElement> syncGroceryElements, GroceryList groceryList) {

        Thread thread = new Thread(() -> {
            synchronized (syncGroceryElements) {
                syncGroceryElements.forEach(groceryElement -> {
                    ProductParsedFromShopDTO parsedFromShopDTO = fetchDataFromGroszek(groceryElement);
                    saveProduct(parsedFromShopDTO, groceryElement);
                });

                setProcessedGroceryList(groceryList);
            }
        });

        CompletableFuture.runAsync(thread);
    }

    private void clearParsedProductsForGrocerList(GroceryList groceryList) {

        Set<GroceryElement> groceryElements = groceryList.getGroceryElements();
        groceryElements.forEach(groceryElement -> groceryElement.getParsedProducts().clear());

        log.info("Deleted products for grocery list with id: {}", groceryList.getId());
    }

    @Transactional
    private void saveProduct(ProductParsedFromShopDTO parsedFromShopDTO, GroceryElement groceryElement) {

        groceryElement.addAllProducts(productMapper.mapListParsedProductDTOToListProduct(parsedFromShopDTO.getProducts()));
        groceryElement.getParsedProducts().forEach(product -> product.setShopName(parsedFromShopDTO.getShopName()));

        groceryElementRepository.save(groceryElement);
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
