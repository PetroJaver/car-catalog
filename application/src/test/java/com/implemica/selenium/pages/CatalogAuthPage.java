package com.implemica.selenium.pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.implemica.selenium.helpers.BaseTestValues.*;

public class CatalogAuthPage extends BaseSeleniumPage {
    @FindBy(id = "logo")
    private WebElement logo;

    @FindBy(id = "toast-container")
    private WebElement toast;

    @FindBy(className = "toast-close-button")
    private WebElement closeButtonToast;

    @FindBy(className = "toast-title")
    private WebElement titleToast;

    @FindBy(className = "toast-message")
    private WebElement messageToast;

    @FindBy(id = "car-card-0")
    private WebElement firstCarCard;

    @FindBy(id = "car-image-0")
    private WebElement firstCarImage;

    @FindBy(id = "car-title-0")
    private WebElement firstCarTitle;

    @FindBy(id = "car-short-description-0")
    private WebElement firstCarShortDescription;

    @FindBy(id = "car-delete-button-0")
    private WebElement firstCarDeleteButton;

    @FindBy(id = "car-edit-button-0")
    private WebElement firstCarEditButton;

    @FindBy(id = "confirm-modal-button-0")
    private WebElement firstCarDeleteModalConfirmButton;

    @FindBy(id = "cancel-modal-button-0")
    private WebElement firstCarDeleteModalCancelButton;

    @FindBy(id = "modal-0")
    private WebElement firstCarModal;

    @FindBy(id = "button-to-add-car-page")
    private WebElement addCarButton;

    @FindBy(id = "logout-button")
    private WebElement buttonLogout;

    //region logout modal
    @FindBy(id = "confirm-modal-logout")
    private WebElement confirmModalLogout;

    @FindBy(id = "cancel-modal-logout")
    private WebElement cancelModalLogout;

    @FindBy(id = "modal-logout")
    private WebElement modalLogout;
    //endregion

    @FindBy(id = "user-avatar")
    private WebElement userAvatar;

    @FindBy(id = "car-edit-button-0")
    private WebElement editButtonFirstCarCard;

    @FindBy(id = "car-delete-button-0")
    private WebElement deleteButtonFirstCarCard;

    public CatalogAuthPage() {
        PageFactory.initElements(driver, this);
    }

    public CatalogPage doLogout(){
        return clickLogo()
                .clickLogoutButton()
                .clickConfirmLogoutModal();
    }

    public String getMessageToast(String waitedValue){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(messageToast, waitedValue));

        return messageToast.getText();
    }

    public String getTitleToast(String waitedValue){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(titleToast, waitedValue));

        return titleToast.getText();
    }

    public String getTitleFirstCar(String waitedValue) {
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(firstCarTitle, waitedValue));
        return firstCarTitle.getText();
    }

    public String getShortDescriptionFirstCar(String waitedValue) {
        String shortDescription = null;

        if (waitedValue != null) {
            webDriverWait.until(ExpectedConditions.textToBePresentInElement(firstCarShortDescription, waitedValue));
            shortDescription = firstCarShortDescription.getText();
        }

        return shortDescription;
    }

    public String getImageSrcFirstCar(){
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarImage));

        return firstCarImage.getAttribute("src");
    }

    public CatalogAuthPage clickLogo(){
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(logo));
        clickByJse(logo);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_BASE_PAGE));

        return this;
    }

    public AddCarPage clickAddCarButton() {
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(addCarButton));
        clickByJse(addCarButton);
        webDriverWait.until(ExpectedConditions.urlToBe(ADD_CAR_URL));

        return new AddCarPage();
    }

    public CatalogAuthPage clickCloseToastButton(){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));
        webDriverWait.until(ExpectedConditions.visibilityOf(closeButtonToast));
        clickByJse(closeButtonToast);
        webDriverWait.until(ExpectedConditions.invisibilityOf(toast));

        return this;
    }

    public DetailsCarPage clickFirstCarImage(){
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarImage));
        clickByJse(firstCarImage);
        webDriverWait.until(ExpectedConditions.urlMatches(DETAILS_URL_MATCHES));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_PART_DETAILS));

        return new DetailsCarPage();
    }

    public CatalogAuthPage clickFirstCarDeleteButton(){
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarCard));
        clickByJse(firstCarDeleteButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarModal));

        return this;
    }

    public EditCarPage clickFirstCarEditButton(){
        scrollUp();
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarCard));
        clickByJse(firstCarEditButton);
        webDriverWait.until(ExpectedConditions.urlMatches(EDIT_URL_MATCHES));
        webDriverWait.until(ExpectedConditions.titleContains(TITLE_PART_EDIT));

        return new EditCarPage();
    }

    public CatalogAuthPage clickFirstCarDeleteModalConfirm(){
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarDeleteModalConfirmButton));
        clickByJse(firstCarDeleteModalConfirmButton);
        webDriverWait.until(ExpectedConditions.invisibilityOf(firstCarModal));
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));

        return this;
    }

    public CatalogAuthPage clickFirstCarDeleteModalCancel(){
        webDriverWait.until(ExpectedConditions.visibilityOf(firstCarDeleteModalCancelButton));
        clickByJse(firstCarDeleteModalCancelButton);
        webDriverWait.until(ExpectedConditions.invisibilityOf(firstCarModal));

        return this;
    }

    public CatalogAuthPage clickLogoutButton(){
        webDriverWait.until(ExpectedConditions.visibilityOf(buttonLogout));
        clickByJse(buttonLogout);
        //webDriverWait.until(ExpectedConditions.visibilityOf(modalLogout));

        return this;
    }

    public CatalogPage clickConfirmLogoutModal(){
        webDriverWait.until(ExpectedConditions.visibilityOf(modalLogout));
        webDriverWait.until(ExpectedConditions.visibilityOf(confirmModalLogout));
        clickByJse(confirmModalLogout);
        webDriverWait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        webDriverWait.until(ExpectedConditions.invisibilityOf(confirmModalLogout));
        webDriverWait.until(ExpectedConditions.invisibilityOf(modalLogout));

        return new CatalogPage();
    }

    public CatalogAuthPage clickCancelLogoutModal(){
        webDriverWait.until(ExpectedConditions.visibilityOf(cancelModalLogout));
        clickByJse(cancelModalLogout);
        webDriverWait.until(ExpectedConditions.invisibilityOf(modalLogout));

        return new CatalogAuthPage();
    }

    public Boolean isToastDisplayed(){
        webDriverWait.until(ExpectedConditions.visibilityOf(toast));

        return toast.isDisplayed();
    }

    public Boolean isAddCarButtonDisplayed() {
        return addCarButton.isDisplayed();
    }

    public Boolean isButtonLogoutDisplayed() {
        return addCarButton.isDisplayed();
    }

    public Boolean isUserAvatarDisplayed() {
        return userAvatar.isDisplayed();
    }

    public Boolean isEditButtonFirstCarCardEnabled() {
        return editButtonFirstCarCard.isEnabled();
    }

    public Boolean isDeleteButtonFirstCarCardEnabled() {
        return deleteButtonFirstCarCard.isEnabled();
    }
}
