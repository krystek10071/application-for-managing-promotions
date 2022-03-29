package com.example.managingpromotions.services.shopParser;

import org.jsoup.nodes.Document;
import pl.managingPromotions.api.model.ProductDTO;

import java.util.List;

public interface IParser {
    Document fetchDataFromWeb(String URL);

    List<ProductDTO> prepareData(Document document);
}
