package com.implemica.selenium.tests;

import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.*;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;


public class CatalogTest extends BaseSeleniumTest {

    private CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
    private CatalogPage catalogPage = new CatalogPage();
    private LogInPage logInPage = new LogInPage();
    private EditCarPage editCarPage = new EditCarPage();

    private DetailsCarPage detailsCarPage = new DetailsCarPage();
    private AddCarPage addCarPage = new AddCarPage();

    @Test
    public void loginButtonTest() {
        catalogPage.clickLogo();
        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());

        catalogPage.clickLogIn();
        assertEquals(LOGIN_URL, getCurrentUrl());
        assertEquals(LOGIN_TITLE, driver.getTitle());
    }

    @Test
    public void addCarButtonTest() {
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());

        catalogAuthPage.clickAddCarButton();
        assertEquals(ADD_CAR_URL, getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, driver.getTitle());

        addCarPage.clickLogo().doLogout();
    }

    @Test
    public void logoutButtonTest() {
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

        Assert.assertEquals(BASE_URL, getCurrentUrl());
        Assert.assertEquals(BASE_TITLE, driver.getTitle());
        Assert.assertTrue(catalogAuthPage.isAddCarButtonDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isUserAvatarDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isDeleteButtonFirstCarCardEnabled());
        Assert.assertTrue(catalogAuthPage.isEditButtonFirstCarCardEnabled());

        catalogAuthPage.clickLogoutButton()
                .clickConfirmLogoutModal();

        Assert.assertEquals(LOGIN_URL, getCurrentUrl());
        Assert.assertEquals(LOGIN_TITLE, driver.getTitle());

        logInPage.clickLogo();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isAddCarButtonDisplayed());
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isUserAvatarDisplayed());
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isDeleteButtonFirstCarCardEnabled());
        Assert.assertThrows(NoSuchElementException.class, () -> catalogAuthPage.isEditButtonFirstCarCardEnabled());
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void logoutButtonCancelTest() {
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

        Assert.assertEquals(BASE_URL, getCurrentUrl());
        Assert.assertEquals(BASE_TITLE, driver.getTitle());
        Assert.assertTrue(catalogAuthPage.isAddCarButtonDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isUserAvatarDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isDeleteButtonFirstCarCardEnabled());
        Assert.assertTrue(catalogAuthPage.isEditButtonFirstCarCardEnabled());

        catalogAuthPage.clickLogoutButton().clickCancelLogoutModal();

        Assert.assertEquals(BASE_URL, getCurrentUrl());
        Assert.assertEquals(BASE_TITLE, driver.getTitle());
        Assert.assertTrue(catalogAuthPage.isAddCarButtonDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isUserAvatarDisplayed());
        Assert.assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        Assert.assertTrue(catalogAuthPage.isDeleteButtonFirstCarCardEnabled());
        Assert.assertTrue(catalogAuthPage.isEditButtonFirstCarCardEnabled());

        catalogAuthPage.doLogout();
    }

    @Test
    public void deleteCarButtonTest() {
        //region delete car then confirm modal
        deleteCarButtonCase(STANDARD_CAR);
        //endregion

        //region delete car then cancel modal
        deleteCarButtonThenModalCancelCase(STANDARD_CAR);
        //endregion
    }

    private void deleteCarButtonCase(CarValue carValue) {
        String detailsURL;
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .clickAddCarButton().fillForm(carValue)
                .clickSaveButton()
                .clickCloseToastButton()
                .clickFirstCarImage();

        detailsURL = getCurrentUrl();
        detailsCarPage.clickLogo()
                .clickFirstCarDeleteButton()
                .clickFirstCarDeleteModalConfirm();

        assertEquals(TITLE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_DELETE));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_DELETE, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_DELETE));
        assertEquals(BASE_URL, getCurrentUrl());
        catalogAuthPage.clickCloseToastButton();

        catalogAuthPage.clickFirstCarImage();
        assertNotEquals(detailsURL, getCurrentUrl());

        detailsCarPage.clickLogo().doLogout();
    }

    @Test
    public void editCarButtonTest() {
        editCarButtonCase(STANDARD_CAR);
    }

    private void editCarButtonCase(CarValue carValue) {
        String detailsUrl;
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .clickAddCarButton()
                .fillForm(carValue)
                .clickSaveButton()
                .clickCloseToastButton()
                .clickFirstCarEditButton();

        detailsUrl = getCurrentUrl();

        detailsCarPage.clickLogo()
                .clickFirstCarEditButton();
        try {
            assertEquals(getEditUrlByDetailsUrl(detailsUrl), getCurrentUrl());
        } finally {
            editCarPage.clickLogo()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton()
                    .doLogout();
        }
    }

    private void deleteCarButtonThenModalCancelCase(CarValue carValue) {
        String detailsUrl;
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .clickAddCarButton()
                .fillForm(carValue)
                .clickSaveButton()
                .clickCloseToastButton()
                .clickFirstCarImage();

        detailsUrl = getCurrentUrl();

        detailsCarPage.clickLogo()
                .clickFirstCarDeleteButton()
                .clickFirstCarDeleteModalCancel()
                .clickFirstCarImage();

        try {
            assertEquals(detailsUrl, getCurrentUrl());
        } finally {
            detailsCarPage.clickLogo()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton()
                    .doLogout();
        }
    }

    @Test
    public void clickLogoFromLoginPageCase() {
        catalogPage.clickLogo().clickLogIn();
        assertEquals(LOGIN_TITLE, getCurrentTitle());
        assertEquals(LOGIN_URL, getCurrentUrl());
        logInPage.clickLogo();
        assertEquals(BASE_TITLE, getCurrentTitle());
        assertEquals(BASE_URL, getCurrentUrl());
    }

    @Test
    public void clickLogoFromDetailsPageCase() {
        catalogPage.clickLogo().clickFirstCarImage();
        assertTrue(getCurrentTitle().matches(DETAILS_TITLE_MATCHES));
        assertTrue(getCurrentUrl().matches(DETAILS_URL_MATCHES));
        detailsCarPage.clickLogo();
        assertEquals(BASE_TITLE, getCurrentTitle());
        assertEquals(BASE_URL, getCurrentUrl());
    }

    @Test
    public void clickLogoFromAddPageCase() {
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .clickAddCarButton();

        assertEquals(ADD_CAR_TITLE,getCurrentTitle());
        assertEquals(ADD_CAR_URL,getCurrentUrl());
        addCarPage.clickLogo();
        assertEquals(BASE_TITLE, getCurrentTitle());
        assertEquals(BASE_URL, getCurrentUrl());
        editCarPage.clickLogo().doLogout();
    }

    @Test
    public void clickLogoFromEditPageCase() {
        catalogPage.clickLogo()
                .clickLogIn()
                .doLogin(ADMIN_USERNAME, ADMIN_PASSWORD)
                .clickFirstCarEditButton();

        assertTrue(getCurrentTitle().matches(EDIT_TITLE_MATCHES));
        assertTrue(getCurrentUrl().matches(EDIT_URL_MATCHES));
        editCarPage.clickLogo();
        assertEquals(BASE_TITLE, getCurrentTitle());
        assertEquals(BASE_URL, getCurrentUrl());
        editCarPage.clickLogo().doLogout();
    }
}
