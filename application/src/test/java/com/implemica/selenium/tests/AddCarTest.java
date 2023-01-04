package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.LogInPage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import static com.implemica.model.enums.CarBrand.TOYOTA;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.selenium.helpers.AddCarTestValues.*;
import static com.implemica.selenium.helpers.DetailsCarTestValues.XPATH_FORMAT_FOR_IMG_ADDED_CAR;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;

public class AddCarTest extends BaseSeleniumTest {
    private static AddCarPage addCarPage;

    @BeforeAll
    public static void beforeAll() {
        addCarPage = new LogInPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD).clickAddCarButton();
        webDriverWait.until(ExpectedConditions.urlToBe(ADD_CAR_URL));
    }

    @AfterAll
    @SneakyThrows
    public static void afterAll() {
        addCarPage.logOut();
    }

    @ParameterizedTest(name = "testcase {index} => select = ''{0}''")
    @EnumSource(CarBrand.class)
    public void selectBrand(CarBrand brand) {
        Select selectBrand = new Select(addCarPage.selectBrand);
        selectBrand.selectByVisibleText(brand.stringValue);

        assertTrue(addCarPage.selectBrand.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
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
        addCarPage.inputModel.clear();
        addCarPage.inputModel.sendKeys(input);

        assertTrue(addCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForInputModel.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "aa",
            "AAaaa bBbbb-cCcCc ddd'dd AAaaa bBbbb-cCc",
            "RAV 4",
            "Prado"
    })
    public void inputModelValid(String input) {
        addCarPage.inputModel.clear();
        addCarPage.inputModel.sendKeys(input);

        assertTrue(addCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testCase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "1234", "user_user_user_use_r", "####", "&&&&", "1234567890", "op.jkl", "user-name@345yy",
            "USER _ NAME", "USER@Er *_NAME", "USER-  NAME"})
    public void inputModelTipRequired(String input) {
        addCarPage.inputModel.sendKeys(input);
        addCarPage.inputModel.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        addCarPage.inputModel.sendKeys(Keys.chord(Keys.DELETE));
        assertTrue(addCarPage.inputModel.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertTrue(addCarPage.invalidTipForInputModel.getText().equals(TIP_REQUIRED));
    }

    @ParameterizedTest(name = "testcase {index} => select = ''{0}''")
    @EnumSource(CarBodyType.class)
    public void selectBodyType(CarBodyType bodyType) {
        Select selectBodyType = new Select(addCarPage.selectBodyType);
        selectBodyType.selectByVisibleText(bodyType.stringValue);
        assertTrue(addCarPage.selectBodyType.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @Test
    public void selectTransmissionAutomatic() {
        addCarPage.clickByJse(addCarPage.transmissionButtonAutomatic);
        assertTrue(addCarPage.transmissionButtonAutomatic.isSelected());
    }

    @Test
    public void selectTransmissionManual() {
        addCarPage.clickByJse(addCarPage.transmissionButtonManual);
        assertTrue(addCarPage.transmissionButtonManual.isSelected());
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
        addCarPage.inputEngine.clear();
        addCarPage.inputEngine.sendKeys(input);

        assertTrue(addCarPage.inputEngine.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "-0.1, " + TIP_MIN_ENGINE,
            "10.1, " + TIP_MAX_ENGINE
    })
    public void inputEngineInvalid(String input, String tipText) {
        addCarPage.inputEngine.clear();
        addCarPage.inputEngine.sendKeys(input);
        assertTrue(addCarPage.inputEngine.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForInputEngine.getText());
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
        addCarPage.inputYear.clear();
        addCarPage.inputYear.sendKeys(input);
        assertTrue(addCarPage.inputYear.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "1879.9, " + TIP_MIN_YEAR,
            "2100.1, " + TIP_MAX_YEAR,
            "1879, " + TIP_MIN_YEAR,
            "2101, " + TIP_MAX_YEAR
    })
    public void inputYearInvalid(String input, String tipText) {
        addCarPage.inputYear.clear();
        addCarPage.inputYear.sendKeys(input);

        assertTrue(addCarPage.inputYear.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForInputYear.getText());
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
        addCarPage.textareaShortDescription.clear();
        addCarPage.textareaShortDescription.sendKeys(input);

        assertTrue(addCarPage.textareaShortDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "aaa aaa ^ aaa*(098aaaa|" + TIP_MIN_LENGTH_SHORT_DESCRIPTION,
            MORE_MAX_SHORT_DESCRIPTION + "|" + TIP_MAX_LENGTH_SHORT_DESCRIPTION
    }, delimiter = '|')
    public void inputShortDescriptionInvalid(String input, String tipText) {
        addCarPage.textareaShortDescription.clear();
        addCarPage.textareaShortDescription.sendKeys(input);

        assertTrue(addCarPage.textareaShortDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForTextareaShortDescription.getText());
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
        addCarPage.scrollDown();
        addCarPage.textareaDescription.clear();
        addCarPage.textareaDescription.sendKeys(input);

        assertTrue(addCarPage.textareaDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}'', tip = ''{1}''")
    @CsvSource(value = {
            "aaa aaa ^ aaa*(098aaaa|" + TIP_MIN_LENGTH_DESCRIPTION,
            "aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa aaaaa a|" + TIP_MIN_LENGTH_DESCRIPTION
    }, delimiter = '|')
    public void inputDescriptionInvalid(String input, String tipText) {
        addCarPage.textareaDescription.clear();
        addCarPage.textareaDescription.sendKeys(input);

        assertTrue(addCarPage.textareaDescription.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForTextareaDescription.getText());
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
        addCarPage.scrollDown();
        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(input);

        assertTrue(addCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(tipText, addCarPage.invalidTipForInputOptionField.getText());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "aa",
            "AAaaa bBbbb-cCcCc ddd'd23",
            "ABS",
            "Climate Control"
    })
    public void inputOptionValid(String input) {
        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(input);

        assertTrue(addCarPage.inputOptionField.getAttribute("class").matches(VALIDATION_CLASS_REG_VALID));
    }

    @Test
    public void chooseValidImageCar() {
        driver.navigate().refresh();
        webDriverWait.until(ExpectedConditions.visibilityOf(addCarPage.image));
        assertEquals(DEFAULT_CAR_IMAGE_PATH, addCarPage.image.getAttribute("src"));
        addCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_VALID);
        assertNotEquals(DEFAULT_CAR_IMAGE_PATH, addCarPage.image.getAttribute("src"));
    }

    @Test
    public void chooseInvalidImageCar() {
        assertEquals(DEFAULT_CAR_IMAGE_PATH, addCarPage.image.getAttribute("src"));
        addCarPage.inputImage.sendKeys(INPUT_IMAGE_NAME_INVALID);
        webDriverWait.until(ExpectedConditions.visibilityOf(addCarPage.wrongToast));
        addCarPage.wrongToast.isDisplayed();
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(addCarPage.titleWrongToast,"Invalid file"));
        assertEquals(addCarPage.titleWrongToast.getText(), "Invalid file");
        webDriverWait.until(ExpectedConditions.textToBePresentInElement(addCarPage.messageWrongToast,"Please select correct image format!"));
        assertEquals(addCarPage.messageWrongToast.getText(), "Please select correct image format!");
        assertEquals(DEFAULT_CAR_IMAGE_PATH, addCarPage.image.getAttribute("src"));

        addCarPage.clickByJse(addCarPage.closeButtonWrongToast);
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "a",
            "AAaaa bBbbb-cCcCc ddd'd23a",
            "ABS^%",
            "Climate_Control"
    })
    public void disableAddOptionButton(String input) {
        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(input);

        assertTrue(!addCarPage.addOptionButton.isEnabled());
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "ABS",
            "Automatic headlights",
            "Signaling",
            "Climate Control"
    })
    public void enableAddOptionButton(String input) {
        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(input);

        assertTrue(addCarPage.addOptionButton.isEnabled());
    }


    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "Any option"
    })
    public void addOption(String option) {
        addCarPage.scrollDown();
        By optionId = By.id(String.format("option-'%s'", option));
        By deleteOptionButtonId = By.id(String.format("delete-option-'%s'", option));
        try {
            driver.findElement(optionId);
            fail("Option found before adding!");
        } catch (NoSuchElementException e) {
        }

        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(option);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(addCarPage.addOptionButton));
        addCarPage.clickByJse(addCarPage.addOptionButton);
        assertEquals(option, driver.findElement(optionId).getText());
        addCarPage.clickByJse(driver.findElement(deleteOptionButtonId));
    }

    @ParameterizedTest(name = "testcase {index} => input = ''{0}''")
    @ValueSource(strings = {
            "Any option"
    })
    public void deleteOption(String option) {
        addCarPage.scrollDown();
        By optionId = By.id(String.format("option-'%s'", option));
        By deleteOptionButtonId = By.id(String.format("delete-option-'%s'", option));
        try {
            driver.findElement(optionId);
            fail("Option found before adding!");
        } catch (NoSuchElementException e) {

        }

        addCarPage.inputOptionField.clear();
        addCarPage.inputOptionField.sendKeys(option);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(addCarPage.addOptionButton));
        addCarPage.clickByJse(addCarPage.addOptionButton);

        assertEquals(option, driver.findElement(optionId).getText());
        addCarPage.clickByJse(driver.findElement(deleteOptionButtonId));

        try {
            driver.findElement(optionId);
            fail("Option doesn't delete!");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void clickCancelButtonModalConfirm() {
        driver.navigate().refresh();
        assertEquals(ADD_CAR_URL, driver.getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, driver.getTitle());

        addCarPage.clickByJse(addCarPage.cancelButton);
        addCarPage.clickByJse(addCarPage.confirmModalButton);

        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals(BASE_TITLE, driver.getTitle());
        addCarPage.openAddCarPage();
    }

    @Test
    public void clickCancelButtonModalCancel() {
        assertEquals(ADD_CAR_URL, driver.getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, driver.getTitle());

        addCarPage.clickByJse(addCarPage.cancelButton);
        addCarPage.clickByJse(addCarPage.cancelModalButton);

        assertEquals(ADD_CAR_URL, driver.getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, driver.getTitle());
        addCarPage.openAddCarPage();
    }

    @ParameterizedTest
    @MethodSource("sourceValueForEnableAddCarButton")
    public void enableAddCarButton(CarValue carValue) {
        driver.navigate().refresh();

        addCarPage.addCar(carValue);

        assertEquals(carValue.isValid, addCarPage.addCarButton.isEnabled());
    }

    @ParameterizedTest
    @MethodSource("sourceValueForAddCar")
    public void addCar(CarValue carValue) {
        CatalogAuthPage catalogAuthPage = new CatalogAuthPage();
        addCarPage.openAddCarPage().addCar(carValue);
        addCarPage.clickByJse(addCarPage.addCarButton);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.visibilityOf(catalogAuthPage.successToast));
            assertEquals(MESSAGE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.messageSuccessToast.getText());
            assertEquals(TITLE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.titleSuccessToast.getText());
            catalogAuthPage.clickByJse(catalogAuthPage.closeButtonSuccessToast);
            WebElement carCard = driver.findElement(By.xpath(String.format(XPATH_FOR_ADDED_CAR,
                    carValue.brand.name(), carValue.model)));
            assertTrue(carCard.isDisplayed());
            assertFalse(driver.findElement(By.xpath(String.format(XPATH_FORMAT_FOR_IMG_ADDED_CAR, carValue.brand, carValue.model)))
                    .getAttribute("src").matches(String.format(".*%s", DEFAULT_CAR_IMAGE_PATH)));
            assertEquals(toTitleCase(carValue.brand.name()) + " " + carValue.model,
                    driver.findElement(By.xpath(String.format(XPATH_FOR_TITLE_ADDED_CAR, carValue.brand, carValue.model))).getText());
            assertEquals(carValue.shortDescription, driver.findElement(By.xpath(String.format(XPATH_FOR_BODY_ADDED_CAR, carValue.brand, carValue.model))).getText());
        } finally {
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_DELETE_ADDED_CAR,
                    carValue.brand, carValue.model))));
            addCarPage.clickByJse(driver.findElement(By.xpath(String.format(XPATH_FOR_CONFIRM_DELETE_MODAL,
                    carValue.brand, carValue.model))));
            driver.navigate().to(ADD_CAR_URL);
        }
    }

    private static Stream<CarValue> sourceValueForAddCar() {
        return Stream.of(new CarValue(TOYOTA, "Prado", CarBodyType.JEEP, AUTOMATIC, 4D,
                2006, INPUT_IMAGE_NAME_VALID, randomAlphabetic(149), randomAlphabetic(2000),
                List.of(randomAlphabetic(10), randomAlphabetic(12), randomAlphabetic(2)), true));
    }

    private static Stream<CarValue> sourceValueForEnableAddCarButton() {
        return Stream.of(new CarValue(TOYOTA, "Prado", CarBodyType.JEEP, AUTOMATIC, 4D,
                        2006, INPUT_IMAGE_NAME_VALID, randomAlphabetic(149), randomAlphabetic(2000),
                        List.of(randomAlphabetic(10), randomAlphabetic(12), randomAlphabetic(2)), true),
                new CarValue(TOYOTA, "Prado", CarBodyType.JEEP, AUTOMATIC, 4D,
                        2006, INPUT_IMAGE_NAME_VALID, randomAlphabetic(149), randomAlphabetic(200),
                        List.of(randomAlphabetic(10), randomAlphabetic(40), randomAlphabetic(2)), false));
    }
}
