package com.example.managingpromotions.services.shopParser;

import com.example.managingpromotions.models.ProductDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service("auchanService")
public class AuchanParser implements IParser {
    private static final String URL_SHOP = "https://zakupy.auchan.pl/shop/search?q%5B%5D=mleko&qq=%7B%7D";

    @Override
    public Document fetchDataFromWeb(String URL) {
        try {
            return Jsoup.connect(URL).maxBodySize(0).get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("resource failed to fetch from Daisy");
        }
        return null;
    }

    @Override
    public List<ProductDTO> prepareData(String shopName) {
        List<ProductDTO> listProducts = new ArrayList<>();
        System.setProperty("webdriver.gecko.driver", "D:\\Praca dyplomowa KUL\\managing-promotions\\src\\main\\resources\\webdriver\\geckodriver.exe");

        WebDriver driver = new FirefoxDriver();
        driver.get(URL_SHOP);
        Document document = Jsoup.parse(driver.getPageSource());

        if (document != null) {
            //display document
            Elements elementsPromotionsFromAuchan = document.select("._2XHV > div:nth-child(1) > div:nth-child(1) > div");

            for(var row : elementsPromotionsFromAuchan){
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
        return row.select("a._2J-k").text();
    }

    private String findPriceProduct(Element row) {
        Elements elements = row.select("div._3vje");

        String price = elements.text();

        return Arrays.stream(price.split(" ")).findFirst().get().replace(",", ".");
    }

    private String findProductDescription(Element row) {
        return row.select("a._2J-k").text();
    }

    private String findLinkToImage(Element row) {
        return row.select("source.data-srcset").text();
    }
}