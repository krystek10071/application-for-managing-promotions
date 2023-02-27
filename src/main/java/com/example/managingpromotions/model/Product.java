package com.example.managingpromotions.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.managingPromotions.api.model.ShopEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product")
    @SequenceGenerator(
            name = "seq_product",
            sequenceName = "seq_product",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "shop_name")
    private ShopEnum shopName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grocery_element_id", referencedColumnName = "id")
    private GroceryElement groceryElement;
}
