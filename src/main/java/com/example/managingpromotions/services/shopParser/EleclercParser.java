package com.example.managingpromotions.services.shopParser;

import com.example.managingpromotions.models.ProductDTO;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("eleclercParser")
@AllArgsConstructor
public class EleclercParser implements IParser{

    private static final String URL_SHOP_PREFIX = "https://leclercdrive.lublin.pl/szukaj?controller=search&orderby=position&orderway=desc&search_query=";
    private static final String URL_SHOP_POSTFIX = "&submit_search=";

    private final WebDriver webDriver;

    @Override
    public Document fetchDataFromWeb(String nameProduct) {
        String eleclercProductUrl = URL_SHOP_PREFIX + nameProduct + URL_SHOP_POSTFIX;
        return fetchDataByWebDriver(eleclercProductUrl);
    }

    @Override
    public List<ProductDTO> prepareData(Document document) {
        List<ProductDTO> listProductDTO = new ArrayList<>();

        if (document != null) {
            //display document
            Elements elementsPromotionsFromAuchan = document.select("ul.product_list > li");

            for (var row : elementsPromotionsFromAuchan) {
                ProductDTO product = new ProductDTO();

                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));
                product.setLinkToImage(findLinkToImage(row));

                listProductDTO.add(product);
            }
        }
        return listProductDTO;
    }

    private String findLinkToImage(Element row) {
        return row.select("#center_column > ul > li:nth-child(1) > div > div.left-block > div > a > img").text();
    }

    private String findProductDescription(Element row) {
        return row.select("a.product-name").text();
    }

    private Document fetchDataByWebDriver(String eleclercProductUrl) {
        webDriver.get(eleclercProductUrl);
        System.out.println(webDriver.getPageSource());
        return Jsoup.parse(webDriver.getPageSource());
    }

    private String findPriceProduct(Element row) {
        String price = row.select("span.price").text();

        return Arrays.stream(price.split(" "))
                .findFirst()
                .get()
                .replace(",", ".");
    }

    private String findProductNameInDocument(Element row) {
        return row.select("a.product-name").text();
    }

}
