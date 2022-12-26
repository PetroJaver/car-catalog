package com.implemica.selenium.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CatalogAuthPage extends BaseSeleniumPage {
    @FindBy(how = How.ID, using = "toast-container")
    public WebElement successToast;

    @FindBy(how = How.ID, using = "logo")
    public WebElement logo;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    public WebElement closeButtonSuccessToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    public WebElement titleSuccessToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    public WebElement messageSuccessToast;

    @FindBy(how = How.ID, using = "button-to-add-car-page")
    public WebElement addCarButton;

    @FindBy(how = How.ID, using = "logout-button")
    public WebElement logoutButtonHeader;

    @FindBy(how = How.ID, using = "logout-modal-button")
    public WebElement logoutButtonModal;

    @FindBy(how = How.ID, using = "user-avatar")
    public WebElement userAvatar;

    @FindBy(xpath = "//button[contains(@id,'car-edit-button')]")
    public WebElement editButtonFirstCarCard;

    @FindBy(xpath = "//button[contains(@id,'car-delete-button')]")
    public WebElement deleteButtonFirstCarCard;

    public CatalogAuthPage() {
        PageFactory.initElements(driver, this);
    }

    public AddCarPage clickAddCarButton() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(addCarButton));
        addCarButton.click();
        return new AddCarPage();
    }
}
