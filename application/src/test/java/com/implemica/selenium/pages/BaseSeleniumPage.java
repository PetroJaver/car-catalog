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

public class BaseSeleniumPage {
    protected static WebDriver driver;

    public static JavascriptExecutor jse;
    public static Actions actions;

    public static void setDriver(WebDriver webDriver){
        driver = webDriver;
        actions = new Actions(driver);
        jse = (JavascriptExecutor) driver;
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

    public void enterDataByJse(WebElement element, String data) {
        data = data.replaceAll("\n","\\\\n");
        String dataPartForExucuter = data.substring(0,data.length()-1);
        String dataPartForBrowser = Character.toString(data.charAt(data.length()-1));

        String script = String.format("arguments[0].value='%s';", dataPartForExucuter);
        jse.executeScript(script, element);

        element.sendKeys(dataPartForBrowser);
    }
}
