package com.example.managingpromotions.model;

import com.example.managingpromotions.model.enums.RoleUserEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
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

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleUserEnum role;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "userApp")
    private Set<GroceryList> groceryLists;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "userApp")
    private Set<FavouriteProduct> favouriteProducts;
}
