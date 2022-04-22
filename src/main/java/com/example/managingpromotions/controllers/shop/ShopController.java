package com.example.managingpromotions.controllers.shop;

import com.example.managingpromotions.services.ShopService;
import com.example.managingpromotions.services.shopParser.AuchanParser;
import com.example.managingpromotions.services.shopParser.CarrefourParser;
import com.example.managingpromotions.services.shopParser.EleclercParser;
import com.example.managingpromotions.services.shopParser.GroszekParser;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;
import pl.managingPromotions.api.model.CheapestShoppingReponse;
import pl.managingPromotions.api.model.ListProductParsedFromShopDTO;
import pl.managingPromotions.api.model.ProductDTO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "api/v1/shops")
public class ShopController {

    private final ShopService shopService;
    private final AuchanParser auchanParser;
    private final GroszekParser groszekParser;
    private final EleclercParser eleclercParser;
    private final CarrefourParser carrefourParser;

    @GetMapping(value = "/carrefour")
    public List<ProductDTO> findProductInCareFour(@RequestParam String nameProduct) {
        Document document = carrefourParser.fetchDataFromWeb(nameProduct);
        return carrefourParser.prepareData(document);
    }

    @GetMapping(value = "/auchan")
    public List<ProductDTO> findProductInAuchan(@RequestParam String nameProduct) {
        Document document = auchanParser.fetchDataFromWeb(nameProduct);
        return auchanParser.prepareData(document);
    }

    @GetMapping(value = "/eleclerc")
    public List<ProductDTO> findProductInEleclerc(@RequestParam String nameProduct) {
        Document document = eleclercParser.fetchDataFromWeb(nameProduct);
        return eleclercParser.prepareData(document);
    }

    @GetMapping(value = "/groszek")
    public List<ProductDTO> findProductInGroszek(@RequestParam String nameProduct) {
        Document document = groszekParser.fetchDataFromWeb(nameProduct);
        return groszekParser.prepareData(document);
    }

    @PostMapping(value = "/best-shop")
    public CheapestShoppingReponse findCheapestProducts(@RequestBody ListProductParsedFromShopDTO productParsedFromShopDTO) {

        return shopService.findCheapestProduct(productParsedFromShopDTO.getProducts());
    }
}
