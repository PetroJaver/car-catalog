package com.implemica.selenium.helpers;

import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.EditCarPage;
import com.implemica.selenium.pages.LogInPage;
import com.implemica.selenium.tests.BaseSeleniumTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.implemica.model.enums.CarBodyType.COUPE;
import static com.implemica.model.enums.CarBrand.PORSCHE;
import static com.implemica.selenium.helpers.BaseTestValues.*;
import static com.implemica.selenium.helpers.DetailsCarTestValues.ANY_MODEL;
import static com.implemica.selenium.helpers.EditCarTestValues.XPATH_LAST_DELETE_CAR_BUTTON;
import static com.implemica.selenium.helpers.EditCarTestValues.XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON;

public class CreateCarForTest extends BaseSeleniumTest {
    protected static EditCarPage editCarPage;
    protected static WebDriverWait webDriverWait;

    @BeforeAll
    public static void beforeAll() {
        new LogInPage().openLoginPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD).clickAddCarButton();
        AddCarPage addCarPage = new AddCarPage();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        editCarPage = new EditCarPage();

        addCarPage.openAddCarPage().addCar(CarValue.builder().brand(PORSCHE).model(ANY_MODEL).bodyType(COUPE).build());
        addCarPage.clickByJse(addCarPage.addCarButton);
    }

    @AfterAll
    public static void afterAll() {
        driver.get(BASE_URL);
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_DELETE_CAR_BUTTON)));
        editCarPage.clickByJse(driver.findElement(By.xpath(XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON)));

    }
}