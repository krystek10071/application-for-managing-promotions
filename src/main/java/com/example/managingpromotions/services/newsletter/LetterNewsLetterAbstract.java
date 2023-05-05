package com.example.managingpromotions.services.newsletter;

import com.example.managingpromotions.exception.NewsletterFetchProcessException;
import com.example.managingpromotions.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pl.managingPromotions.api.model.NewsletterFromWebDTO;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
public abstract class LetterNewsLetterAbstract {

    private static final String DATE_FORMATTER = "dd/MM/yyyy";
    private static final String MAGIC_DASH = "–";

    protected void createDirectory(String destination, String rootLocation) {
        if (!createDirectoryIfNotExists(destination, rootLocation)) {
            String errorMessage = "Failed to create directory: " + destination;
            log.error(errorMessage);
            throw new StorageException(errorMessage);
        }
    }

    private boolean createDirectoryIfNotExists(String destination, String rootLocation) {
        final String fullDestination = appendDestinationToRoot(destination, rootLocation);
        File dir = new File(fullDestination);

        if (!dir.exists()) {
            log.info("Creating folder in destination: {}", fullDestination);
            return dir.mkdirs();
        }

        return true;
    }

    protected String appendDestinationToRoot(final String destination, String rootLocation) {
        return Optional.ofNullable(destination)
                .map(s -> rootLocation + destination)
                .orElse(rootLocation);
    }

    protected NewsletterFromWebDTO fetchNewsletterDataFromWeb(WebDriver firefoxDriver, String urlNewsLetter) {

        try {
            firefoxDriver.navigate().to(urlNewsLetter);
            firefoxDriver.findElement(By.cssSelector(".newspapper-btn")).click();
            Document document = Jsoup.parse(firefoxDriver.getPageSource());
            String url = document.select("a.newspapper-nav-item.newspapper-nav-download").attr("href");
            String date = document.select(".newspapper-footer > p").get(0).text();

            return createNewsletterFromWebDTO(url, date);
        } catch (Exception e) {
            String description = this.getClass() + "No find elements with newsletter";
            throw new NewsletterFetchProcessException(description);
        }
    }

    protected LocalDate[] getDateRange(String dateRange) {
        String[] dateStrings = dateRange.replace(" ", "").split(MAGIC_DASH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        LocalDate startDate = LocalDate.parse(dateStrings[0], formatter);
        LocalDate endDate = LocalDate.parse(dateStrings[1], formatter);
        return new LocalDate[]{startDate, endDate};
    }

    private NewsletterFromWebDTO createNewsletterFromWebDTO(String url, String date) {
        return NewsletterFromWebDTO.builder()
                .urlToDownload(url)
                .date(date)
                .build();
    }
}
