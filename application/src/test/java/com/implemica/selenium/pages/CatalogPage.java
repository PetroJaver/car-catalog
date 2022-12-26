package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;

public class CatalogPage extends BaseSeleniumPage{
    @FindBy(how = How.ID, using = "login-button")
    private WebElement logInButton;

    public CatalogPage(){
        PageFactory.initElements(driver,this);
    }

    public CatalogPage openCatalogPage(){
        driver.get(BASE_URL);
        return this;
    }

    public LogInPage clickLogIn(){
        clickByJse(logInButton);
        return new LogInPage();
    }
}
