package com.example.managingpromotions.services.shopParser;

import com.example.managingpromotions.models.ProductDTO;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IParser {
    Document fetchDataFromWeb(String URL);
    List<ProductDTO> prepareData(Document document);
}
