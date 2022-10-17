package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.model.NewsletterFileRepository;
import com.example.managingpromotions.model.UserApp;
import com.example.managingpromotions.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class LetterNewsletterGroszekService {

    private final String URL_NEWSLETTER_GROSZEK = "https://groszek.com.pl/gazetka/?leaflet=1";
    private final String PATH_NEWSLETTER_GROSZEK = "C:\\projects\\managing-promotions\\src\\main\\resources\\file\\newslater\\groszek";

    @Qualifier("firefoxDriver")
    private final WebDriver firefoxDriver;
    private final NewsletterFileRepository newsletterFileRepository;
    private final UserRepository userRepository;

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

        UserApp userApp = userRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Not found User"));

        String fileName;
        Date dateFrom;
        Date dateTol;
    }

}
