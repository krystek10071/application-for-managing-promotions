package com.example.managingpromotions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_shops")
    @SequenceGenerator(
            name = "seq_shops",
            sequenceName = "seq_shops",
            allocationSize = 1)
    private Long id;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "open_hour")
    private String openHour;

    @Column(name = "close_hour")
    private String closeHour;

    @OneToMany(cascade = CascadeType.ALL
            , mappedBy = "shop")
    private Set<Product> product;
}
