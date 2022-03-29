package com.example.managingpromotions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favourite_product")
public class FavouriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_favourite_product")
    @SequenceGenerator(
            name = "seq_favourite_product",
            sequenceName = "seq_favourite_product",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_app_id", referencedColumnName = "id")
    private UserApp userApp;
}
