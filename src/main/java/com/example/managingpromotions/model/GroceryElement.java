package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grocery_element")
public class GroceryElement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_grocery_element")
    @SequenceGenerator(
            name = "seq_grocery_element",
            sequenceName = "seq_grocery_element",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit")
    private String unit;

    @Column(name = "amount")
    private Double amount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groceryElement")
    @Fetch(FetchMode.JOIN)
    private List<Product> parsedProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grocery_list_id", referencedColumnName = "id")
    private GroceryList groceryList;
}
