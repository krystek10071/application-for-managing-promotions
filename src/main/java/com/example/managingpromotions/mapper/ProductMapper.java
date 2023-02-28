package com.example.managingpromotions.mapper;

import com.example.managingpromotions.model.Product;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.managingPromotions.api.model.ParsedProductDTO;
import pl.managingPromotions.api.model.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring",
        builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    @Mapping(source = "productName", target = "name")
    ParsedProductDTO mapProductDTOSToParsedProductDTO(ProductDTO productDTO);

    List<ParsedProductDTO> mapListProductDTOToListParsedProductDTO(List<ProductDTO> productDTOS);

    List<Product> mapListParsedProductDTOToListProduct(List<ParsedProductDTO> parsedProductDTOS);

    @Mapping(source = "name", target = "productName")
    Product mapParsedProductDTOToProduct(ParsedProductDTO parsedProductDTO);

    @Mapping(target = "name", source = "product.productName")
    @Mapping(target = "price", source = "product.price")
    ParsedProductDTO mapProductToParseProductDTO(Product product, String unit, BigDecimal amount);
}
