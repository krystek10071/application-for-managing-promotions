package com.example.managingpromotions.mapper;

import com.example.managingpromotions.models.GroceryElement;
import com.example.managingpromotions.models.GroceryList;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.GroceryListProductDTO;
import pl.managingPromotions.api.model.GroceryListRequestDTO;
import pl.managingPromotions.api.model.GroceryListResponseDTO;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface GroceryListMapper {

    @Mapping(source = "products", target = "groceryElements")
    GroceryList mapGroceryListRequestDTOToGroceryList(GroceryListRequestDTO groceryListRequestDTO);

    List<GroceryListResponseDTO> mapGroceryListToGroceryListResponseDTO(List<GroceryList> groceryLists);

    @Mapping(source = "groceryElements", target = "products")
    GroceryListResponseDTO mapGroceryListToGroceryListResponseDTO(GroceryList groceryList);

    List<GroceryListProductDTO> mapSetGroceryElementToGroceryListProductDTO(Set<GroceryElement> groceryElements);

    GroceryListProductDTO mapGroceryElementToGroceryListProductDTO(GroceryElement groceryElement);

    Set<GroceryElement> mapListGroceryListProductDTOToListGroceryElement(List<GroceryListProductDTO> groceryListProductDTOS);
}
