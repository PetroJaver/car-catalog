package com.implemica.selenium.pages;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.tool.schema.Action;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;
import static com.implemica.selenium.helpers.BaseTestValues.EDIT_URL_REG_FORMAT;

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

    public void sendKeyByJse(WebElement element, String text){
        text = text.replaceAll("'", "\\\\'");
        text = text.replaceAll("\n", "\\\\n");

        jse.executeScript(String.format("arguments[0].value='%s';", text), element);

        if(text.length() > 0){
            element.sendKeys(Keys.chord(Keys.BACK_SPACE), text.substring(text.length()-1));
        }
    }

    public WebElement getActiveElement(){
        return driver.switchTo().activeElement();
    }
}
