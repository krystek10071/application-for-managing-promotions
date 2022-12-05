package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_app")
public class UserApp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_app")
    @SequenceGenerator(
            name = "seq_user_app",
            sequenceName = "seq_user_app",
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
            mappedBy = "userApp")
    private Set<GroceryList> groceryLists;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "userApp")
    private Set<FavouriteProduct> favouriteProducts;
}
