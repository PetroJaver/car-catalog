package com.implemica.application.util.selenium.pages;

import com.implemica.application.util.readproperties.ConfigProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class BaseSeleniumPage {
    protected static WebDriver driver;

    protected static JavascriptExecutor jse;

    public static void setDriver(WebDriver webDriver){
        driver = webDriver;
        jse = (JavascriptExecutor) driver;
    }

    protected void scrollUp(){
        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
    }

    protected void scrollDown(){
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public void scrollDownUp() throws Exception{
        scrollDown();
        Thread.sleep(500);
        scrollUp();
        Thread.sleep(300);
    }

    public void openMainPage(){
        driver.get(ConfigProvider.URL);
    }
}
