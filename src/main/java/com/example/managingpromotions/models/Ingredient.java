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
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ingredients")
    @SequenceGenerator(
            name = "seq_ingredients",
            sequenceName = "seq_ingredients",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "calories")
    private Integer calories;

    @Column(name = "carbohydrates")
    private Integer carbohydrates;

    @Column(name = "protein")
    private Integer protein;

    @Column(name = "fats")
    private Integer fats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;
}
