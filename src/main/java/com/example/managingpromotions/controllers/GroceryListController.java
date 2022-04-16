package com.example.managingpromotions.controllers;

import com.example.managingpromotions.services.GroceryListService;
import com.example.managingpromotions.services.ShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.managingPromotions.api.model.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/grocery-list")
public class GroceryListController {

    private final ShopService shopService;
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

    @PatchMapping(value = "/{groceryListId}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyGroceryList(@RequestParam long groceryListId, @RequestBody GroceryListModifyRequestDTO groceryListModifyRequestDTO) {
        groceryListService.patchGroceryList(groceryListId, groceryListModifyRequestDTO);
    }

    @DeleteMapping(value = "/{groceryListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroceryList(@RequestParam long groceryListId) {
        groceryListService.deleteGroceryList(groceryListId);
    }

    @GetMapping(value = "/{groceryList}/product-shop")
    List<ProductParsedFromShopDTO> getTheCheapestShop(@RequestParam Long groceryListId){
        return shopService.getCheapestShop(groceryListId);
    }
}
