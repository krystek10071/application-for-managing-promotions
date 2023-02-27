package com.example.managingpromotions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoryDictionary")
public class CategoryDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category_dictionary")
    @SequenceGenerator(
            name = "seq_category_dictionary",
            sequenceName = "seq_category_dictionary",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "name")
    private String name;

}
