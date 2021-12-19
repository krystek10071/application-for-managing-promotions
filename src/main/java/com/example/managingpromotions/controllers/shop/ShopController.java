package com.example.managingpromotions.controllers.shop;

import com.example.managingpromotions.models.ProductDTO;
import com.example.managingpromotions.services.shopParser.AuchanParser;
import com.example.managingpromotions.services.shopParser.CarrefourParser;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("api/v1/shops")
@AllArgsConstructor
public class ShopController {

    private final CarrefourParser carrefourParser;
    private final AuchanParser auchanParser;

    @GetMapping("/carrefour")
    List<ProductDTO> findProductInCareFour(@RequestParam String nameProduct) {
        Document document = carrefourParser.fetchDataFromWeb(nameProduct);
        return carrefourParser.prepareData(document);
    }

    @GetMapping("/auchan")
    List<ProductDTO> findProductInAuchan(@RequestParam String nameProduct){
        Document document = auchanParser.fetchDataFromWeb(nameProduct);
        return auchanParser.prepareData(document);
    }
}
