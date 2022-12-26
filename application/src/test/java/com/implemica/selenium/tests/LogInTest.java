package com.implemica.selenium.tests;

import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.CatalogPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.implemica.selenium.helpers.LogInTestValues.*;
import static org.junit.Assert.*;

public class LogInTest extends BaseSeleniumTest {
    private static LogInPage logInPage;

    public LogInTest() {
        if (logInPage == null)
            logInPage = new CatalogPage().clickLogIn();
    }

    @Test
    public void inputUsernameFocusedAfterOpenLoginPage() {
        LogInPage logInPageForTest = new CatalogPage().openCatalogPage().clickLogIn();

        assertEquals(driver.switchTo().activeElement(), logInPageForTest.inputUsernameField);
    }

    @Test
    public void clickCancelButton() {
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
        assertEquals(LOGIN_TITLE, driver.getTitle());

        logInPage.clickByJse(logInPage.cancelLoginButton);

        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        logInPage.openLoginPage();
    }

    @Test
    public void clickSubmitButton() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        CatalogPage catalogPage = new CatalogPage();
        driver.navigate().to(BASE_URL);
        catalogPage.clickLogIn();
        assertEquals(LOGIN_URL, driver.getCurrentUrl());
        assertEquals(LOGIN_TITLE, driver.getTitle());

        CatalogAuthPage catalogAuthPage = logInPage.doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

        assertTrue(catalogAuthPage.successToast.isDisplayed());
        assertEquals(MESSAGE_SUCCESSFULLY_LOGIN, catalogAuthPage.messageSuccessToast.getText());
        assertEquals(TITLE_SUCCESSFULLY_LOGIN, catalogAuthPage.titleSuccessToast.getText());
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
        webDriverWait.until(ExpectedConditions.invisibilityOf(catalogAuthPage.successToast));
        assertFalse(catalogAuthPage.successToast.isDisplayed());

        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        assertTrue(catalogAuthPage.addCarButton.isDisplayed());
        assertTrue(catalogAuthPage.logoutButtonHeader.isDisplayed());
        assertTrue(catalogAuthPage.userAvatar.isDisplayed());
        assertTrue(catalogAuthPage.editButtonFirstCarCard.isEnabled());
        assertTrue(catalogAuthPage.deleteButtonFirstCarCard.isEnabled());

        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonHeader);
        catalogAuthPage.clickByJse(catalogAuthPage.logoutButtonModal);
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource({
            "1, " + TIP_MIN_LENGTH_4,
            "12, " + TIP_MIN_LENGTH_4,
            "123, " + TIP_MIN_LENGTH_4,
            "a-, " + TIP_MIN_LENGTH_4,
            "a_, " + TIP_MIN_LENGTH_4,
            "123456789012345678901, " + TIP_MAX_LENGTH_20,
            "aaaaa-aaaaa-aaaaa-aaaaa, " + TIP_MAX_LENGTH_20,
            "AAAAA-AAAAA-AAAAA-AAAAA, " + TIP_MAX_LENGTH_20
    })
    public void inputPasswordInvalid(String input, String tipText) {
        logInPage.inputPasswordField.sendKeys(input);
        assertTrue(logInPage.inputPasswordField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(logInPage.passwordInvalidFeedBack.getText().equals(tipText));
        logInPage.inputPasswordField.clear();
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "1234", "user_user_user_use_r", "####", "&&&&", "1234567890", "op.jkl", "user-name@345yy",
            "USER _ NAME", "USER@Er *_NAME", "USER-  NAME"})
    public void inputPasswordTipRequired(String input) {
        logInPage.inputPasswordField.sendKeys(input);
        logInPage.inputPasswordField.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        logInPage.inputPasswordField.sendKeys(Keys.chord(Keys.DELETE));
        assertTrue(logInPage.inputPasswordField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(logInPage.passwordInvalidFeedBack.getText().equals(TIP_REQUIRED));
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "1234", "user_user_user_use_r", "####", "&&&&", "1234567890", "op.jkl", "user-name@345yy",
            "USER _ NAME", "USER@Er *_NAME", "USER-  NAME"})
    public void inputPasswordValid(String input) {
        logInPage.inputPasswordField.sendKeys(input);
        assertTrue(logInPage.inputPasswordField.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
        logInPage.inputPasswordField.clear();
    }

    @ParameterizedTest(name = "testCase => input = ''{0}''")
    @ValueSource(strings = {
            "username", "user_user_user_use_r", "username123", "USERNAME123", "1234567890", "user.name", "user-name",
            "USER.NAME", "USER_NAME", "USER-NAME"})
    public void inputUsernameValid(String input) {
        logInPage.inputUsernameField.sendKeys(input);
        assertTrue(logInPage.inputUsernameField.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
        logInPage.inputUsernameField.clear();
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource({
            "1, " + TIP_MIN_LENGTH_4,
            "12, " + TIP_MIN_LENGTH_4,
            "123, " + TIP_MIN_LENGTH_4,
            "a-, " + TIP_MIN_LENGTH_4,
            "a_, " + TIP_MIN_LENGTH_4,
            "123456789012345678901, " + TIP_MAX_LENGTH_20,
            "aaaaa-aaaaa-aaaaa-aaaaa, " + TIP_MAX_LENGTH_20,
            "AAAAA-AAAAA-AAAAA-AAAAA, " + TIP_MAX_LENGTH_20,
            "@@@@," + TIP_INCORRECT_USERNAME,
            "username..username," + TIP_INCORRECT_USERNAME,
            "username--username," + TIP_INCORRECT_USERNAME,
            "' username '," + TIP_INCORRECT_USERNAME,
            " .. ," + TIP_INCORRECT_USERNAME,
            " -- ," + TIP_INCORRECT_USERNAME,
            " __ ," + TIP_INCORRECT_USERNAME,
    })
    public void inputUsernameInvalid(String input, String tipText) {
        logInPage.inputUsernameField.sendKeys(input);
        assertTrue(logInPage.inputUsernameField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(logInPage.usernameInvalidFeedBack.getText().equals(tipText));
        logInPage.inputUsernameField.clear();
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "1234", "user_user_user_use_r", "####", "&&&&", "1234567890", "op.jkl", "user-name@345yy",
            "USER _ NAME", "USER@Er *_NAME", "USER-  NAME"})
    public void inputUsernameTipRequired(String input) {
        logInPage.inputUsernameField.sendKeys(input);
        logInPage.inputUsernameField.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        logInPage.inputUsernameField.sendKeys(Keys.chord(Keys.DELETE));
        assertTrue(logInPage.inputUsernameField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(logInPage.usernameInvalidFeedBack.getText().equals(TIP_REQUIRED));
    }

    @ParameterizedTest(name = "testCase {index} => username = ''{0}'', password = ''{1}'', focus = '{2}'")
    @CsvSource({
            "admin, admin, true",
            "admin, 123, false",
            "123, admin, false",
            "admin, admin123456789, true",
            "#, 123213123213213213123, false"
    })
    public void submitLoginButton(String username, String password, boolean isEnable) {
        logInPage.inputUsernameField.clear();
        logInPage.inputPasswordField.clear();
        logInPage.inputUsernameField.sendKeys(username);
        logInPage.inputPasswordField.sendKeys(password);

        assertEquals(isEnable, logInPage.submitLoginButton.isEnabled());
        driver.navigate().to(ADD_CAR_URL);
    }
}
