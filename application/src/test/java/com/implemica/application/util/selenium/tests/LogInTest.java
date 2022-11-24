package com.implemica.application.util.selenium.tests;

import static com.implemica.application.util.selenium.helpers.LogInTestValues.*;
import com.implemica.application.util.selenium.pages.CatalogAuthPage;
import com.implemica.application.util.selenium.pages.Header;
import com.implemica.application.util.selenium.pages.LogInPage;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LogInTest extends BaseSeleniumTest{

    @Test
    public void testLogIn() throws Exception {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        Header header = new Header();
        header.openMainPage();

        LogInPage logInPage = header.clickLogIn();
        assertFalse(logInPage.getLogInButton().isEnabled());
        assertTrue(driver.switchTo().activeElement().equals(logInPage.elementOnFocus()));
        assertFalse(logInPage.getInValidTipForInputPassword().isDisplayed());
        assertFalse(logInPage.getInValidTipForInputUsername().isDisplayed());
        assertFalse(logInPage.getValidTipForInputPassword().isDisplayed());
        assertFalse(logInPage.getValidTipForInputUsername().isDisplayed());

        logInPage.invalidFillFields();
        assertTrue(logInPage.noValidTipForInputUsername().equals(INVALID_USERNAME_TIP));
        assertTrue(logInPage.noValidTipForInputPassword().equals(INVALID_MIN_PASSWORD_TIP));
        assertFalse(logInPage.getLogInButton().isEnabled());

        logInPage.emptyFillFields();
        assertTrue(logInPage.noValidTipForInputUsername().equals(REQUIRED_TIP));
        assertTrue(logInPage.noValidTipForInputPassword().equals(REQUIRED_TIP));
        assertFalse(logInPage.getLogInButton().isEnabled());

        logInPage.validFillFieldsWithNotRealAdmin();
        assertTrue(logInPage.validTipForInputUsername().equals(VALID_INPUT));
        assertTrue(logInPage.validTipForInputPassword().equals(VALID_INPUT));
        assertTrue(logInPage.getLogInButton().isEnabled());
        logInPage.clickLogInButton();
        assertTrue(logInPage.getWrongToast().isDisplayed());
        assertTrue(logInPage.getTitleWrongToast().equals(WRONG_MESSAGE));
        assertTrue(logInPage.getMessageWrongToast().equals(INVALID_PASSWORD_OR_USERNAME));
        logInPage.clickCloseButtonWrongToast();

        logInPage.getResetButton().click();
        assertFalse(logInPage.getLogInButton().isEnabled());
        assertFalse(logInPage.getInValidTipForInputPassword().isDisplayed());
        assertFalse(logInPage.getInValidTipForInputUsername().isDisplayed());
        assertFalse(logInPage.getValidTipForInputPassword().isDisplayed());
        assertFalse(logInPage.getValidTipForInputUsername().isDisplayed());

        logInPage.validFillFieldsWithRealAdmin();
        assertTrue(logInPage.validTipForInputUsername().equals(VALID_INPUT));
        assertTrue(logInPage.validTipForInputPassword().equals(VALID_INPUT));

        CatalogAuthPage catalogAuthPage = logInPage.clickLogInButton();
        webDriverWait.until(driver->catalogAuthPage.getSuccessToast().isDisplayed());
        assertTrue(catalogAuthPage.getTitleSuccessToast().equals(SUCCESS_MESSAGE));
        assertTrue(catalogAuthPage.getMessageSuccessToast().equals(MESSAGE_SUCCESSFUL_LOG_IN));
        catalogAuthPage.clickCloseButtonSuccessToast();
    }

}
