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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        for (GroceryListProductDTO productDTO : groceryListProductDTOS) {
            productParsedFromShopDTOS.add(fetchDataFromEleclerc(productDTO, groceryListId));
        }

        return Collections.emptyList();
    }

    private ProductParsedFromShopDTO fetchDataFromEleclerc(GroceryListProductDTO groceryListProductDTO, Long groceryListId) {
        Document document = eleclercParser.fetchDataFromWeb(groceryListProductDTO.getName());

        ProductParsedFromShopDTO productParsedFromShopDTO = new ProductParsedFromShopDTO();

        productParsedFromShopDTO.setGroceryListId(groceryListId);
        productParsedFromShopDTO.setShopName(ShopEnum.ELECLERC.getValue());

        List<ProductDTO> productDTOS = eleclercParser.prepareData(document).subList(0, 5);
        List<ParsedProductDTO> parsedProductDTOS = productMapper.mapListProductDTOToListParsedProductDTO(productDTOS);

        parsedProductDTOS.forEach(productParsedDTO -> {
            productParsedDTO.setUnit(groceryListProductDTO.getUnit());
            productParsedDTO.amount(groceryListProductDTO.getAmount());
        });

         productParsedFromShopDTO.setProducts(parsedProductDTOS);

         return productParsedFromShopDTO;
    }

}
