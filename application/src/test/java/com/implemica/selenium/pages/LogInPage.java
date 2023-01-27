package com.implemica.selenium.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;


import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;
import static com.implemica.selenium.helpers.BaseTestValues.LOGIN_URL;

public class LogInPage extends BaseSeleniumPage {
    @FindBy(id = "logo")
    private WebElement logo;
    @FindBy(id = "invalid-feedback-username")
    private WebElement usernameInvalidFeedBack;
    @FindBy(id = "invalid-feedback-password")
    private WebElement passwordInvalidFeedBack;
    @FindBy(id = "username")
    private WebElement inputUsernameField;
    @FindBy(id = "password")
    private WebElement inputPasswordField;
    @FindBy(id = "submit-button")
    private WebElement submitLoginButton;
    @FindBy(id = "cancel-button")
    private WebElement cancelLoginButton;

    @FindBy(id = "toast-container")
    private WebElement toast;

    @FindBy(className = "toast-close-button")
    private WebElement closeButtonToast;

    @FindBy(className = "toast-title")
    private WebElement titleToast;

    @FindBy(className = "toast-message")
    private WebElement messageToast;

    public LogInPage() {
        PageFactory.initElements(driver, this);
    }

    public LogInPage openLoginPage() {
        return new CatalogPage().clickLogo().clickLogIn();
    }

    public String getClassPassword() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputPasswordField, "class"));

        return inputPasswordField.getAttribute("class");
    }

    public String getMessageToast(String waitedValue){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(messageToast, waitedValue));

        return messageToast.getText();
    }

    public String getTitleToast(String waitedValue){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(titleToast, waitedValue));

        return titleToast.getText();
    }

    public String getPasswordTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(passwordInvalidFeedBack, waitedValue));

        return passwordInvalidFeedBack.getText();
    }

    public String getClassUsername() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputUsernameField, "class"));

        return inputUsernameField.getAttribute("class");
    }

    public String getUsernameTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(usernameInvalidFeedBack, waitedValue));

        return usernameInvalidFeedBack.getText();
    }

    public CatalogAuthPage doLogin(String username, String password) {
        return openLoginPage().fillForm(username, password).clickLoginSuccessful().clickCloseToastButton();
    }

    public LogInPage fillForm(String username, String password) {
        if (username != null) {
            clearUsername();
sendKeyByJse(            inputUsernameField, username);
        }

        if (password != null) {
            clearPassword();
sendKeyByJse(            inputPasswordField, password);
        }

        return this;
    }

    public CatalogAuthPage clickLoginSuccessful() {
        webDriverWait.until(ExpectedConditions.visibilityOf(submitLoginButton));
        clickByJse(submitLoginButton);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return new CatalogAuthPage();
    }

    public CatalogPage clickCancelFormButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(cancelLoginButton));
        clickByJse(cancelLoginButton);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return new CatalogPage();
    }

    public LogInPage clickLoginNotSuccessful() {
        webDriverWait.until(ExpectedConditions.visibilityOf(submitLoginButton));
        clickByJse(submitLoginButton);
        webDriverWait.until(ExpectedConditions.urlToBe(LOGIN_URL));

        return this;
    }

    public CatalogPage clickLogo(){
        webDriverWait.until(ExpectedConditions.visibilityOf(logo));
        clickByJse(logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return new CatalogPage();
    }

    public LogInPage clickCloseToastButton(){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.visibilityOf(closeButtonToast));
        clickByJse(closeButtonToast);
        webDriverWait.until(ExpectedConditions.invisibilityOf(toast));

        return this;
    }

    public Boolean isInputUsernameActive() {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsernameField));

        return getActiveElement().equals(inputUsernameField);
    }

    public Boolean isToastDisplayed(){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));

        return toast.isDisplayed();
    }

    public Boolean isSubmitLoginButtonEnabled(){
        webDriverWait.until(ExpectedConditions.visibilityOf(submitLoginButton));

        return submitLoginButton.isEnabled();
    }

    public LogInPage clearPassword() {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputPasswordField));
        inputPasswordField.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        inputPasswordField.sendKeys(Keys.chord(Keys.DELETE));
        webDriverWait.until(ExpectedConditions.attributeToBe(inputPasswordField, "value", ""));

        return this;
    }

    public LogInPage clearUsername() {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputUsernameField));
        inputUsernameField.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        inputUsernameField.sendKeys(Keys.chord(Keys.DELETE));
        webDriverWait.until(ExpectedConditions.attributeToBe(inputUsernameField, "value", ""));

        return this;
    }
}
