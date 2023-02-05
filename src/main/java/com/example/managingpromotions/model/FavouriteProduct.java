package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(name = "product_name")
    private String productName;

    @Column(name = "is_observe")
    private Boolean isObserve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_app_id", referencedColumnName = "id")
    private UserApp userApp;
}
