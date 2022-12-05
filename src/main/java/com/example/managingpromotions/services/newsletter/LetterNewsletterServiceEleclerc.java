package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.model.NewsletterFile;
import com.example.managingpromotions.model.repository.NewsletterFileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@AllArgsConstructor
public class LetterNewsletterServiceEleclerc extends LetterNewsLetterAbstract implements LetterNewsLetter {

    private static final String URL_NEWSLETTER_ELECLERC = "https://www.gazetkipromocyjne.net/e-leclerc/";
    private static final String DESTINATION = "/eLeclerc";

    private final WebDriver firefoxDriver;
    private final CloseableHttpClient httpClient;
    private final LetterNewsLetterProperty letterNewsLetterProperty;
    private final NewsletterFileRepository newsletterFileRepository;

    @Override
    public void fetchPDFFromWeb() throws IOException {

        createDirectory(DESTINATION, letterNewsLetterProperty.getRootLocation());
        String fullDestination = appendDestinationToRoot(DESTINATION, letterNewsLetterProperty.getRootLocation());

        HttpGet request = new HttpGet(fetchUrlToNewsLetterAddress(URL_NEWSLETTER_ELECLERC));
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();

        String fileName = generateFileName();
        File pdfFile = new File(fullDestination + "/" + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

        if (httpEntity != null) {
            try {
                httpEntity.writeTo(fileOutputStream);
            } catch (IOException e) {
                log.error("Error in fetch newsletter Eleclerc");
            }
        }
        fileOutputStream.close();
        createAndSaveEntityPdfFile(pdfFile, fileName);
    }

    @Override
    public String fetchUrlToNewsLetterAddress(String urlNewsLetter) {
        firefoxDriver.navigate().to(URL_NEWSLETTER_ELECLERC);
        firefoxDriver.findElement(By.cssSelector(".newspapper-btn")).click();

        Document document = Jsoup.parse(firefoxDriver.getPageSource());
        return document.select("a.newspapper-nav-item.newspapper-nav-download").attr("href");
    }

    private void createAndSaveEntityPdfFile(File pdfFile, String fileName) {

        NewsletterFile newsletterFile = NewsletterFile.builder()
                .fileName(fileName)
                .path(pdfFile.getPath())
                .createdDate(LocalDate.now())
                .extension("pdf")
                .build();

        newsletterFileRepository.save(newsletterFile);
    }

    private String generateFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return "newspapperEleclerk" + date + ".pdf";
    }
}
