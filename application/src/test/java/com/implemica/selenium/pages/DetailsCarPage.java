package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DetailsCarPage extends BaseSeleniumPage{
    @FindBy(how = How.ID, using = "brand-model")
    public WebElement brandModelText;

    @FindBy(how = How.ID, using = "image")
    public WebElement image;

    @FindBy(how = How.ID, using = "body-type")
    public WebElement bodyTypeText;

    @FindBy(how = How.ID, using = "transmission")
    public WebElement transmissionTypeText;

    @FindBy(how = How.ID, using = "engine-electric")
    public WebElement engineElectricText;

    @FindBy(how = How.ID, using = "engine")
    public  WebElement engineText;

    @FindBy(how = How.ID, using = "year")
    public WebElement yearText;

    @FindBy(how = How.ID, using = "short-description")
    public WebElement shortDescriptionText;

    @FindBy(how = How.ID, using = "description")
    public WebElement descriptionText;

    @FindBy(how = How.ID, using = "delete-car-button")
    public WebElement deleteCarButton;

    @FindBy(how = How.ID, using = "update-car-button")
    public WebElement updateCarButton;

    @FindBy(how = How.ID, using = "delete-car-modal-button")
    public WebElement deleteCarModalButton;

    @FindBy(how = How.ID, using = "cancel-modal-button")
    public WebElement closeModalButton;


    public DetailsCarPage(){
        PageFactory.initElements(driver,this);
    }

    public DetailsCarPage deleteCar(){
        scrollDown();
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteCarButton));
        clickByJse(deleteCarButton);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteCarModalButton));
        clickByJse(deleteCarModalButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(catalogAuthPage.successToast));
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
        webDriverWait.until(ExpectedConditions.invisibilityOf(catalogAuthPage.successToast));
        return this;
    }
}
