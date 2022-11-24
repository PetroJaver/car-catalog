package com.implemica.application.util.selenium.pages;

import static com.implemica.application.util.readproperties.ConfigProvider.*;

import static org.apache.commons.lang3.RandomStringUtils.*;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LogInPage extends BaseSeleniumPage {
    @FindBy(id = "username")
    private WebElement inputUsername;

    @FindBy(id = "password")
    private WebElement inputPassword;

    @FindBy(xpath = "//button[@type = 'submit']")
    private WebElement logInButton;

    @FindBy(xpath = "//button[@type = 'reset']")
    private WebElement resetButton;

    @FindBy(xpath = "//input[@id = 'username']/../../div[@class = 'valid-feedback text-start']")
    private WebElement validTipForInputUsername;

    @FindBy(xpath = "//input[@id = 'password']/../../div[@class = 'valid-feedback text-start']")
    private WebElement validTipForInputPassword;
    @FindBy(xpath = "//input[@id = 'username']/../../div[@class = 'invalid-feedback text-start']")
    private WebElement inValidTipForInputUsername;

    @FindBy(xpath = "//input[@id = 'password']/../../div[@class = 'invalid-feedback text-start']")
    private WebElement inValidTipForInputPassword;

    @FindBy(xpath = "//div[@id='toast-container']")
    private WebElement wrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    private WebElement closeButtonWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    private WebElement titleWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    private WebElement messageWrongToast;

    public LogInPage() {
        PageFactory.initElements(driver, this);
    }

    public LogInPage validFillFieldsWithRealAdmin() {
        inputUsername.sendKeys(ADMIN_USERNAME);
        inputPassword.sendKeys(ADMIN_PASSWORD);
        return this;
    }

    public LogInPage validFillFieldsWithNotRealAdmin() {
        inputUsername.sendKeys(ADMIN_USERNAME);
        inputPassword.sendKeys(random(8, true, true));
        return this;
    }

    public LogInPage invalidFillFields() {
        inputUsername.sendKeys(random(10, true, true));
        inputPassword.sendKeys(random(3, false, true));
        return this;
    }

    public LogInPage emptyFillFields() {
        inputUsername.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        inputPassword.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }


    public CatalogAuthPage clickLogInButton() {
        logInButton.click();
        return new CatalogAuthPage();
    }

    public void clickCloseButtonWrongToast() {
        closeButtonWrongToast.click();
    }

    public WebElement elementOnFocus() {
        return inputUsername;
    }

    public WebElement getLogInButton() {
        return logInButton;
    }

    public WebElement getResetButton() {
        return resetButton;
    }

    public String validTipForInputUsername() {
        return validTipForInputUsername.getText();
    }

    public String validTipForInputPassword() {
        return validTipForInputPassword.getText();
    }

    public String noValidTipForInputUsername() {
        return inValidTipForInputUsername.getText();
    }

    public String noValidTipForInputPassword() {
        return inValidTipForInputPassword.getText();
    }

    public WebElement getValidTipForInputUsername() {
        return validTipForInputUsername;
    }

    public WebElement getValidTipForInputPassword() {
        return validTipForInputPassword;
    }

    public WebElement getInValidTipForInputUsername() {
        return inValidTipForInputUsername;
    }

    public WebElement getInValidTipForInputPassword() {
        return inValidTipForInputPassword;
    }

    public WebElement getWrongToast() {
        return wrongToast;
    }

    public String getTitleWrongToast(){
        return titleWrongToast.getText();
    }

    public String getMessageWrongToast(){
        return messageWrongToast.getText();
    }
}
