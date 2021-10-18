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

@Service("lidlParser")
public class LidlParser implements IParser {

    //todo you should add String url parameter in fetch dataFromWeb
    @Override
    public Document fetchDataFromWeb(String URL) {
        final String urlAdress = "https://www.lidl.pl/c/czwartek/c4133/w1";

        try {
            return Jsoup.connect(urlAdress).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource failed to fetch from LIDL");
        }
        return null;
    }

    //todo change parameter in fetch data from web
    @Override
    public List<ProductDTO> prepareData() {
        Document document = fetchDataFromWeb("https://www.lidl.pl/c/czwartek/c4133/w1");
        List<ProductDTO> listProducts = new ArrayList<>();

        if(document!=null){
            Elements rows = document.select("#pageMain > div > div > section > div > div > div[data-currency = zł]");

            for(Element row: rows){
                ProductDTO product = new ProductDTO();
                product.setProductName(row.attr("data-name"));
                product.setPrice(Double.parseDouble(row.attr("data-price").replace(",", ".")));
                product.setDescription(row.attr("data-list"));

                String links = row.select("source").attr("data-srcset");
                String link = links.substring(0, links.indexOf(","));
                product.setLinkToImage(link);

                listProducts.add(product);
            }
        }
        return listProducts;
    }
}
