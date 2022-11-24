package com.implemica.application.util.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Header extends BaseSeleniumPage{
    @FindBy(xpath = "//nav//button[@routerlink = 'login']")
    private WebElement logInButton;

    @FindBy(xpath = "//nav//button[@routerlink = 'add']")
    private WebElement addCarButton;

    public Header(){
        PageFactory.initElements(driver,this);
    }

    public LogInPage clickLogIn(){
        logInButton.click();
        return new LogInPage();
    }

    public AddCarPage clickAddCarButton(){
        addCarButton.click();
        return new AddCarPage();
    }

    public WebElement getLogInButton() {
        return logInButton;
    }

    public WebElement getAddCarButton() {
        return addCarButton;
    }
}
