package com.example.managingpromotions.ParsersData.services;

import com.example.managingpromotions.Models.ProductDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("lidlParser")
public class LidlParser implements IParser {

    //todo you should add String url parameter in fetch dataFromWeb
    @Override
    public Document fetchDataFromWeb() {
        final String urlAdress = "https://www.lidl.pl/pl/c/wtorek/c4019/w2";

        try {
            return Jsoup.connect(urlAdress).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource failed to fetch from LIDL");
        }
        return null;
    }

    @Override
    public List<ProductDTO> prepareData() {
        Document document = fetchDataFromWeb();
        List<ProductDTO> listProducts = new ArrayList<>();

        if(document!=null){
            Elements rows = document.select("#pageMain > div > div > section > div > div > div[data-currency = zł]");

            for(Element row: rows){
                ProductDTO product = new ProductDTO();
                product.setProductName(row.attr("data-name"));
                product.setPrice(Double.parseDouble(row.attr("data-price").replace(",", ".")));
                product.setDescription(row.attr("data-list"));
                listProducts.add(product);
                System.out.println(row.attr("data-name"));
            }
        }
        return listProducts;
    }
}
