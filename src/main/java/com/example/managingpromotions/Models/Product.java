package com.example.managingpromotions.Models;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PRODUCT")
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @Column(name="PRODUCT_NAME", length = 50, nullable = false)
    private String productName;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE", nullable = false)
    private double price;

    @Column(name="CATEGORY")
    private String category;

    @Column(name="EXPIRY_DATE")
    private Date expiryDate;
}
