package com.example.managingpromotions.services.newsletter;

import lombok.AllArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpClient;

@Service
@AllArgsConstructor
public class LetterNewsletterServiceEleclerc implements LetterNewsLetter {

    private static final String URL_NEWSLETTER_ELECLERC = "https://www.gazetkipromocyjne.net/e-leclerc/";
    private static final String TEST_URL = "https://www.gazetkipromocyjne.net/wp-content/uploads/pdf/29341__634d6751ba3a6.pdf";

    @Qualifier("firefoxDriver")
    private final WebDriver firefoxDriver;

    private final HttpClient httpClient;

    public void fetchPDFFromWeb() throws IOException, InterruptedException {

        firefoxDriver.navigate().to(URL_NEWSLETTER_ELECLERC);
        firefoxDriver.findElement(By.cssSelector(".newspapper-btn")).click();
        firefoxDriver.findElement(By.cssSelector("a.newspapper-nav-item:nth-child(5)")).click();

        File myFile = new File("mystuff.bin");

        CloseableHttpClient client = HttpClients.createDefault();
        try (CloseableHttpResponse response = client.execute(new HttpGet(TEST_URL))) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (FileOutputStream outstream = new FileOutputStream(myFile)) {
                    entity.writeTo(outstream);
                }
            }
        }
    }

}
