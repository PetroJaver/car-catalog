package com.implemica.selenium.helpers;

public interface EditCarTestValues extends BaseTestValues{
    String XPATH_LAST_DELETE_CAR_BUTTON = "(//button[contains(@id,'car-delete-button')])[last()]";
    String XPATH_LAST_MODAL_CONFIRM_DELETE_CAR_BUTTON = "(//button[contains(@id,'confirm-modal-button')])[last()]";
    String XPATH_LAST_EDIT_CAR_BUTTON = "(//button[contains(@id,'car-edit-button')])[last()]";
    String XPATH_LAST_IMG_CAR = "(//button[contains(@id,'car-edit-button')])[last()]";
    String TITLE_SUCCESSFULLY_CAR_EDIT = "Success";
    String MESSAGE_SUCCESSFULLY_CAR_EDIT = "Car successful update!";
}
