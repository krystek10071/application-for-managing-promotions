package com.example.managingpromotions.services.shopParser;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import pl.managingPromotions.api.model.ProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service("groszekParser")
public class GroszekParser implements IParser {

    private static final String URL_SHOP_PREFIX = "http://egroszek.pl/szukaj.html?q=";
    private static final String FIND_PRODUCTS_STATEMENT = "div.col-md-9:nth-child(2) > div:nth-child(2) > div";

    private final WebDriver webDriver;

    @Override
    public Document fetchDataFromWeb(String nameProduct) {
        String groszekProductsUrl = URL_SHOP_PREFIX + nameProduct;
        return fetchDataByWebDriver(groszekProductsUrl);
    }

    @Override
    public List<ProductDTO> prepareData(Document document) {
        List<ProductDTO> listProductDTO = new ArrayList<>();

        if (document != null) {
            Elements elementsPromotions = document.select(FIND_PRODUCTS_STATEMENT);

            for (var row : elementsPromotions) {
                ProductDTO product = new ProductDTO();

                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));

                listProductDTO.add(product);
            }
        }
        return listProductDTO;
    }

    private String findProductNameInDocument(Element row) {
        return row.select("div.productBox__title").text();
    }

    private String findPriceProduct(Element row) {
        String price = row.select("div.productBox__price").text();

        return (price.isEmpty()) ? "0.00" : price.replace("zł", "");
    }

    private String findProductDescription(Element row) {
        return row.select("div.productBox__title").text();
    }

    private Document fetchDataByWebDriver(String groszekProducts) {
        webDriver.get(groszekProducts);
        System.out.println(webDriver.getPageSource());
        return Jsoup.parse(webDriver.getPageSource());
    }
}
