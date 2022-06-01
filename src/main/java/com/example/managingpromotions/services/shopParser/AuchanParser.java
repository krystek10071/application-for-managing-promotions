package com.example.managingpromotions.services.shopParser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.ProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service("auchanService")
@AllArgsConstructor
public class AuchanParser implements IParser {
    private static final String URL_SHOP_PREFIX = "https://zakupy.auchan.pl/shop/search?q%5B%5D=";
    private static final String URL_SHOP_POSTFIX = "&qq=%7B%7D";

    @Qualifier("operaDriver")
    private final WebDriver chromeDriver;

    @Override
    @Transactional
    public Document fetchDataFromWeb(String nameProduct) {
        String auchanProductUrl = URL_SHOP_PREFIX + nameProduct + URL_SHOP_POSTFIX;
        return fetchDataByWebDriver(auchanProductUrl);
    }

    @Override
    public List<ProductDTO> prepareData(Document document) {
        List<ProductDTO> listProductDTO = new ArrayList<>();

        if (document != null) {
            //display document
            Elements elementsPromotionsFromAuchan = document.select("._2XHV > div:nth-child(1) > div:nth-child(1) > div");

            for (var row : elementsPromotionsFromAuchan) {
                ProductDTO product = new ProductDTO();

                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));
                //   product.setLinkToImage(findLinkToImage(row));

                listProductDTO.add(product);
            }
        }
        return listProductDTO;
    }

    private Document fetchDataByWebDriver(String auchanProductUrl) {
        chromeDriver.get(auchanProductUrl);
        return Jsoup.parse(chromeDriver.getPageSource());
    }

    private String findProductNameInDocument(Element row) {
        return row.select("a._2J-k").text();
    }

    private String findPriceProduct(Element row) {
        Elements elements = row.select("div._3vje");

        String price = elements.text();

        return Arrays.stream(price.split(" "))
                .findFirst()
                .get()
                .replace(",", ".");
    }

    private String findProductDescription(Element row) {
        return row.select("a._2J-k").text();
    }

    private String findLinkToImage(Element row) {
        return row.select("source.data-srcset").text();
    }
}