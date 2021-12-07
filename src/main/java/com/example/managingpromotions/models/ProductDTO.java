package com.example.managingpromotions.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
    private String productName;
    private String description;
    private BigDecimal price;
    private String category;
    private Date expiryData;
    private String linkToImage;
}
