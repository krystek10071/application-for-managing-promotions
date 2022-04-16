package com.example.managingpromotions.mapper;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.ParsedProductDTO;
import pl.managingPromotions.api.model.ProductDTO;

import java.util.List;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    @Mapping(source = "productName", target = "name")
    ParsedProductDTO mapProductDTOSToParsedProductDTO(ProductDTO productDTO );

    List<ParsedProductDTO> mapListProductDTOToListParsedProductDTO(List<ProductDTO> productDTOS);
}
