package com.example.managingpromotions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grocery_list")
public class GroceryList {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grocery_list")
    @SequenceGenerator(
            name = "seq_grocery_list",
            sequenceName = "seq_grocery_list",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "createDate")
    private LocalDate createDate;

    @Column(name = "modifyDate")
    private LocalDate modifyDate;

    @OneToMany(cascade = CascadeType.ALL,
    mappedBy = "groceryList")
    private Set<GroceryElement> groceryElements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserApp userApp;
}
