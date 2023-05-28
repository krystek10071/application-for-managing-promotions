package com.example.managingpromotions.services.shopParser;

import com.example.managingpromotions.util.StringUtils;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.managingPromotions.api.model.ProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@Service("eleclercParser")
public class EleclercParser implements IParser {

    private static final String URL_SHOP_PREFIX = "https://leclercdrive.lublin.pl/szukaj?word=";
    private static final String URL_SHOP_POSTFIX = "&search_box_btn_submit=";

    @Qualifier("edgeDriver")
    private final WebDriver edgeDriver;

    @Override
    public Document fetchDataFromWeb(String nameProduct) {
        String eleclercProductUrl = URL_SHOP_PREFIX + nameProduct + URL_SHOP_POSTFIX;
        return fetchDataByWebDriver(eleclercProductUrl);
    }

    @Override
    public List<ProductDTO> prepareData(Document document) {
        List<ProductDTO> listProductDTO = new ArrayList<>();

        if (document != null) {
            Elements elementsPromotionsFromShop = document.select("div.l-listing > div.product-listing");

            for (var row : elementsPromotionsFromShop) {
                ProductDTO product = new ProductDTO();

                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));

                listProductDTO.add(product);
            }
        }
        return listProductDTO;
    }

    private String findProductDescription(Element row) {
        String productDescription = row.select("div.product-listing-name").text();
        return StringUtils.shortenString(productDescription);
    }

    private Document fetchDataByWebDriver(String eleclercProductUrl) {
        edgeDriver.get(eleclercProductUrl);
        return Jsoup.parse(edgeDriver.getPageSource());
    }

    private String findPriceProduct(Element row) {
        String price = row.select("span.product-listing-prices__price").text();

        return Arrays.stream(price.split(" "))
                .findFirst()
                .orElse("")
                .replace(",", ".");
    }

    private String findProductNameInDocument(Element row) {
        String productName = row.select("div.product-listing-name").text();
        return StringUtils.shortenString(productName);
    }
}
