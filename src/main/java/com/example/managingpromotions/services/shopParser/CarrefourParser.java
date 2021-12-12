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
import java.util.Collections;
import java.util.List;

@Slf4j
@Service("carrefourParser")
@AllArgsConstructor
public class CarrefourParser implements IParser {

    private static final String URL_SHOP =  "https://www.carrefour.pl/szukaj?q=mleko";

    private final WebDriver webDriver;

    @Override
    public Document fetchDataFromWeb(String URL) {
        setPropertySeleniumDriver();

        //todo implements
        return null;
    }

    @Override
    public List<ProductDTO> prepareData(String shopName) {
        List<ProductDTO> productDTOList = new ArrayList<>();

        Document document =  fetchDataByWebDriver(webDriver);

        //print document
        System.out.println(document);

        if(document != null) {
            //display document
            Elements elementsPromotionsFromCarrefour =  document.select(".jss277");

            for(var row : elementsPromotionsFromCarrefour){
                ProductDTO product = new ProductDTO();
                
                product.setProductName(findProductNameInDocument(row));
                product.setPrice(new BigDecimal(findPriceProduct(row)));
                product.setDescription(findProductDescription(row));
                product.setLinkToImage(findLinkToImage(row));

                System.out.println(" ");
                System.out.println(row);
            }
        }

        return Collections.emptyList();
    }

    private String findProductNameInDocument(Element row) {
        return row.select("a.MuiButtonBase-root").text();
    }

    private String findPriceProduct(Element row) {
        String price  = row.select("div.MuiTypography-root").text();

        return Arrays.stream(price.split(" ")).findFirst().get().replace(",", ".");
    }

    private String findProductDescription(Element row) {
        return row.select("a.MuiButtonBase-root").text();
    }

    private String findLinkToImage(Element row) {
        return row.select("img").text();
    }


    private void setPropertySeleniumDriver() {
        System.setProperty("webdriver.gecko.driver", "D:\\Praca dyplomowa KUL\\managing-promotions\\src\\main\\resources\\webdriver\\geckodriver.exe");
    }

    private Document fetchDataByWebDriver(WebDriver webDriver) {
        webDriver.get(URL_SHOP);
        return Jsoup.parse(webDriver.getPageSource());
    }
}