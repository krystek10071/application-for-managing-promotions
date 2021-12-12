package com.example.managingpromotions.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

    @Bean
    public WebDriver webDriver(){
        System.setProperty("webdriver.gecko.driver", "D:\\Praca dyplomowa KUL\\managing-promotions\\src\\main\\resources\\webdriver\\geckodriver.exe");

        return new FirefoxDriver();
    }
}
