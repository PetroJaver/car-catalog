package com.implemica.selenium.helpers;

public interface LogInTestValues extends BaseTestValues{
    String VALIDATION_CLASS_REG_VALID = ".*is-valid.*";
    String VALIDATION_CLASS_REG_INVALID = ".*is-invalid.*";
    String TIP_MIN_LENGTH_4 = "Min length 4 symbols!";
    String TIP_MAX_LENGTH_20 = "Max length 20 symbols!";
    String TIP_INCORRECT_USERNAME = "Incorrect username!";

    String MESSAGE_SUCCESSFULLY_LOGIN = "You successfully logged in!";
    String TITLE_SUCCESSFULLY_LOGIN = "Success";
}
