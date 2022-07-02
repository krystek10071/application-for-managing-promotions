package com.example.managingpromotions.services.newsletter;

import lombok.AllArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

@Service
@AllArgsConstructor
public class LetterNewsletterServiceEleclerc {

    private final String URL_NEWSLETTER_ELECLERK = "https://leclerc.pl/gazetki/";

    @Qualifier("firefoxDriver")
    private final WebDriver firefoxDriver;

    public void fetchPDFFromWeb() throws AWTException, InterruptedException {

        System.setProperty("java.awt.headless", "false");

        Robot robot = new Robot();
        //disable headless environment in system

        firefoxDriver.navigate().to(URL_NEWSLETTER_ELECLERK);
        Thread.sleep(5000L);
        firefoxDriver.findElement(By.id("cookie_action_close_header")).click();
        firefoxDriver.findElement(By.className("entry-content")).click();
        Thread.sleep(3000L);
        firefoxDriver.findElement(By.className("fa-print")).click();
        Thread.sleep(3000L);
        firefoxDriver.findElement(By.className("c-p")).click();

        String parentWindow = firefoxDriver.getWindowHandle();

        Set<String> allWindows = firefoxDriver.getWindowHandles();
        for (String curWindow : allWindows) {
            firefoxDriver.switchTo().window(curWindow);
            firefoxDriver.manage().window().maximize();
        }

        firefoxDriver.manage().window().maximize();
        Thread.sleep(6000L);
        robot.keyPress(KeyEvent.VK_ENTER);

        Thread.sleep(5000L);

        robot.keyPress(KeyEvent.VK_F);
        robot.keyPress(KeyEvent.VK_I);
        robot.keyPress(KeyEvent.VK_L);
        robot.keyPress(KeyEvent.VK_E);
        robot.keyPress(KeyEvent.VK_1);
        Thread.sleep(1000L);
        robot.keyPress(KeyEvent.VK_ENTER);


    /*    firefoxDriver.close();
        firefoxDriver.switchTo().window(parentWindow);*/
    }

}
