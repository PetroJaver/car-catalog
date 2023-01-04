package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.pages.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.implemica.selenium.helpers.AddCarTestValues.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class EditCarTestValidation extends BaseSeleniumTest {
    private static EditCarPage editCarPage;
    private static WebDriverWait webDriverWait;

    @BeforeAll
    public static void beforeAll() {
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        new LogInPage().openLoginPage().doLogin(ADMIN_USERNAME,ADMIN_PASSWORD);
        editCarPage = new EditCarPage();
        editCarPage.clickByJse(new CatalogAuthPage().editButtonFirstCarCard);
    }

    @AfterAll
    public static void afterAll() {
        editCarPage.logOut();
    }

    @ParameterizedTest(name = "testcase {index} => select = ''{0}''")
    @EnumSource(CarBrand.class)
    public void selectBrand(CarBrand brand) {
        Select selectBrand = new Select(editCarPage.selectBrand);
        selectBrand.selectByVisibleText(brand.stringValue);

        assertTrue(editCarPage.selectBrand.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "a, " + TIP_MIN_LENGTH_2,
            "aaaaa aaaaa aaaaa aaaa aaaaa aaaaa aaaaaa, " + TIP_MAX_LENGTH_40,
            "%, " + TIP_INCORRECT_MODEL,
            "aaaaa aaaaa aaaaa aaaa aaaaa aaaaa aaaaa%, " + TIP_INCORRECT_MODEL,
            "prado@, " + TIP_INCORRECT_MODEL
    })
    public void inputModelInvalid(String input, String tipText) {
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(input);

        assertTrue(editCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForInputModel.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "aa",
            "AAaaa bBbbb-cCcCc ddd'dd AAaaa bBbbb-cCc",
            "RAV 4",
            "Prado"
    })
    public void inputModelValid(String input) {
        editCarPage.inputModel.clear();
        editCarPage.inputModel.sendKeys(input);

        assertTrue(editCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "1234", "user_user_user_use_r", "####", "&&&&", "1234567890", "op.jkl", "user-name@345yy",
            "USER _ NAME", "USER@Er *_NAME", "USER-  NAME"})
    public void inputModelTipRequired(String input) {
        editCarPage.inputModel.sendKeys(input);
        editCarPage.inputModel.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editCarPage.inputModel.sendKeys(Keys.chord(Keys.DELETE));
        assertTrue(editCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(editCarPage.invalidTipForInputModel.getText().equals(TIP_REQUIRED));
    }

    @ParameterizedTest(name = "testcase {index} => select = ''{0}''")
    @EnumSource(CarBodyType.class)
    public void selectBodyType(CarBodyType bodyType) {
        Select selectBodyType = new Select(editCarPage.selectBodyType);
        selectBodyType.selectByVisibleText(bodyType.stringValue);
        assertTrue(editCarPage.selectBodyType.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @Test
    public void selectTransmissionAutomatic() {
        editCarPage.clickByJse(editCarPage.transmissionButtonAutomatic);
        assertTrue(editCarPage.transmissionButtonAutomatic.isSelected());
    }

    @Test
    public void selectTransmissionManual() {
        editCarPage.clickByJse(editCarPage.transmissionButtonManual);
        assertTrue(editCarPage.transmissionButtonManual.isSelected());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "0",
            "10",
            "5.4",
            "4.4",
            "6.7"
    })
    public void inputEngineValid(String input) {
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys(input);

        assertTrue(editCarPage.inputEngine.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "-0.1, " + TIP_MIN_ENGINE,
            "10.1, " + TIP_MAX_ENGINE
    })
    public void inputEngineInvalid(String input, String tipText) {
        editCarPage.inputEngine.clear();
        editCarPage.inputEngine.sendKeys(input);

        assertTrue(editCarPage.inputEngine.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForInputEngine.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "2000",
            "2010",
            "2005.4",
            "1904.4",
            "1966.7"
    })
    public void inputYearValid(String input) {
        editCarPage.inputYear.clear();
        editCarPage.inputYear.sendKeys(input);
        assertTrue(editCarPage.inputYear.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "1879.9, " + TIP_MIN_YEAR,
            "2100.1, " + TIP_MAX_YEAR,
            "1879, " + TIP_MIN_YEAR,
            "2101, " + TIP_MAX_YEAR
    })
    public void inputYearInvalid(String input, String tipText) {
        editCarPage.inputYear.clear();
        editCarPage.inputYear.sendKeys(input);

        assertTrue(editCarPage.inputYear.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForInputYear.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "sadlkadsf klkads hfjhdsa f hakldshfkhdsakf hlkdsalh fjldsaf kjlhdsaf k",
            "ads fdsafsdf adsf dsaf dsaf  3233221#@&(*)",
            "*)*)&%**^)(*(KKHL HLKJ HLH KHLKh hkjhewkjhewrkh",
            "JSodja;ls lkj;lkdsajfoiuewqp j ;laskdufoiew, wef",
            "aaaa aaaaaa aaaaa aaaaa a",
            LESS_MAX_SHORT_DESCRIPTION

    })
    public void inputShortDescriptionValid(String input) {
        editCarPage.textareaShortDescription.clear();
        editCarPage.textareaShortDescription.sendKeys(input);

        assertTrue(editCarPage.textareaShortDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "aaa aaa ^ aaa*(098aaaa|" + TIP_MIN_LENGTH_SHORT_DESCRIPTION,
            MORE_MAX_SHORT_DESCRIPTION + "|" + TIP_MAX_LENGTH_SHORT_DESCRIPTION
    }, delimiter = '|')
    public void inputShortDescriptionInvalid(String input, String tipText) {
        editCarPage.textareaShortDescription.clear();
        editCarPage.textareaShortDescription.sendKeys(input);

        assertTrue(editCarPage.textareaShortDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForTextareaShortDescription.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            LESS_MAX_DESCRIPTION,
            MAX_DESCRIPTION,
            "aaaaaaaaaaaaaaaaaaaaaaa(*)&(^&aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "aaaaaaaaaaaaaaaaaa769876876a $$^&$*()*&)&(^$aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                    "AAAAAAAAAAAAAAAAAAAAAA"
    })
    public void inputDescriptionValid(String input) {
        editCarPage.scrollDown();
        editCarPage.textareaDescription.clear();
        editCarPage.textareaDescription.sendKeys(input);

        assertTrue(editCarPage.textareaDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "aaa aaa ^ aaa*(098aaaa|" + TIP_MIN_LENGTH_DESCRIPTION,
            "aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa a|" + TIP_MIN_LENGTH_DESCRIPTION
    }, delimiter = '|')
    public void inputDescriptionInvalid(String input, String tipText) {
        editCarPage.textareaDescription.clear();
        editCarPage.textareaDescription.sendKeys(input);

        assertTrue(editCarPage.textareaDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForTextareaDescription.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "a, " + TIP_MIN_LENGTH_2,
            "aaaaa aaaaa aaaaa aaaaa aa, " + TIP_MAX_LENGTH_25,
            "%, " + TIP_INCORRECT_OPTION,
            "aaaaa aaaaa aaaaa aaaa aa%, " + TIP_INCORRECT_OPTION,
            "prado@, " + TIP_INCORRECT_OPTION
    })
    public void inputOptionInvalid(String input, String tipText) {
        editCarPage.scrollDown();
        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(input);

        assertTrue(editCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, editCarPage.invalidTipForInputOptionField.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "aa",
            "AAaaa bBbbb-cCcCc ddd'd23",
            "ABS",
            "Climate Control"
    })
    public void inputOptionValid(String input) {
        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(input);

        assertTrue(editCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @Test
    public void chooseValidImageCar() {
        driver.navigate().refresh();
        webDriverWait.until(ExpectedConditions.visibilityOf(editCarPage.image));
        String imgBefore = editCarPage.image.getAttribute("src");
        assertEquals(imgBefore, editCarPage.image.getAttribute("src"));

        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_VALID);
        webDriverWait.until(ExpectedConditions.visibilityOf(editCarPage.image));
        assertNotEquals(imgBefore, editCarPage.image.getAttribute("src"));
    }

    @Test
    public void chooseInvalidImageCar() {
        String imgBefore = editCarPage.image.getAttribute("src");
        assertEquals(imgBefore, editCarPage.image.getAttribute("src"));

        editCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_INVALID);
        webDriverWait.until(ExpectedConditions.visibilityOf(editCarPage.wrongToast));
        editCarPage.wrongToast.isDisplayed();
        assertEquals(editCarPage.titleWrongToast.getText(), "Invalid file");
        assertEquals(editCarPage.messageWrongToast.getText(), "Please select correct image format!");

        assertEquals(imgBefore, editCarPage.image.getAttribute("src"));

        editCarPage.clickByJse(editCarPage.closeButtonWrongToast);
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "a",
            "AAaaa bBbbb-cCcCc ddd'd23a",
            "ABS^%",
            "Climate_Control"
    })
    public void disableAddOptionButton(String input) {
        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(input);

        assertTrue(!editCarPage.addOptionButton.isEnabled());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "ABS",
            "Automatic headlights",
            "Signaling",
            "Climate Control"
    })
    public void enableAddOptionButton(String input) {
        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(input);

        assertTrue(editCarPage.addOptionButton.isEnabled());
    }


    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "Any option"
    })
    public void addOption(String option) {
        editCarPage.scrollDown();
        By optionId = By.id(String.format("option-'%s'", option));
        By deleteOptionButtonId = By.id(String.format("delete-option-'%s'", option));

        try {
            driver.findElement(optionId);
            fail("Option found before adding!");
        } catch (NoSuchElementException e) {}

        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(option);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editCarPage.addOptionButton));
        editCarPage.clickByJse(editCarPage.addOptionButton);
        assertEquals(option, driver.findElement(optionId).getText());
        editCarPage.clickByJse(driver.findElement(deleteOptionButtonId));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "Any option"
    })
    public void deleteOption(String option) {
        editCarPage.scrollDown();
        By optionId = By.id(String.format("option-'%s'", option));
        By deleteOptionButtonId = By.id(String.format("delete-option-'%s'", option));

        try {
            driver.findElement(optionId);
            fail("Option found before adding!");
        } catch (NoSuchElementException e) {

        }

        editCarPage.inputOptionField.clear();
        editCarPage.inputOptionField.sendKeys(option);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(editCarPage.addOptionButton));
        editCarPage.clickByJse(editCarPage.addOptionButton);

        assertEquals(option, driver.findElement(optionId).getText());
        editCarPage.clickByJse(driver.findElement(deleteOptionButtonId));

        try {
            driver.findElement(optionId);
            fail("Option doesn't delete!");
        } catch (NoSuchElementException e) {

        }
    }
}
