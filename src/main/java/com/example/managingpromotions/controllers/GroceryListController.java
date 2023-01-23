package com.example.managingpromotions.controllers;


import com.example.managingpromotions.services.GroceryListService;
import com.example.managingpromotions.services.ShopService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.managingPromotions.api.model.CreateIdResponse;
import pl.managingPromotions.api.model.GroceryListModifyRequestDTO;
import pl.managingPromotions.api.model.GroceryListRequestDTO;
import pl.managingPromotions.api.model.GroceryListResponseDTO;
import pl.managingPromotions.api.model.ProductParsedFromShopDTO;

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
    List<ProductParsedFromShopDTO> getTheCheapestShop(@RequestParam Long groceryListId) throws InterruptedException {
        return shopService.getCheapestShop(groceryListId);
    }
}
