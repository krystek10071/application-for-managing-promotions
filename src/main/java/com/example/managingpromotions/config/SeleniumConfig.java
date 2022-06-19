package com.example.managingpromotions.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    @Bean
    public WebDriver chromeDriver(){
         System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver.exe");
        return new ChromeDriver();
    }

    @Bean
    public WebDriver firefoxDriver(){
        System.setProperty("webdriver.gecko.driver", "src/main/resources/webdriver/geckodriver.exe");
        return new FirefoxDriver();
    }

    @Bean
    public WebDriver edgeDriver(){
        System.setProperty("webdriver.edge.driver", "src/main/resources/webdriver/msedgedriver.exe");
        return new EdgeDriver();
    }

    @Bean
    public WebDriver operaDriver(){
        System.setProperty("webdriver.opera.driver", "src/main/resources/webdriver/operadriver.exe");
        return new OperaDriver();
    }
}
