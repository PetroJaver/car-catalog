package com.implemica.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CatalogAuthPage extends BaseSeleniumPage{
    @FindBy(xpath = "//div[@id='toast-container']")
    private WebElement successToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    private WebElement closeButtonSuccessToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    private WebElement titleSuccessToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    private WebElement messageSuccessToast;

    public CatalogAuthPage(){
        PageFactory.initElements(driver,this);
    }

    public void clickCloseButtonSuccessToast() {
        closeButtonSuccessToast.click();
    }

    public WebElement getSuccessToast() {
        return successToast;
    }

    public String getTitleSuccessToast(){
        return titleSuccessToast.getText();
    }

    public String getMessageSuccessToast(){
        return messageSuccessToast.getText();
    }
}
