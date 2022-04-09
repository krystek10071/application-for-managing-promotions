package com.example.managingpromotions.models.repository;

import com.example.managingpromotions.models.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroceryListRepository extends JpaRepository<GroceryList, Long> {

    List<GroceryList> findByUserAppId(Long userId);
}
