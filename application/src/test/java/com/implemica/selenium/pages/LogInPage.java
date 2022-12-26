package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


import static com.implemica.selenium.helpers.BaseTestValues.LOGIN_URL;

public class LogInPage extends BaseSeleniumPage {
    @FindBy(id = "invalid-feedback-username")
    public WebElement usernameInvalidFeedBack;
    @FindBy(id = "invalid-feedback-password")
    public WebElement passwordInvalidFeedBack;
    @FindBy(id = "username")
    public WebElement inputUsernameField;
    @FindBy(id = "password")
    public WebElement inputPasswordField;
    @FindBy(id = "submit-button")
    public WebElement submitLoginButton;
    @FindBy(id = "cancel-button")
    public WebElement cancelLoginButton;

    public LogInPage() {
        PageFactory.initElements(driver, this);
    }

    public LogInPage openLoginPage(){
        driver.get(LOGIN_URL);
        return this;
    }

    public CatalogAuthPage doLogin(String username, String password) {
        inputUsernameField.sendKeys(username);
        inputPasswordField.sendKeys(password);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(submitLoginButton));

        clickByJse(submitLoginButton);

        return new CatalogAuthPage();
    }
}
