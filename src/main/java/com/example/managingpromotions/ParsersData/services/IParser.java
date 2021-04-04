package com.example.managingpromotions.ParsersData.services;

import com.example.managingpromotions.Models.ProductDTO;
import org.jsoup.nodes.Document;

import java.util.List;

public interface IParser {
    Document fetchDataFromWeb();
    List<ProductDTO> prepareData();
}
