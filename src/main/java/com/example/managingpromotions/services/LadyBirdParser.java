package com.example.managingpromotions.services;

import com.example.managingpromotions.models.ProductDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("ladyBirdParser")
public class LadyBirdParser implements IParser{

    @Override
    public Document fetchDataFromWeb(String URL) {
         final String urlAdress = "https://www.biedronka.pl/pl/w-tym-tyg-27-05";

        try {
            return Jsoup.connect(urlAdress).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource failed to fetch from ladyBird shop");
        }
        return null;
    }

    @Override
    public List<ProductDTO> prepareData() {
        Document document = fetchDataFromWeb("https://www.biedronka.pl/pl/w-tym-tyg-27-05");
        List<ProductDTO> listProducts = new ArrayList<>();

        if(document!=null){
            Elements rows =  document.select("#container > div:nth-child(2) > div > div.slot > div > a > div > span > span > span.price > span > span.pln");
        //#produkt-227536 > a > div > span > span > span.price > span > span.pln
          System.out.println(rows);

            for(Element row: rows){
                ProductDTO product = new ProductDTO();
                //  product.setProductName(row.attr();
              //  product.setPrice(row.text());
                System.out.println(row.text());
              //  System.out.println(product.getProductName());
            }


        }


        return null;
    }
}
