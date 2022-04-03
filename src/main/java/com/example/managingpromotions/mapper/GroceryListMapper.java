package com.example.managingpromotions.mapper;

import com.example.managingpromotions.models.GroceryList;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.GroceryListRequestDTO;
import pl.managingPromotions.api.model.GroceryListResponseDTO;

import java.util.List;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface GroceryListMapper {

    @Mapping(source = "products", target = "groceryElements")
    GroceryList mapGroceryListRequestDTOToGroceryList(GroceryListRequestDTO groceryListRequestDTO);

    @Mapping(source = "GroceryList.groceryElements", target = "GroceryListResponseDTO.products")
    List<GroceryListResponseDTO> mapGroceryListToGroceryListResponseDTO(List<GroceryList> groceryLists);
}
