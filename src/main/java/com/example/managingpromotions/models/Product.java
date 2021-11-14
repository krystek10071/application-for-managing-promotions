package com.example.managingpromotions.models;


import javax.persistence.*;
import java.util.Date;

@Entity(name = "product")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name="product_name", length = 50, nullable = false)
    private String productName;

    @Column(name="description")
    private String description;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="category")
    private String category;

    @Column(name="expiry_date")
    private Date expiryDate;

    @Column(name ="link_to_image")
    private String linkToImage;
}
