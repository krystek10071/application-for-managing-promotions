package com.example.managingpromotions.services.shopParser;

import com.example.managingpromotions.models.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service("carrefourParser")
@AllArgsConstructor
public class CarrefourParser implements IParser {

    private static final String URL_SHOP = "https://www.carrefour.pl/szukaj?page=0&size=10&q=";

    private final WebDriver webDriver;

    @Override
    public Document fetchDataFromWeb(String nameProduct) {
        String carrefourProductUrl = URL_SHOP + nameProduct;
        return fetchDataByWebDriver(carrefourProductUrl);
    }

    @Override
    public List<ProductDTO> prepareData(Document document) {
        List<ProductDTO> productDTOList = new ArrayList<>();

        if (document != null) {
            Elements elementsPromotionsFromCarrefour = document.select(" #__next > div > div.MuiBox-root.jss206 > div > div > div > div.jss76 > div > div > div");
            for (var row : elementsPromotionsFromCarrefour) {
                ProductDTO product = new ProductDTO();

                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));
                product.setLinkToImage(findLinkToImage(row));

                productDTOList.add(product);
            }
        }
        return productDTOList;
    }

    private String findProductNameInDocument(Element row) {
        return row.select("a.MuiButtonBase-root").text();
    }

    private String findPriceProduct(Element row) {
        String price = row.select("div.MuiTypography-root").text();

        return Arrays.stream(price.split(" ")).findFirst().get().replace(",", ".");
    }

    private String findProductDescription(Element row) {
        return row.select("a.MuiButtonBase-root").text();
    }

    private String findLinkToImage(Element row) {
        return row.select("div.lazyload-wrapper").text();
    }


    private Document fetchDataByWebDriver(String url) {
        webDriver.get(url);
        return Jsoup.parse(webDriver.getPageSource());
    }

    private String prepareUrl(String nameProduct){
        return URL_SHOP + nameProduct;
    }
}