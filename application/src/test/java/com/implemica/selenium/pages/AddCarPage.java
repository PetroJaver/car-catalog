package com.implemica.selenium.pages;

import com.implemica.selenium.helpers.CarValue;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.AddCarTestValues.VALIDATION_CLASS_REG_INVALID;
import static com.implemica.selenium.helpers.BaseTestValues.ADD_CAR_URL;
import static com.implemica.selenium.helpers.BaseTestValues.BASE_URL;

public class AddCarPage extends BaseSeleniumPage {
    //region file
    @FindBy(how = How.ID, using = "image")
    public WebElement inputImage;

    @FindBy(how = How.ID, using = "car-image")
    public WebElement image;
    //endregion

    //region brand
    @FindBy(how = How.ID, using = "brand")
    public WebElement selectBrand;
    //endregion

    //region model
    @FindBy(how = How.ID, using = "model")
    public WebElement inputModel;

    @FindBy(how = How.ID, using = "invalid-feedback-model")
    public WebElement invalidTipForInputModel;
    //endregion

    //region bodyType
    @FindBy(how = How.ID, using = "body-type")
    public WebElement selectBodyType;
    //endregion

    //region toast
    @FindBy(xpath = "//div[@id='toast-container']")
    public WebElement wrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    public WebElement closeButtonWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    public WebElement titleWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    public WebElement messageWrongToast;
    //endregion

    //region transmissionButtons
    @FindBy(how = How.ID, using = "MANUAL")
    public WebElement transmissionButtonManual;


    @FindBy(how = How.ID, using = "AUTOMATIC")
    public WebElement transmissionButtonAutomatic;
    //endregion

    //region engineSize
    @FindBy(how = How.ID, using = "engine")
    public WebElement inputEngine;

    @FindBy(how = How.ID, using = "invalid-feedback-engine")
    public WebElement invalidTipForInputEngine;
    //endregion

    //region year
    @FindBy(how = How.ID, using = "year")
    public WebElement inputYear;

    @FindBy(how = How.ID, using = "invalid-feedback-year")
    public WebElement invalidTipForInputYear;
    //endregion

    //region shortDescription
    @FindBy(how = How.ID, using = "short-description")
    public WebElement textareaShortDescription;

    @FindBy(how = How.ID, using = "invalid-feedback-short-description")
    public WebElement invalidTipForTextareaShortDescription;
    //endregion

    //region description
    @FindBy(how = How.ID, using = "description")
    public WebElement textareaDescription;

    @FindBy(how = How.ID, using = "logout-button")
    public WebElement logoutButtonHeader;

    @FindBy(how = How.ID, using = "logout-modal-button")
    public WebElement logoutButtonModal;

    @FindBy(how = How.ID, using = "invalid-feedback-description")
    public WebElement invalidTipForTextareaDescription;
    //endregion

    //region option field
    @FindBy(how = How.ID, using = "option")
    public WebElement inputOptionField;

    @FindBy(how = How.ID, using = "invalid-feedback-option")
    public WebElement invalidTipForInputOptionField;

    @FindBy(how = How.ID, using = "add-option-button")
    public WebElement addOptionButton;
    //endregion

    //region resetForm
    @FindBy(how = How.ID, using = "cancel-form-button")
    public WebElement cancelButton;
    //endregion

    //region modal
    @FindBy(how = How.ID, using = "cancel-modal-button")
    public WebElement cancelModalButton;

    @FindBy(how = How.ID, using = "confirm-modal-button")
    public WebElement confirmModalButton;
    //endregion

    //region header element
    @FindBy(how = How.ID, using = "add-car-button")
    public WebElement addCarButton;
    //endregion

    public AddCarPage() {
        PageFactory.initElements(driver, this);
    }

    public AddCarPage openAddCarPage() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        catalogAuthPage.clickByJse(catalogAuthPage.logo);
        catalogAuthPage.clickByJse(catalogAuthPage.addCarButton);

        return this;
    }

    public LogInPage logOut(){
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logoutButtonHeader));
        clickByJse(logoutButtonHeader);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(logoutButtonModal));
        clickByJse(logoutButtonModal);

        return new LogInPage();
    }

    public AddCarPage addCar(CarValue carValue) {
        Select selectBrand = new Select(this.selectBrand);
        Select selectBodyType = new Select(this.selectBodyType);

        if (carValue.model != null) {
            inputModel.sendKeys(carValue.model);
            //enterDataByJse(inputModel, carValue.model);
        }

        if (carValue.transmissionType == MANUAL) {
            clickByJse(transmissionButtonManual);
        }

        if (carValue.transmissionType == AUTOMATIC) {
            clickByJse(transmissionButtonAutomatic);
        }

        if (carValue.engine != null) {
            inputEngine.clear();
            inputEngine.sendKeys(carValue.engine.toString());
        }

        if (carValue.year != null) {
            inputYear.clear();
            inputYear.sendKeys(carValue.year.toString());
        }

        if (carValue.bodyType != null) {
            selectBodyType.selectByVisibleText(carValue.bodyType.stringValue);
        }

        if (carValue.brand != null) {
            selectBrand.selectByVisibleText(carValue.brand.stringValue);
        }

        if (carValue.imageName != null) {
            inputImage.sendKeys(carValue.imageName);
        }

        if (carValue.shortDescription != null){
            textareaShortDescription.sendKeys(carValue.shortDescription);
            //enterDataByJse(textareaShortDescription, carValue.shortDescription);
        }

        scrollDown();

        if(carValue.description != null){
            textareaDescription.sendKeys(carValue.description);
            //enterDataByJse(textareaDescription, carValue.description);
        }

        if(carValue.options != null){
            for (int i = 0; i < carValue.options.size(); i++) {
                inputOptionField.sendKeys(carValue.options.get(i));
                //enterDataByJse(inputOptionField, carValue.options.get(i));

                if (this.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID)) {
                    break;
                }

                clickByJse(addOptionButton);
            }
        }

        return this;
    }

    public AddCarPage clickSaveButton() {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        clickByJse(addCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(catalogAuthPage.successToast));
        catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);

        return this;
    }
}
