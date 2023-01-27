package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static com.implemica.selenium.helpers.BaseTestValues.TITLE_PART_DETAILS;

public class CatalogPage extends BaseSeleniumPage{
    @FindBy(id = "login-button")
    private WebElement logInButton;

    @FindBy(id = "logo")
    private WebElement logo;

    @FindBy(id = "car-card-0")
    private WebElement firstCarCard;

    @FindBy(id = "car-image-0")
    private WebElement firstCarImage;

    @FindBy(id = "car-title-0")
    private WebElement firstCarTitle;

    @FindBy(id = "car-short-description-0")
    private WebElement firstCarShortDescription;

    public CatalogPage(){
        PageFactory.initElements(driver,this);
    }

    public String getTitleFirstCar(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(firstCarTitle, waitedValue));
        return firstCarTitle.getText();
    }

    public String getShortDescriptionFirstCar(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(firstCarShortDescription, waitedValue));
        return firstCarShortDescription.getText();
    }

    public String getImageSrcFirstCar(){
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarImage));

        return firstCarImage.getAttribute("src");
    }

    public DetailsCarPage clickFirstCarImage(){
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarImage));
        clickByJse(firstCarImage);
        webDriverWait.until(ExpectedConditions.urlMatches(DETAILS_URL_MATCHES));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_PART_DETAILS));

        return new DetailsCarPage();
    }

    public CatalogPage clickLogo(){
        webDriverWait.until(ExpectedConditions.visibilityOf(logo));
        clickByJse(logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return this;
    }

    public LogInPage clickLogIn(){
        webDriverWait.until(ExpectedConditions.visibilityOf(logInButton));
        clickByJse(logInButton);
        webDriverWait.until(ExpectedConditions.urlToBe(LOGIN_URL));

        return new LogInPage();
    }
}
