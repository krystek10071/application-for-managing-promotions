package com.example.managingpromotions.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    @Bean
    public WebDriver webDriver(){
         System.setProperty("webdriver.chrome.driver", "src/main/resources/webdriver/chromedriver.exe");
        return new ChromeDriver();
    }
}
