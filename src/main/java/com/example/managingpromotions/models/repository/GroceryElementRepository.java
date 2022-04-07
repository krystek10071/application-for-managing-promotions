package com.example.managingpromotions.models.repository;

import com.example.managingpromotions.models.GroceryElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroceryElementRepository extends JpaRepository<GroceryElement, Long> {

    List<GroceryElement> findByGroceryListId(Long id);
}
