package com.example.managingpromotions.services.newsletter;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LetterNewsletterGroszekService {

    private final String URL_NEWSLETTER_GROSZEK = "https://groszek.com.pl/gazetka/?leaflet=1";

    @Qualifier("firefoxDriver")
    private final WebDriver firefoxDriver;

    public void fetchPDFFromWeb() {

        try {
            firefoxDriver.get(URL_NEWSLETTER_GROSZEK);
            Document document = Jsoup.parse(firefoxDriver.getPageSource());

            String urlNewspaperGroszek = document.select("a.read-more").attr("href");

            firefoxDriver.get(urlNewspaperGroszek);

            createAndSaveFileEntity(document);
        } catch (WebDriverException e) {
            e.getMessage();
        }
    }

    private void createAndSaveFileEntity(Document document) {
    }

}
