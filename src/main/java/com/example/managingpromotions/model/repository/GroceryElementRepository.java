package com.example.managingpromotions.model.repository;

import com.example.managingpromotions.model.GroceryElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryElementRepository extends JpaRepository<GroceryElement, Long> {

    List<GroceryElement> findByGroceryListId(Long id);
}
