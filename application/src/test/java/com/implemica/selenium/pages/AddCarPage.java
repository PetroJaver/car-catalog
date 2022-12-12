package com.implemica.selenium.pages;

import static com.implemica.selenium.helpers.AddCarTestValues.*;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;

import static org.apache.commons.lang3.RandomStringUtils.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class AddCarPage extends BaseSeleniumPage {
    //region file
    @FindBy(xpath = "//input[@id = 'image']")
    private WebElement inputFile;

    @FindBy(xpath = "//input[@id = 'image']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForInputFile;

    @FindBy(xpath = "//input[@id = 'image']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForInputFile;

    @FindBy(xpath = "//img[@style='max-width: 100%;']")
    private WebElement image;
    //endregion

    //region brand
    @FindBy(xpath = "//select[@id = 'brand']")
    private WebElement selectBrand;

    @FindBy(xpath = "//select[@id = 'brand']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForSelectBrand;

    @FindBy(xpath = "//select[@id = 'brand']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForSelectBrand;
    //endregion

    //region model
    @FindBy(xpath = "//input[@id = 'model']")
    private WebElement inputModel;

    @FindBy(xpath = "//input[@id = 'model']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForInputModel;

    @FindBy(xpath = "//input[@id = 'model']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForInputModel;
    //endregion

    //region bodyType
    @FindBy(xpath = "//select[@id = 'bodyType']")
    private WebElement selectBodyType;

    @FindBy(xpath = "//select[@id = 'bodyType']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForSelectBodyType;

    @FindBy(xpath = "//select[@id = 'bodyType']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForSelectBodyType;
    //endregion

    //region toast
    @FindBy(xpath = "//div[@id='toast-container']")
    private WebElement wrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//button[contains(@class,'toast-close-button')]")
    private WebElement closeButtonWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-title')]")
    private WebElement titleWrongToast;

    @FindBy(xpath = "//div[@id='toast-container']//div//div[contains(@class,'toast-message')]")
    private WebElement messageWrongToast;
    //endregion

    //region transmissionButtons
    @FindBy(xpath = "//input[@id='MANUAL']")
    private WebElement transmissionButtonManual;

    @FindBy(xpath = "//input[@id='AUTOMATIC']")
    private WebElement transmissionButtonAutomatic;
    //endregion

    //region engineSize
    @FindBy(xpath = "//input[@id = 'engine']")
    private WebElement inputEngine;

    @FindBy(xpath = "//input[@id = 'engine']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForInputEngine;

    @FindBy(xpath = "//input[@id = 'engine']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForInputEngine;
    //endregion

    //region year
    @FindBy(xpath = "//input[@id = 'year']")
    private WebElement inputYear;

    @FindBy(xpath = "//input[@id = 'year']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForInputYear;

    @FindBy(xpath = "//input[@id = 'year']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForInputYear;
    //endregion

    //region shortDescription
    @FindBy(xpath = "//textarea[@id = 'shortDescription']")
    private WebElement textareaShortDescription;

    @FindBy(xpath = "//textarea[@id = 'shortDescription']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForTextareaShortDescription;

    @FindBy(xpath = "//textarea[@id = 'shortDescription']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForTextareaShortDescription;
    //endregion

    //region description
    @FindBy(xpath = "//textarea[@id = 'description']")
    private WebElement textareaDescription;

    @FindBy(xpath = "//textarea[@id = 'description']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForTextareaDescription;

    @FindBy(xpath = "//textarea[@id = 'description']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForTextareaDescription;
    //endregion

    //region common option
    @FindBy(xpath = "//input[@id = '0option']")
    private WebElement commonOptionCheckButton;

    @FindBy(xpath = "//div[@class = 'text-center']//button[contains(@class,'btn-success')]")
    private WebElement addOptionButton;
    //endregion

    //region optional
    @FindBy(xpath = "//input[@id = 'option']")
    private WebElement inputOption;

    @FindBy(xpath = "//input[@id = 'option']//..//div[@class = 'valid-feedback']")
    private WebElement validTipForInputOption;

    @FindBy(xpath = "//input[@id = 'option']//..//div[contains(@class,'invalid-feedback')]")
    private WebElement invalidTipForInputOption;
    //endregion

    //region resetForm
    @FindBy(xpath = "//button[contains(@class,'btn btn-danger col-auto me-3')]")
    private WebElement resetButton;
    //endregion

    //region addCarButton
    @FindBy(xpath = "//button[@type = 'submit']")
    private WebElement addedButton;
    //endregion

    public AddCarPage() {
        PageFactory.initElements(driver, this);
    }

    //region fileInput
    public AddCarPage noValidFillFileInput() {
        inputFile.sendKeys(INVALID_FILE_PATH);
        return this;
    }

    public AddCarPage validFillFileInput() {
        inputFile.sendKeys(VALID_FILE_PATH);
        return this;
    }


    public String validTipForForInputFile() {
        return validTipForInputFile.getText();
    }

    public String invalidTipForInputFile() {
        return invalidTipForInputFile.getText();
    }

    public String getImageAttributeSrc() {
        return image.getAttribute("src");
    }
    //endregion

    //region toast
    public String getTitleWrongToast() {
        return titleWrongToast.getText();
    }

    public WebElement getWrongToast() {
        return wrongToast;
    }

    public AddCarPage closeButtonWrongToast() {
        closeButtonWrongToast.click();
        return this;
    }

    public String getMessageWrongToast() {
        return messageWrongToast.getText();
    }
    //endregion

    //region brand
    public AddCarPage noSelectBrand() {
        Select selectBrand = new Select(this.selectBrand);
        selectBrand.selectByIndex(0);

        return this;
    }

    public AddCarPage selectBrand() {
        Select selectBrand = new Select(this.selectBrand);
        int index = (int)(Math.random() * CarBrand.values().length);
        index = index == 0 ? index + 1 : index;
        selectBrand.selectByIndex(index);

        return this;
    }

    public AddCarPage selectBrand(int index) {
        Select selectBrand = new Select(this.selectBrand);
        selectBrand.selectByIndex(index);

        return this;
    }

    public String validTipForForSelectBrand() {
        return validTipForSelectBrand.getText();
    }

    public String invalidTipForSelectBrand() {
        return invalidTipForSelectBrand.getText();
    }
    //endregion

    //region model

    public AddCarPage validFillModel() {
        inputModel.sendKeys(randomAlphabetic((int)(Math.random()*37) + 2));
        return this;
    }

    public AddCarPage validFillModel(String model) {
        inputModel.sendKeys(model);
        return this;
    }

    public AddCarPage invalidMinLengthFillModel() {
        inputModel.sendKeys(randomAlphabetic(1));
        return this;
    }

    public AddCarPage invalidMaxLengthFillModel() {
        inputModel.sendKeys(randomAlphabetic((int)(Math.random()*41) + 40));
        return this;
    }

    public AddCarPage invalidPatternFillModel() {
        inputModel.sendKeys(random(8, 32, 47, false, false));
        return this;
    }

    public AddCarPage emptyFillModel() {
        inputModel.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForInputModel() {
        return validTipForInputModel.getText();
    }

    public String invalidTipForInputModel() {
        return invalidTipForInputModel.getText();
    }
    //endregion

    //region bodyType
    public AddCarPage noSelectBodyType() {
        Select selectBodyType = new Select(this.selectBodyType);
        selectBodyType.selectByIndex(0);

        return this;
    }

    public AddCarPage selectBodyType() {
        Select selectBodyType = new Select(this.selectBodyType);
        int index = (int)(Math.random() * CarBodyType.values().length);
        index = index == 0 ? index + 1 : index;
        selectBodyType.selectByIndex(index);

        return this;
    }

    public AddCarPage selectBodyType(int index) {
        Select selectBodyType = new Select(this.selectBodyType);
        selectBodyType.selectByIndex(index);

        return this;
    }

    public String validTipForForSelectBodyType() {
        return validTipForSelectBodyType.getText();
    }

    public String invalidTipForSelectBodyType() {
        return invalidTipForSelectBodyType.getText();
    }
    //endregion

    //region transmissionButtons
    public AddCarPage clickManualButton(){
        transmissionButtonManual.click();
        return this;
    }

    public AddCarPage clickAutomaticButton(){
        transmissionButtonAutomatic.click();
        return this;
    }

    public WebElement getTransmissionButtonManual() {
        return transmissionButtonManual;
    }

    public WebElement getTransmissionButtonAutomatic() {
        return transmissionButtonAutomatic;
    }

    //endregion

    //region engine
    public AddCarPage validFillEngine() {
        String engine = String.format("%.1f",(Math.random() * 9.9d) + 0.1d);
        inputEngine.sendKeys(engine);
        return this;
    }

    public AddCarPage validFillEngine(String engine) {
        inputEngine.sendKeys(engine);
        return this;
    }

    public AddCarPage invalidMinLengthFillEngine() {
        String engine = String.format("%.1f",(Math.random() * -9.9d) + -0.1d);
        inputEngine.sendKeys(engine);
        return this;
    }

    public AddCarPage invalidMaxLengthFillEngine() {
        String engine = String.format("%.1f",(Math.random() * 10d) + 10.1d);
        inputEngine.sendKeys(engine);
        return this;
    }

    public AddCarPage emptyFillEngine() {
        inputEngine.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForInputEngine() {
        return validTipForInputEngine.getText();
    }

    public String invalidTipForInputEngine() {
        return invalidTipForInputEngine.getText();
    }
    //endregion

    // region year
    public AddCarPage validFillYear() {
        String year = String.format("%d",(int)(Math.random() * 220) + 1880);
        inputYear.sendKeys(year);
        return this;
    }

    public AddCarPage validFillYear(String year) {
        inputYear.sendKeys(year);
        return this;
    }

    public AddCarPage invalidMinLengthFillYear() {
        String year = String.format("%d",(int)(Math.random() * 1880));
        inputYear.sendKeys(year);
        return this;
    }

    public AddCarPage invalidMaxLengthFillYear() {
        String year = String.format("%d",(int)(Math.random() * 1000) + 2100);
        inputYear.sendKeys(year);
        return this;
    }

    public AddCarPage emptyFillYear() {
        inputYear.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForInputYear() {
        return validTipForInputYear.getText();
    }

    public String invalidTipForInputYear() {
        return invalidTipForInputYear.getText();
    }
    //endregion

    //region shortDescription
    public AddCarPage validFillShortDescription() {
        textareaShortDescription.sendKeys(randomAlphabetic((int)(Math.random()*100) + 50));
        return this;
    }

    public AddCarPage validFillShortDescription(String shortDescription) {
        textareaShortDescription.sendKeys(shortDescription);
        return this;
    }

    public AddCarPage invalidMinLengthFillShortDescription() {
        textareaShortDescription.sendKeys(randomAlphabetic(((int)(Math.random()*23)+1)));
        return this;
    }

    public AddCarPage invalidMaxLengthFillShortDescription() {
        textareaShortDescription.sendKeys(randomAlphabetic((int)(Math.random()*100) + 151));
        return this;
    }

    public AddCarPage emptyFillShortDescription() {
        textareaShortDescription.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForTextareaShortDescription() {
        return validTipForTextareaShortDescription.getText();
    }

    public String invalidTipForTextareaShortDescription() {
        return invalidTipForTextareaShortDescription.getText();
    }
    //endregion

    //region description
    public AddCarPage validFillDescription() {
        textareaDescription.sendKeys(randomAlphabetic(((int)(Math.random()*500) + 50)));
        return this;
    }

    public AddCarPage validFillDescription(String description) {
        textareaDescription.sendKeys(description);
        return this;
    }

    public AddCarPage invalidMinLengthFillDescription() {
        textareaDescription.sendKeys(randomAlphabetic(((int)(Math.random()*48)+1)));
        return this;
    }

    public AddCarPage invalidMaxLengthFillDescription() {
        textareaDescription.sendKeys(randomAlphabetic(((int)(Math.random()*500) + 5001)));
        return this;
    }

    public AddCarPage emptyFillDescription() {
        textareaDescription.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForTextareaDescription() {
        return validTipForTextareaDescription.getText();
    }

    public String invalidTipForTextareaDescription() {
        return invalidTipForTextareaDescription.getText();
    }
    //endregion

    //region common option button
    public AddCarPage clickCommonOptionCheckButton(){
        commonOptionCheckButton.click();
        return this;
    }

    public AddCarPage clickCommonOptionCheckButton(String indexOption){
        String xpath = String.format("//input[@id = '%doption']",indexOption);
        WebElement optionCheckButton = driver.findElement(By.xpath(xpath));
        optionCheckButton.click();
        return this;
    }

    public WebElement getCommonOptionCheckButton() {
        return commonOptionCheckButton;
    }
    //endregion

    //region optional

    public AddCarPage validFillOption() {
        inputOption.sendKeys(randomAlphabetic((int)(Math.random()*23) + 2));
        return this;
    }

    public AddCarPage validFillOption(String option) {
        inputOption.sendKeys(option);
        return this;
    }

    public AddCarPage invalidMinLengthFillOption() {
        inputOption.sendKeys(randomAlphabetic(1));
        return this;
    }

    public AddCarPage invalidMaxLengthFillOption() {
        inputOption.sendKeys(randomAlphabetic((int)(Math.random()*25) + 26));
        return this;
    }

    public AddCarPage invalidPatternFillOption() {
        inputOption.sendKeys(random(8, 32, 47, false, false));
        return this;
    }

    public AddCarPage emptyFillOption() {
        inputOption.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        return this;
    }

    public String validTipForInputOption() {
        return validTipForInputOption.getText();
    }

    public String invalidTipForInputOption() {
        return invalidTipForInputOption.getText();
    }

    public WebElement getValidTipForInputOption() {
        return validTipForInputOption;
    }

    public WebElement getInvalidTipForInputOption() {
        return invalidTipForInputOption;
    }

    public AddCarPage clickAddOptionButton(){
        addOptionButton.click();
        return this;
    }

    public WebElement getAddOptionButton() {
        return addOptionButton;
    }

    public AddCarPage clickDropButtonAddedOption(String option){
        String xpath = String.format("//label[contains(text(),'%s')]//button",option);
        driver.findElement(By.xpath(xpath)).click();
        return this;
    }

    public String getTextAddedOption(String option){
        String xpath = String.format("//label[contains(text(),'%s')]",option);
        return driver.findElement(By.xpath(xpath)).getText();
    }

    public WebElement getAddedOption(String option){
        String xpath = String.format("//label[contains(text(),'%s')]",option);
        return driver.findElement(By.xpath(xpath));
    }
    public AddCarPage clickResetButton(){
        resetButton.click();
        return this;
    }

    public CatalogAuthPage clickAddCarButton(){
        addedButton.click();
        return new CatalogAuthPage();
    }

    public WebElement getAddedButton() {
        return addedButton;
    }

    //endregion

    public boolean isAllFieldNotTouch(){

        return !(invalidTipForSelectBrand.isDisplayed()||validTipForSelectBrand.isDisplayed()||
                invalidTipForInputModel.isDisplayed()||validTipForInputModel.isDisplayed()||
                invalidTipForSelectBodyType.isDisplayed()||validTipForSelectBodyType.isDisplayed()||
                invalidTipForInputYear.isDisplayed()||validTipForInputYear.isDisplayed()||
                transmissionButtonManual.isSelected()||
                validTipForInputEngine.isDisplayed()||invalidTipForInputEngine.isDisplayed()||
                validTipForTextareaShortDescription.isDisplayed()||invalidTipForTextareaShortDescription.isDisplayed()||
                validTipForTextareaDescription.isDisplayed()||invalidTipForTextareaDescription.isDisplayed()||
                validTipForInputOption.isDisplayed()||invalidTipForInputOption.isDisplayed());
    }
}
