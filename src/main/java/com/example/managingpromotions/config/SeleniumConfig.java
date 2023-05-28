package com.example.managingpromotions.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.opera.OperaDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    private static final String FILE_DOWNLOAD_LOCATION = "C:\\projects\\managing-promotions\\src\\main\\resources\\file\\newslater\\groszek";

/*    @Bean
    public WebDriver chromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver.exe");
        return new ChromeDriver();
    }*/

    @Bean
    public WebDriver firefoxDriver() {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/webdriver/geckodriver.exe");

        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                "text/csv,application/java-archive, application/x-msexcel,application/excel," +
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                        "application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain," +
                        "application/msword,application/xml,application/vnd.microsoft.portable-executable");

        firefoxProfile.setPreference("pdfjs.disabled", true);
        firefoxProfile.setPreference("browser.download.dir", FILE_DOWNLOAD_LOCATION);
        firefoxProfile.setPreference("plugin.scan.Acrobat", "99.0");
        firefoxProfile.setPreference("plugin.scan.plid.all", false);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(firefoxProfile);

        return new FirefoxDriver(firefoxOptions);
    }

/*    @Bean
    public WebDriver edgeDriver() {
        System.setProperty("webdriver.edge.driver", "src/main/resources/webdriver/msedgedriver.exe");
        return new EdgeDriver();
    }

    @Bean
    public WebDriver operaDriver() {
        System.setProperty("webdriver.opera.driver", "src/main/resources/webdriver/operadriver.exe");
        return new OperaDriver();
    }*/
}
