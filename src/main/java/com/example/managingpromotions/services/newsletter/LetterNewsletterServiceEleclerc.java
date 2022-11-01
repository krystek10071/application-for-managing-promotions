package com.example.managingpromotions.services.newsletter;

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

@Slf4j
@Service
@AllArgsConstructor
public class LetterNewsletterServiceEleclerc extends LetterNewsLetterAbstract implements LetterNewsLetter {

    private static final String URL_NEWSLETTER_ELECLERC = "https://www.gazetkipromocyjne.net/e-leclerc/";
    private static final String TEST_URL = "https://www.gazetkipromocyjne.net/wp-content/uploads/pdf/29341__634d6751ba3a6.pdf";
    private static final String DESTINATION = "/eLeclerc";

    private final WebDriver firefoxDriver;
    private final CloseableHttpClient httpClient;
    private final LetterNewsLetterProperty letterNewsLetterProperty;

    @Override
    public String fetchUrlToNewsLetterAddress(String urlNewsLetter) {
        firefoxDriver.navigate().to(URL_NEWSLETTER_ELECLERC);
        firefoxDriver.findElement(By.cssSelector(".newspapper-btn")).click();
        firefoxDriver.findElement(By.cssSelector("a.newspapper-nav-item:nth-child(5)")).click();

        //todo fetch URL from website
        return TEST_URL;
    }

    public void fetchPDFFromWeb() throws IOException {

        createDirectory(DESTINATION, letterNewsLetterProperty.getRootLocation());
        String fullDestination = appendDestinationToRoot(DESTINATION, letterNewsLetterProperty.getRootLocation());

        HttpGet request = new HttpGet(TEST_URL);
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();

        //todo get pdfName
        File myFile = new File(fullDestination + "/" + "pdfFile.pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(myFile);

        if (httpEntity != null) {
            try {
                httpEntity.writeTo(fileOutputStream);
            } catch (IOException e) {
                log.error("Error in fetch newsletter Eleclerc");
            }
        }
        fileOutputStream.close();
    }


}
