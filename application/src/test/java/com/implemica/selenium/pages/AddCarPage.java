package com.implemica.selenium.pages;

import com.implemica.selenium.helpers.CarValue;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.BaseTestValues.*;

public class AddCarPage extends BaseSeleniumPage {
    @FindBy(id = "logo")
    private WebElement logo;
    //region file
    @FindBy(id = "image")
    private WebElement inputImage;

    @FindBy(id = "car-image")
    private WebElement image;
    //endregion

    //region brand
    @FindBy(id = "brand")
    private WebElement selectBrand;
    //endregion

    //region model
    @FindBy(id = "model")
    private WebElement inputModel;

    @FindBy(id = "invalid-feedback-model")
    private WebElement invalidTipForInputModel;
    //endregion

    //region bodyType
    @FindBy(id = "body-type")
    private WebElement selectBodyType;
    //endregion

    //region toast
    @FindBy(id = "toast-container")
    private WebElement toast;

    @FindBy(className = "toast-close-button")
    private WebElement closeButtonToast;

    @FindBy(className = "toast-title")
    private WebElement titleToast;

    @FindBy(className = "toast-message")
    private WebElement messageToast;
    //endregion

    //region transmissionButtons
    @FindBy(id = "MANUAL")
    private WebElement transmissionButtonManual;


    @FindBy(id = "AUTOMATIC")
    private WebElement transmissionButtonAutomatic;
    //endregion

    //region engineSize
    @FindBy(id = "engine")
    private WebElement inputEngine;

    @FindBy(id = "invalid-feedback-engine")
    private WebElement invalidTipForInputEngine;
    //endregion

    //region year
    @FindBy(id = "year")
    private WebElement inputYear;

    @FindBy(id = "invalid-feedback-year")
    private WebElement invalidTipForInputYear;
    //endregion

    //region shortDescription
    @FindBy(id = "short-description")
    private WebElement textareaShortDescription;

    @FindBy(id = "invalid-feedback-short-description")
    private WebElement invalidTipForTextareaShortDescription;
    //endregion

    //region description
    @FindBy(id = "description")
    private WebElement textareaDescription;

    @FindBy(id = "logout-button")
    private WebElement logoutButtonHeader;

    @FindBy(id = "logout-modal-button")
    private WebElement logoutButtonModal;

    @FindBy(id = "invalid-feedback-description")
    private WebElement invalidTipForTextareaDescription;
    //endregion

    //region option field
    @FindBy(id = "option")
    private WebElement inputOptionField;

    @FindBy(id = "invalid-feedback-option")
    private WebElement invalidTipForInputOptionField;

    @FindBy(id = "add-option-button")
    private WebElement addOptionButton;
    //endregion

    //region resetForm
    @FindBy(id = "cancel-form-button")
    private WebElement cancelButton;
    //endregion

    //region modal
    @FindBy(id = "cancel-modal-button")
    private WebElement cancelModalButton;

    @FindBy(id = "confirm-modal-button")
    private WebElement confirmModalButton;

    @FindBy(id = "cancel-form-modal")
    private WebElement cancelFormModal;
    //endregion

    //region header element
    @FindBy(id = "add-car-button")
    private WebElement saveCarButton;
    //endregion

    public AddCarPage() {
        PageFactory.initElements(driver, this);
    }

    public AddCarPage openAddCarPage() {
        return new CatalogAuthPage()
                .clickLogo()
                .clickAddCarButton();
    }

    public String getClassBrand() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(selectBrand, "class"));

        return selectBrand.getAttribute("class");
    }

    public String getValueBrand(String waitedValue) {
        webDriverWait.until(ExpectedConditions.attributeToBe(selectBrand, "value", waitedValue));

        return selectBrand.getAttribute("value");
    }

    public String getModelTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(invalidTipForInputModel, waitedValue));

        return invalidTipForInputModel.getText();
    }

    public String getClassModel() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputModel, "class"));

        return inputModel.getAttribute("class");
    }

    public String getYearTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(invalidTipForInputYear, waitedValue));

        return invalidTipForInputYear.getText();
    }

    public String getClassYear() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputYear, "class"));

        return inputYear.getAttribute("class");
    }

    public String getEngineTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(invalidTipForInputEngine, waitedValue));

        return invalidTipForInputEngine.getText();
    }

    public String getClassEngine() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputEngine, "class"));

        return inputEngine.getAttribute("class");
    }

    public String getClassBodyType() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(selectBodyType, "class"));

        return selectBodyType.getAttribute("class");
    }

    public String getValueBodyType(String waitedValue) {
        webDriverWait.until(ExpectedConditions.attributeToBe(selectBodyType, "value", waitedValue));

        return selectBodyType.getAttribute("value");
    }

    public WebElement getDeleteOptionButton(String option) {
        return driver.findElement(By.id(String.format(FORMAT_DELETE_OPTION_BUTTON_ID, option)));
    }

    public WebElement getOption(String option) {
        return driver.findElement(By.id(String.format(FORMAT_OPTION_ID, option)));
    }

    public String getOptionText(String option) {
        WebElement optionElement = getOption(option);
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(optionElement, option));

        return optionElement.getText();
    }

    public String getOptionTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(invalidTipForInputOptionField, waitedValue));

        return invalidTipForInputOptionField.getText();
    }

    public String getClassOptionField() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(inputOptionField, "class"));

        return inputOptionField.getAttribute("class");
    }

    public String getDescriptionTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(invalidTipForTextareaDescription, waitedValue));

        return invalidTipForTextareaDescription.getText();
    }

    public String getClassDescription() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(textareaDescription, "class"));

        return textareaDescription.getAttribute("class");
    }

    public String getShortDescriptionTipText(String waitedValue) {
        webDriverWait.until(ExpectedConditions
                .textToBePresentInElement(invalidTipForTextareaShortDescription, waitedValue));

        return invalidTipForTextareaShortDescription.getText();
    }

    public String getClassShortDescription() {
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(textareaShortDescription, "class"));

        return textareaShortDescription.getAttribute("class");
    }

    public List<String> getAllOptions(){
        return driver.findElements(By.className("option-chips"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public String getSrcImage() {
        webDriverWait.until(ExpectedConditions.visibilityOf(image));
        webDriverWait.until(ExpectedConditions.attributeToBeNotEmpty(image, "src"));

        return image.getAttribute("src");
    }

    public String getMessageToast(String waitedValue) {
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(messageToast, waitedValue));

        return messageToast.getText();
    }

    public String getTitleToast(String waitedValue) {
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(titleToast, waitedValue));

        return titleToast.getText();
    }

    public CatalogAuthPage clickSaveButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(saveCarButton));
        clickByJse(saveCarButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));

        return new CatalogAuthPage();
    }

    public AddCarPage clickCloseToastButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.visibilityOf(closeButtonToast));
        clickByJse(closeButtonToast);
        webDriverWait.until(ExpectedConditions.invisibilityOf(toast));

        return this;
    }

    public AddCarPage clickCancelFormButton() {
        webDriverWait.until(ExpectedConditions.visibilityOf(cancelButton));
        clickByJse(cancelButton);
       // webDriverWait.until(ExpectedConditions.visibilityOf(cancelFormModal));

        return this;
    }

    public CatalogAuthPage clickConfirmModal() {
        //webDriverWait.until(ExpectedConditions.visibilityOf(confirmModalButton));
        clickByJse(confirmModalButton);
        //webDriverWait.until(ExpectedConditions.invisibilityOf(cancelFormModal));
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));

        return new CatalogAuthPage();
    }

    public AddCarPage clickCancelModal() {
        //webDriverWait.until(ExpectedConditions.visibilityOf(cancelModalButton));
        clickByJse(cancelModalButton);
        //webDriverWait.until(ExpectedConditions.invisibilityOf(cancelFormModal));

        return this;
    }

    public AddCarPage clickTransmissionManual() {
        webDriverWait.until(ExpectedConditions.visibilityOf(transmissionButtonManual));
        clickByJse(transmissionButtonManual);
        webDriverWait.until(ExpectedConditions.elementToBeSelected(transmissionButtonManual));

        return this;
    }

    public AddCarPage clickTransmissionAutomatic() {
        webDriverWait.until(ExpectedConditions.visibilityOf(transmissionButtonAutomatic));
        clickByJse(transmissionButtonAutomatic);
        webDriverWait.until(ExpectedConditions.elementToBeSelected(transmissionButtonAutomatic));

        return this;
    }

    public AddCarPage clickDeleteOption(String option) {
        webDriverWait.until(ExpectedConditions.visibilityOf(getDeleteOptionButton(option)));
        clickByJse(getDeleteOptionButton(option));

        return this;
    }

    public CatalogAuthPage clickLogo() {
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(logo));
        clickByJse(logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_BASE_PAGE));

        return new CatalogAuthPage();
    }

    public AddCarPage inputOption(String option) {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputOptionField));
        inputOptionField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        inputOptionField.sendKeys(Keys.DELETE);
        sendKeyByJse(inputOptionField, option);

        return this;
    }

    public Boolean isSaveCarButtonEnable() {
        webDriverWait.until(ExpectedConditions.visibilityOf(saveCarButton));

        return saveCarButton.isEnabled();
    }

    public Boolean isAddOptionButtonEnable() {
        webDriverWait.until(ExpectedConditions.visibilityOf(addOptionButton));

        return addOptionButton.isEnabled();
    }

    public Boolean isTransmissionManualButtonSelected() {
        return transmissionButtonManual.isSelected();
    }

    public Boolean isTransmissionButtonAutomaticSelected() {
        return transmissionButtonAutomatic.isSelected();
    }

    public AddCarPage deleteAllOptions(){
        driver.findElements(By.className("option-chips"))
                .stream()
                .map(WebElement::getText)
                .forEach(this :: clickDeleteOption);

        return this;
    }

    public Boolean isSelectBrandActive() {
        webDriverWait.until(ExpectedConditions.visibilityOf(selectBrand));

        return getActiveElement().equals(selectBrand);
    }

    public Boolean isToastDisplayed() {
        return toast.isDisplayed();
    }

    public AddCarPage clearModel() {
        webDriverWait.until(ExpectedConditions.visibilityOf(inputModel));
        inputModel.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        inputModel.sendKeys(Keys.chord(Keys.DELETE));
        webDriverWait.until(ExpectedConditions.attributeToBe(inputModel, "value", ""));

        return this;
    }

    public AddCarPage fillForm(CarValue carValue) {
        Select selectBrand = new Select(this.selectBrand);
        Select selectBodyType = new Select(this.selectBodyType);

        if (carValue.model != null) {
            inputModel.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            inputModel.sendKeys(Keys.DELETE);
            sendKeyByJse(inputModel, carValue.model);
        }

        if (carValue.transmissionType == MANUAL) {
            webDriverWait.until(ExpectedConditions.visibilityOf(transmissionButtonManual));
            clickByJse(transmissionButtonManual);
        }

        if (carValue.transmissionType == AUTOMATIC) {
            webDriverWait.until(ExpectedConditions.visibilityOf(transmissionButtonAutomatic));
            clickByJse(transmissionButtonAutomatic);
        }

        if (carValue.engine != null) {
            inputEngine.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            inputEngine.sendKeys(Keys.DELETE);
            sendKeyByJse(inputEngine, carValue.engine);
        }

        if (carValue.year != null) {
            inputYear.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            inputYear.sendKeys(Keys.DELETE);
            sendKeyByJse(inputYear, carValue.year);
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

        if (carValue.shortDescription != null) {
            textareaShortDescription.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            textareaShortDescription.sendKeys(Keys.DELETE);
            sendKeyByJse(textareaShortDescription, carValue.shortDescription);
        }

        if (carValue.description != null) {
            textareaDescription.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            textareaDescription.sendKeys(Keys.DELETE);
            sendKeyByJse(textareaDescription, carValue.description);
        }

        if (carValue.options != null) {
            for (int i = 0; i < carValue.options.size(); i++) {
                inputOption(carValue.options.get(i));

                if (this.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID)) {
                    break;
                }

                webDriverWait.until(ExpectedConditions.visibilityOf(addOptionButton));
                clickByJse(addOptionButton);
            }
        }

        return this;
    }
}
