package com.implemica.selenium.tests;

import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.DetailsCarPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class DetailsCarTest extends BaseSeleniumTest {
    private static AddCarPage addCarPage;
    private static DetailsCarPage detailsCarPage;
    private static CatalogAuthPage catalogAuthPage;

    @BeforeAll
    public static void beforeAll() {
        catalogAuthPage = new LogInPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
        addCarPage = new AddCarPage();
        detailsCarPage = new DetailsCarPage();
    }

    @AfterAll
    public static void afterAll() {
        catalogAuthPage.doLogout();
    }

    @Test
    public void deleteCarButtonCase(){
        deleteCarButtonCase(
                CarValue.builder()
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .bodyType(getRandomBodyTypes())
                        .build());
    }
    private void deleteCarButtonCase(CarValue carValue) {
        String urlDetailsPage;
        String titleDetailsPage;

        addCarPage.openAddCarPage().fillForm(carValue).clickSaveButton().clickCloseToastButton().clickFirstCarImage();
        urlDetailsPage = getCurrentUrl();
        titleDetailsPage = getCurrentTitle();

        detailsCarPage.clickDeleteButton().clickConfirmDeleteCarModal();
        assertTrue(catalogAuthPage.isToastDisplayed());
        assertEquals(TITLE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_DELETE));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_DELETE));
        catalogAuthPage.clickCloseToastButton();
        assertEquals(BASE_URL, getCurrentUrl());

        catalogAuthPage.clickFirstCarImage();
        assertNotEquals(urlDetailsPage, getCurrentUrl());
        assertNotEquals(titleDetailsPage, getCurrentTitle());
    }

    @Test
    public void editCarButtonCase(){
        editCarButtonCase(
                CarValue.builder()
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .bodyType(getRandomBodyTypes())
                        .build());
    }

    private void editCarButtonCase(CarValue carValue) {
        String urlEditPage;
        String titleEditPage;

        addCarPage.openAddCarPage().fillForm(carValue).clickSaveButton().clickCloseToastButton().clickFirstCarImage();
        urlEditPage = getEditUrlByDetailsUrl(getCurrentUrl());
        titleEditPage = getEditTitleByDetailsTitle(getCurrentTitle());
        detailsCarPage.clickEditButton();

        try {
            assertEquals(urlEditPage, getCurrentUrl());
            assertEquals(titleEditPage, getCurrentTitle());
        } finally {
            detailsCarPage.clickLogo().clickFirstCarImage().clickDeleteButton().clickConfirmDeleteCarModal();
        }
    }

    @Test
    public void deleteCarButtonThenModalCancelCase(){
        deleteCarButtonThenModalCancelCase(
                CarValue.builder()
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .bodyType(getRandomBodyTypes())
                        .build());
    }

    private void deleteCarButtonThenModalCancelCase(CarValue carValue) {
        String detailsUrl;
        String detailsTitle;

        addCarPage.openAddCarPage().fillForm(carValue).clickSaveButton().clickCloseToastButton().clickFirstCarImage();
        detailsUrl = getCurrentUrl();
        detailsTitle = getCurrentTitle();

        try {
            detailsCarPage.clickDeleteButton().clickCancelDeleteCarModal();
            assertEquals(detailsUrl, getCurrentUrl());
            assertEquals(detailsTitle, getCurrentTitle());

            detailsCarPage.clickLogo().clickFirstCarImage();
            assertEquals(detailsUrl, getCurrentUrl());
            assertEquals(detailsTitle, getCurrentTitle());
        } finally {
            detailsCarPage.clickDeleteButton().clickConfirmDeleteCarModal();
        }
    }
}
