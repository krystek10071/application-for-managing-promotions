package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.exception.NewsletterFetchProcessException;
import com.example.managingpromotions.model.NewsletterFile;
import com.example.managingpromotions.model.repository.NewsletterFileRepository;
import com.example.managingpromotions.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
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
public class LetterNewsletterGroszekService extends LetterNewsLetterAbstract implements LetterNewsLetter {

    private final String URL_NEWSLETTER_GROSZEK = "https://www.gazetkipromocyjne.net/groszek/";
    private static final String TEST_URL = "https://www.gazetkipromocyjne.net/wp-content/uploads/pdf/29341__634d6751ba3a6.pdf";
    private static final String DESTINATION = "/groszek";

    private final WebDriver firefoxDriver;
    private final CloseableHttpClient httpClient;

    private final UserRepository userRepository;
    private final NewsletterFileRepository newsletterFileRepository;
    private final LetterNewsLetterProperty letterNewsLetterProperty;

    @Override
    public void fetchPDFFromWeb() throws IOException {

        createDirectory(DESTINATION, letterNewsLetterProperty.getRootLocation());
        String fullDestination = appendDestinationToRoot(DESTINATION, letterNewsLetterProperty.getRootLocation());

        HttpGet request = new HttpGet(fetchUrlToNewsLetterAddress(URL_NEWSLETTER_GROSZEK));
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();

        //todo search pdf newsLetter fileName in next TASK
        String fileName = generateFileName();
        File pdfFile = new File(fullDestination + "/" + "pdfFile.pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

        if (httpEntity != null) {
            try {
                httpEntity.writeTo(fileOutputStream);
            } catch (IOException e) {
                log.error("Error in fetch newsletter Eleclerc");
            }
        }
        fileOutputStream.close();
        createAndSaveEntityPdfFile(pdfFile);
    }

    @Override
    public String fetchUrlToNewsLetterAddress(String urlNewsLetter) {

        try {
            firefoxDriver.navigate().to(URL_NEWSLETTER_GROSZEK);
            firefoxDriver.findElement(By.cssSelector(".newspapper-btn")).click();
        } catch (Exception e) {
            String description = this.getClass() + "No find elements with newsletter";
            throw new NewsletterFetchProcessException(description);
        }

        //todo fetch URL from website
        return TEST_URL;
    }

    private void createAndSaveEntityPdfFile(File pdfFile) {
        NewsletterFile newsletterFile = NewsletterFile.builder()
                .fileName("fileName")
                .path(pdfFile.getPath())
                .extension("pdf")
                .build();

        newsletterFileRepository.save(newsletterFile);
    }

    private String generateFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return "newspapperGroszek" + date + ".pdf";
    }
}
