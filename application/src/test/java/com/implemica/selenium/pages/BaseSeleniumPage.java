package com.implemica.selenium.pages;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;

public class BaseSeleniumPage {
    protected static WebDriver driver;
    private static JavascriptExecutor jse;
    protected static WebDriverWait webDriverWait;

    public static void setDriver(WebDriver webDriver){
        driver = webDriver;
        jse = (JavascriptExecutor) driver;
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void scrollUp() {
        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
    }

    public void scrollDown(){
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void clickByJse(WebElement element){
        jse.executeScript("arguments[0].click()", element);
    }
}
