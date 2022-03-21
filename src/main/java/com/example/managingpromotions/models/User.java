package com.example.managingpromotions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @SequenceGenerator(
            name = "seq_user",
            sequenceName = "seq_user",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean isEnabled;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<Recipe> recipes;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<GroceryList> groceryLists;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user")
    private List<FavouriteProduct> favouriteProducts;
}
