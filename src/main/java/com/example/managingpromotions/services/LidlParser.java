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

    @Override
    public Document fetchDataFromWeb(String URL) {

        try {
            return Jsoup.connect(URL).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource failed to fetch from LIDL");
        }
        return null;
    }

    @Override
    public List<ProductDTO> prepareData(String shopName) {
        List<ProductDTO> listProducts = new ArrayList<>();
        Document document = fetchDataFromWeb("https://ding.pl/oferty?contractors[]=130-Lidl&categories[]=4-Spo%C5%BCywcze");

        if (document != null) {
            //display document
            Elements elementsPromotionsFromLidl = document.select("section._1q38i8t:nth-child(3) > div > div > div > div > div" );
            System.out.println("Elements size is: " + elementsPromotionsFromLidl.size());

            for (Element row : elementsPromotionsFromLidl) {
               ProductDTO product = new ProductDTO();

               product.setProductName(findProductNameInHTML(row));
               product.setDescription("");
               product.setPrice(Double.parseDouble(findPrice(row)));


                listProducts.add(product);
            }
        }
        return listProducts;
    }

    private String findProductNameInHTML(Element row) {
        Elements elementWithoutTextOfChildren = row.select("div._15inhnc");
        return elementWithoutTextOfChildren.first().ownText();
    }

    private String findDescription(Element row){
        return "";
    }

    private String findPrice(Element row){
        Elements elementsWithPriceBeforeDecimalPoint = row.select("span._1423ztp");
        Elements elementsWithPriceAfterDecimalPoint = row.select("sup");

        String valuePriceBeforePoint = elementsWithPriceBeforeDecimalPoint.first().ownText();
        String valuePriceAfterPoint = elementsWithPriceAfterDecimalPoint.first().ownText();

        return valuePriceBeforePoint + "." + valuePriceAfterPoint;
    }

    private String findCategory(Element row){
        return "";
    }

    private String findExpiryDate(Element row){
        Elements elementWithExpiryDate = row.select("span._wdbkny-7");
        return elementWithExpiryDate.first().ownText();
    }
}