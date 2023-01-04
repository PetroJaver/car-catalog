package com.implemica.selenium.tests;

import com.implemica.selenium.pages.BaseSeleniumPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;

public class BaseSeleniumTest {
    protected static WebDriver driver;
    protected static WebDriverWait webDriverWait;
    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        BaseSeleniumPage.setDriver(driver);

        driver.get("http://localhost:4200/");
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
    }

    @AfterAll
    public static void close(){
        driver.close();
        driver.quit();
    }

    protected static String toTitleCase(String str) {
        String result = "";
        String[] worlds = str.split("_");
        String firstChar;
        String restPart;

        for (String world : worlds) {
            firstChar = Character.toString(world.charAt(0));
            restPart = world.substring(1).toLowerCase();
            result += " " + firstChar + restPart;
        }

        return result.trim();
    }
}
