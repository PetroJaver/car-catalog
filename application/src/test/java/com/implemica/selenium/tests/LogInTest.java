package com.implemica.selenium.tests;

import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.CatalogPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class LogInTest extends BaseSeleniumTest {
    private static LogInPage logInPage;

    private static CatalogPage catalogPage;

    private static CatalogAuthPage catalogAuthPage;

    @BeforeAll
    public static void beforeAll() {
        catalogPage = new CatalogPage();
        catalogAuthPage = new CatalogAuthPage();
        logInPage = new LogInPage().openLoginPage();
    }

    @Test
    public void inputUsernameFocusedAfterOpenLoginPageTest() {
        logInPage.openLoginPage();
        assertTrue(logInPage.isInputUsernameActive());
    }

    @Test
    public void clickCancelButtonTest() {
        logInPage.openLoginPage();
        assertEquals(LOGIN_URL, getCurrentUrl());
        assertEquals(LOGIN_TITLE, getCurrentTitle());

        logInPage.clickCancelFormButton();
        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, getCurrentTitle());
        logInPage.openLoginPage();
    }

    @Test
    public void clickSubmitButtonTest() {
        logInPage.openLoginPage();
        assertEquals(LOGIN_URL, getCurrentUrl());
        assertEquals(LOGIN_TITLE, getCurrentTitle());

        logInPage.fillForm(ADMIN_USERNAME, ADMIN_PASSWORD).clickLoginSuccessful();
        assertEquals(TITLE_SUCCESSFULLY_LOGIN, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_LOGIN));
        assertEquals(MESSAGE_SUCCESSFULLY_LOGIN, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_LOGIN));
        catalogAuthPage.clickCloseToastButton();

        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, getCurrentTitle());
        assertTrue(catalogAuthPage.isAddCarButtonDisplayed());
        assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        assertTrue(catalogAuthPage.isUserAvatarDisplayed());
        assertTrue(catalogAuthPage.isEditButtonFirstCarCardEnabled());
        assertTrue(catalogAuthPage.isDeleteButtonFirstCarCardEnabled());

        catalogAuthPage.clickLogoutButton();
        catalogAuthPage.clickConfirmLogoutModal();
    }

    @Test
    public void inputPasswordValidationTest() {
        logInPage.openLoginPage();

        //region valid values
        inputPasswordValidCase("1234");
        inputPasswordValidCase("user_user_user_use_r");
        inputPasswordValidCase("####");
        inputPasswordValidCase("&&&&");
        inputPasswordValidCase("1234567890");
        inputPasswordValidCase("op.jkl");
        inputPasswordValidCase("user-name@345yy");
        inputPasswordValidCase("USER _ NAME");
        inputPasswordValidCase("USER@Er *_NAME");
        inputPasswordValidCase("USER-  NAME");
        //endregion

        //region invalid values
        inputPasswordInvalidCase("1", TIP_MIN_LENGTH_4);
        inputPasswordInvalidCase("12", TIP_MIN_LENGTH_4);
        inputPasswordInvalidCase("123", TIP_MIN_LENGTH_4);
        inputPasswordInvalidCase("a-", TIP_MIN_LENGTH_4);
        inputPasswordInvalidCase("a_", TIP_MIN_LENGTH_4);

        inputPasswordInvalidCase("123456789012345678901", TIP_MAX_LENGTH_20);
        inputPasswordInvalidCase("aaaaa-aaaaa-aaaaa-aaaaa", TIP_MAX_LENGTH_20);
        inputPasswordInvalidCase("AAAAA-AAAAA-AAAAA-AAAAA", TIP_MAX_LENGTH_20);

        inputPasswordTipRequiredCase("1234");
        inputPasswordTipRequiredCase("user_user_user_use_r");
        inputPasswordTipRequiredCase("####");
        inputPasswordTipRequiredCase("&&&&");
        inputPasswordTipRequiredCase("1234567890");
        inputPasswordTipRequiredCase("op.jkl");
        inputPasswordTipRequiredCase("user-name@345yy");
        inputPasswordTipRequiredCase("USER _ NAME");
        inputPasswordTipRequiredCase("USER@Er *_NAME");
        inputPasswordTipRequiredCase("USER-  NAME");
        //endregion
    }

    private void inputPasswordValidCase(String password) {
        logInPage.fillForm(null, password);

        assertFalse(logInPage.getClassPassword().matches(VALIDATION_CLASS_REG_INVALID));
    }

    private void inputPasswordInvalidCase(String password, String expectedTextTip) {
        logInPage.fillForm(null, password);

        assertTrue(logInPage.getClassPassword().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, logInPage.getPasswordTipText(expectedTextTip));
    }

    private void inputPasswordTipRequiredCase(String password) {
        logInPage.fillForm(null, password).clearPassword();

        assertTrue(logInPage.getClassPassword().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(TIP_REQUIRED, logInPage.getPasswordTipText(TIP_REQUIRED));
    }

    @Test
    public void inputUsernameValidationTest() {
        logInPage.openLoginPage();

        //region valid values
        inputUsernameValidCase("username");
        inputUsernameValidCase("user_user_user_use_r");
        inputUsernameValidCase("username123");
        inputUsernameValidCase("USERNAME123");
        inputUsernameValidCase("1234567890");
        inputUsernameValidCase("user.name");
        inputUsernameValidCase("user-name");
        inputUsernameValidCase("USER.NAME");
        inputUsernameValidCase("USER_NAME");
        inputUsernameValidCase("USER-NAME");
        //endregion

        //region invalid values
        inputUsernameInvalidCase("1", TIP_MIN_LENGTH_4);
        inputUsernameInvalidCase("12", TIP_MIN_LENGTH_4);
        inputUsernameInvalidCase("123", TIP_MIN_LENGTH_4);
        inputUsernameInvalidCase("a-", TIP_MIN_LENGTH_4);
        inputUsernameInvalidCase("a_", TIP_MIN_LENGTH_4);

        inputUsernameInvalidCase("123456789012345678901", TIP_MAX_LENGTH_20);
        inputUsernameInvalidCase("aaaaa-aaaaa-aaaaa-aaaaa", TIP_MAX_LENGTH_20);
        inputUsernameInvalidCase("AAAAA-AAAAA-AAAAA-AAAAA", TIP_MAX_LENGTH_20);

        inputUsernameInvalidCase("@@@@", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase("username..username", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase("username--username", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase("' username '", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase(" .. ", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase(" -- ", TIP_INCORRECT_USERNAME);
        inputUsernameInvalidCase(" __ ", TIP_INCORRECT_USERNAME);

        inputUsernameTipRequiredCase("1234");
        inputUsernameTipRequiredCase("user_user_user_use_r");
        inputUsernameTipRequiredCase("####");
        inputUsernameTipRequiredCase("&&&&");
        inputUsernameTipRequiredCase("1234567890");
        inputUsernameTipRequiredCase("op.jkl");
        inputUsernameTipRequiredCase("user-name@345yy");
        inputUsernameTipRequiredCase("USER _ NAME");
        inputUsernameTipRequiredCase("USER@Er *_NAME");
        inputUsernameTipRequiredCase("USER-  NAME");
        //endregion
    }

    private void inputUsernameValidCase(String username) {
        logInPage.fillForm(username, null);

        assertFalse(logInPage.getClassUsername().matches(VALIDATION_CLASS_REG_INVALID));
    }

    private void inputUsernameInvalidCase(String username, String expectedTextTip) {
        logInPage.fillForm(username, null);

        assertTrue(logInPage.getClassUsername().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, logInPage.getUsernameTipText(expectedTextTip));
    }

    private void inputUsernameTipRequiredCase(String username) {
        logInPage.fillForm(username, null).clearUsername();

        assertTrue(logInPage.getClassUsername().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(TIP_REQUIRED, logInPage.getUsernameTipText(TIP_REQUIRED));
    }

    @Test
    public void submitLoginButtonEnableTest() {
        logInPage.openLoginPage();

        submitLoginButtonEnableCase("admin", "admin");
        submitLoginButtonEnableCase("petro", "admin");
        submitLoginButtonDisableCase("admin", "123");
        submitLoginButtonDisableCase("petro", "123");
        submitLoginButtonDisableCase("123", "admin");
        submitLoginButtonEnableCase("admin", "admin123456789");
        submitLoginButtonDisableCase("#", "123213123213213213123");
    }

    private void submitLoginButtonEnableCase(String username, String password) {
        logInPage.fillForm(username, password);

        assertTrue(logInPage.isSubmitLoginButtonEnabled());
    }

    private void submitLoginButtonDisableCase(String username, String password) {
        logInPage.fillForm(username, password);

        assertFalse(logInPage.isSubmitLoginButtonEnabled());
    }

    @Test
    public void loginTest() {
        //region successful login
        whenSuccessfulLoginCase("admin", "admin");
        whenSuccessfulLoginCase("petro", "petro");
        whenSuccessfulLoginCase("Petro", "petro");
        whenSuccessfulLoginCase("PETRO", "petro");
        whenSuccessfulLoginCase("Admin", "admin");
        whenSuccessfulLoginCase("ADMIN", "admin");
        whenSuccessfulLoginCase("AdMiN", "admin");
        whenSuccessfulLoginCase("PeTrO", "petro");
        //endregion

        //region not successful login
        whenNotSuccessfulLoginCase("Admin", "Admin");
        whenNotSuccessfulLoginCase("admin", "Admin");
        whenNotSuccessfulLoginCase("admin", "petro");
        whenNotSuccessfulLoginCase("Petro", "Petro");
        whenNotSuccessfulLoginCase("petro", "Petro");
        whenNotSuccessfulLoginCase("petro", "admin");
        whenNotSuccessfulLoginCase("adminn", "admin");
        whenNotSuccessfulLoginCase("petro", "petroo");

        whenNotSuccessfulLoginCase(getFirstNameMore4Chars(), dataFactory.getRandomChars(4));
        whenNotSuccessfulLoginCase(getFirstNameMore4Chars(), getFirstNameMore4Chars());
        whenNotSuccessfulLoginCase(dataFactory.getRandomChars(4), dataFactory.getLastName());
        whenNotSuccessfulLoginCase(dataFactory.getRandomChars(4), dataFactory.getRandomChars(4));
        whenNotSuccessfulLoginCase(dataFactory.getRandomChars(20), dataFactory.getRandomChars(4));
        whenNotSuccessfulLoginCase(dataFactory.getLastName(), dataFactory.getRandomChars(20));
        whenNotSuccessfulLoginCase(getFirstNameMore4Chars(), dataFactory.getRandomWord(4, 20));
        whenNotSuccessfulLoginCase(getFirstNameMore4Chars(), dataFactory.getRandomChars(4, 20));
        whenNotSuccessfulLoginCase(dataFactory.getRandomChars(4, 20), dataFactory.getRandomChars(4, 20));
        //endregion
    }

    private void whenSuccessfulLoginCase(String username, String password) {
        catalogPage.clickLogo().clickLogIn();

        assertEquals(LOGIN_URL, getCurrentUrl());
        assertEquals(LOGIN_TITLE, getCurrentTitle());

        logInPage.fillForm(username, password).clickLoginSuccessful();

        assertTrue(catalogAuthPage.isToastDisplayed());
        assertEquals(MESSAGE_SUCCESSFULLY_LOGIN, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_LOGIN));
        assertEquals(TITLE_SUCCESSFULLY_LOGIN, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_LOGIN));
        catalogAuthPage.clickCloseToastButton();

        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, getCurrentTitle());

        assertTrue(catalogAuthPage.isAddCarButtonDisplayed());
        assertTrue(catalogAuthPage.isButtonLogoutDisplayed());
        assertTrue(catalogAuthPage.isUserAvatarDisplayed());
        assertTrue(catalogAuthPage.isEditButtonFirstCarCardEnabled());
        assertTrue(catalogAuthPage.isDeleteButtonFirstCarCardEnabled());

        catalogAuthPage.clickLogoutButton().clickConfirmLogoutModal().clickLogIn();
    }

    private void whenNotSuccessfulLoginCase(String username, String password) {
        catalogPage.clickLogo().clickLogIn();

        assertEquals(LOGIN_URL, getCurrentUrl());
        assertEquals(LOGIN_TITLE, getCurrentTitle());

        logInPage.fillForm(username, password).clickLoginNotSuccessful();

        assertTrue(logInPage.isToastDisplayed());
        assertEquals(TITLE_NOT_SUCCESSFULLY_LOGIN, logInPage.getTitleToast(TITLE_NOT_SUCCESSFULLY_LOGIN));
        assertEquals(MESSAGE_NOT_SUCCESSFULLY_LOGIN, logInPage.getMessageToast(MESSAGE_NOT_SUCCESSFULLY_LOGIN));
        logInPage.clickCloseToastButton();

        assertEquals(LOGIN_URL, getCurrentUrl());
    }
}
