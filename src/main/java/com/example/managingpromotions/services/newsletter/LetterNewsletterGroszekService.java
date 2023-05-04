package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.model.NewsletterFile;
import com.example.managingpromotions.model.repository.NewsletterFileRepository;
import com.example.managingpromotions.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.managingPromotions.api.model.NewsletterFromWebDTO;
import pl.managingPromotions.api.model.ShopEnum;

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
    private static final String DESTINATION = "/groszek";

    private final WebDriver firefoxDriver;
    private final CloseableHttpClient httpClient;

    private final UserRepository userRepository;
    private final NewsletterFileRepository newsletterFileRepository;
    private final LetterNewsLetterProperty letterNewsLetterProperty;

    @Override
    @Transactional
    public void fetchPDFFromWeb() throws IOException {

        createDirectory(DESTINATION, letterNewsLetterProperty.getRootLocation());
        String fullDestination = appendDestinationToRoot(DESTINATION, letterNewsLetterProperty.getRootLocation());

        NewsletterFromWebDTO newsletterFromWebDTO = fetchNewsletterDataFromWeb(firefoxDriver, URL_NEWSLETTER_GROSZEK);
        HttpGet request = new HttpGet(newsletterFromWebDTO.getUrlToDownload());
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();

        String fileName = generateFileName();
        File pdfFile = new File(fullDestination + "/" + fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

        if (httpEntity != null) {
            try {
                httpEntity.writeTo(fileOutputStream);
            } catch (IOException e) {
                log.error("Error in fetch newsletter Groszek");
            }
        }
        fileOutputStream.close();
        createAndSaveEntityPdfFile(pdfFile, fileName, newsletterFromWebDTO);
    }

    private void createAndSaveEntityPdfFile(File pdfFile, String fileName, NewsletterFromWebDTO newsletterFromWebDTO) {

        LocalDate[] datesStartAndEnd = getDateRange(newsletterFromWebDTO.getDate());

        NewsletterFile newsletterFile = NewsletterFile.builder()
                .fileName(fileName)
                .shopName(ShopEnum.GROSZEK)
                .path(pdfFile.getPath())
                .createdDate(LocalDate.now())
                .starDate(datesStartAndEnd[0])
                .endDate(datesStartAndEnd[1])
                .extension("pdf")
                .build();

        newsletterFileRepository.save(newsletterFile);
    }

    private String generateFileName() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return "newspapperGroszek" + date + ".pdf";
    }
}
