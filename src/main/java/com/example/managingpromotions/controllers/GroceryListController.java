package com.example.managingpromotions.controllers;

import com.example.managingpromotions.services.GroceryListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.managingPromotions.api.model.CreateIdResponse;
import pl.managingPromotions.api.model.GroceryListRequestDTO;
import pl.managingPromotions.api.model.GroceryListResponseDTO;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/grocery-list")
public class GroceryListController {

    private final GroceryListService groceryListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateIdResponse createGroceryList(@RequestBody GroceryListRequestDTO groceryListRequestDTO) {
        return groceryListService.createGroceryList(groceryListRequestDTO);
    }

    @GetMapping(value = "/{userName}")
    @ResponseStatus(HttpStatus.OK)
    public List<GroceryListResponseDTO> getGroceryLists(@PathVariable String userName) {
        return groceryListService.getGroceryListForUser(userName);
    }
}
