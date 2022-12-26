package com.implemica.selenium.helpers;

public interface BaseTestValues {
    String BASE_URL = "http://localhost:4200/";
    String LOGIN_URL = "http://localhost:4200/login";
    String EDIT_URL_REG_FORMAT = "http://localhost:4200/edit/%s";
    String LOGIN_TITLE = "Login";
    String BASE_TITLE = "Car catalog";

    String ADD_CAR_URL = "http://localhost:4200/add";

    String ADD_CAR_TITLE = "Add car";

    String ADMIN_USERNAME = "admin";
    String ADMIN_PASSWORD = "admin";
    String TIP_REQUIRED = "Required!";
    String MESSAGE_SUCCESSFULLY_CAR_DELETE = "Car successful delete!";
    String TITLE_SUCCESSFULLY_CAR_DELETE = "Delete success";
    String ELECTRIC = "Electric";
    String XPATH_FOR_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]";
    String XPATH_FOR_TITLE_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//div[contains(@id, 'car-title')]";
    String XPATH_FOR_BODY_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//div[contains(@id, 'car-short-description')]";
    String XPATH_FORMAT_FOR_IMG_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//img[contains(@id,'car-image-%1$s-%2$s')]";
    String XPATH_FOR_DELETE_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//button[contains(@id,'car-delete-button')]";
    String XPATH_FOR_EDIT_ADDED_CAR = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//button[contains(@id,'car-edit-button')]";
    String XPATH_FOR_CONFIRM_DELETE_MODAL = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//button[contains(@id,'confirm-modal-button')]";
    String XPATH_FOR_CANCEL_DELETE_MODAL = "(//div[contains(@id,'car-card-%1$s-%2$s')])[last()]//button[contains(@id,'cancel-modal-button')]";
    String XPATH_FOR_IMG_ADDED_CAR = "(//div[contains(@id,'car-card')])[last()]//img[contains(@id,'car-image')]";
}
