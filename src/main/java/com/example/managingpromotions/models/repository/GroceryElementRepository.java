package com.example.managingpromotions.models.repository;

import com.example.managingpromotions.models.GroceryElement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroceryElementRepository extends JpaRepository<GroceryElement, Long> {
}
