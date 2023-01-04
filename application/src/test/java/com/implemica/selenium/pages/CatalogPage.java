package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;

public class CatalogPage extends BaseSeleniumPage{
    @FindBy(how = How.ID, using = "login-button")
    private WebElement logInButton;

    @FindBy(how = How.ID, using = "logo")
    private WebElement logo;

    public CatalogPage(){
        PageFactory.initElements(driver,this);
    }

    public CatalogPage openCatalogPage(){
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logo));
        clickByJse(logo);

        return this;
    }

    public LogInPage clickLogIn(){
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logInButton));
        clickByJse(logInButton);

        return new LogInPage();
    }
}
