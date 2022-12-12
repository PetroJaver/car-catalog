package com.implemica.selenium.tests;

import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.Header;
import com.implemica.selenium.helpers.AddCarTestValues;
import com.implemica.selenium.helpers.BaseTestValues;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddCarTest extends BaseSeleniumTest{

    @Ignore
    @Test
    public void addCarForm() throws Exception{
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Header header = new Header();
        header.openMainPage();
        CatalogAuthPage catalogAuthPage = header.clickLogIn().validFillFieldsWithRealAdmin().clickLogInButton();
        catalogAuthPage.clickCloseButtonSuccessToast();
        webDriverWait.until(driver -> !catalogAuthPage.getSuccessToast().isDisplayed());

        AddCarPage addCarPage =  header.clickAddCarButton();
        addCarPage.scrollDownUp();
        assertTrue(addCarPage.getImageAttributeSrc().equals(AddCarTestValues.DEFAULT_IMAGE_PATH));
        addCarPage.noValidFillFileInput();

        webDriverWait.until(driver->addCarPage.getWrongToast().isDisplayed());
        assertTrue(addCarPage.getTitleWrongToast().equals(AddCarTestValues.INVALID_FILE_TITLE));
        assertTrue(addCarPage.getMessageWrongToast().equals(AddCarTestValues.INVALID_FILE_MESSAGE));
        addCarPage.closeButtonWrongToast();

        assertTrue(addCarPage.getImageAttributeSrc().equals(AddCarTestValues.DEFAULT_IMAGE_PATH));
        assertTrue(addCarPage.invalidTipForInputFile().equals(BaseTestValues.REQUIRED_TIP));

        addCarPage.validFillFileInput();
        assertFalse(addCarPage.getImageAttributeSrc().equals(AddCarTestValues.DEFAULT_IMAGE_PATH));
        assertTrue(addCarPage.validTipForForInputFile().equals(BaseTestValues.VALID_INPUT));

        addCarPage.selectBrand();
        assertTrue(addCarPage.validTipForForSelectBrand().equals(BaseTestValues.VALID_INPUT));
        addCarPage.noSelectBrand();
        assertTrue(addCarPage.invalidTipForSelectBrand().equals(AddCarTestValues.NO_CHOOSE_BRAND));
        addCarPage.selectBrand();
        assertTrue(addCarPage.validTipForForSelectBrand().equals(BaseTestValues.VALID_INPUT));

        addCarPage.invalidPatternFillModel();
        assertTrue(addCarPage.invalidTipForInputModel().equals(AddCarTestValues.INVALID_PATTERN));
        addCarPage.emptyFillModel();
        assertTrue(addCarPage.invalidTipForInputModel().equals(BaseTestValues.REQUIRED_TIP));
        addCarPage.invalidMinLengthFillModel();
        assertTrue(addCarPage.invalidTipForInputModel().equals(AddCarTestValues.INVALID_MIN_LENGTH_MODEL_OR_OPTION));
        addCarPage.invalidMaxLengthFillModel();
        assertTrue(addCarPage.invalidTipForInputModel().equals(AddCarTestValues.INVALID_MAX_LENGTH_MODEL));
        addCarPage.emptyFillModel();
        addCarPage.validFillModel();
        assertTrue(addCarPage.validTipForInputModel().equals(BaseTestValues.VALID_INPUT));

        addCarPage.selectBodyType();
        assertTrue(addCarPage.validTipForForSelectBodyType().equals(BaseTestValues.VALID_INPUT));
        addCarPage.noSelectBodyType();
        assertTrue(addCarPage.invalidTipForSelectBodyType().equals(AddCarTestValues.NO_CHOOSE_BODY));
        addCarPage.selectBodyType();
        assertTrue(addCarPage.validTipForForSelectBodyType().equals(BaseTestValues.VALID_INPUT));

        addCarPage.clickManualButton();
        assertTrue(addCarPage.getTransmissionButtonManual().isSelected());
        assertFalse(addCarPage.getTransmissionButtonAutomatic().isSelected());
        addCarPage.clickAutomaticButton();
        assertTrue(addCarPage.getTransmissionButtonAutomatic().isSelected());
        assertFalse(addCarPage.getTransmissionButtonManual().isSelected());

        addCarPage.invalidMinLengthFillEngine();
        assertTrue(addCarPage.invalidTipForInputEngine().equals(AddCarTestValues.INVALID_MIN_ENGINE));
        addCarPage.emptyFillEngine();
        assertTrue(addCarPage.invalidTipForInputEngine().equals(BaseTestValues.REQUIRED_TIP));
        addCarPage.invalidMaxLengthFillEngine();
        assertTrue(addCarPage.invalidTipForInputEngine().equals(AddCarTestValues.INVALID_MAX_ENGINE));
        addCarPage.emptyFillEngine();
        addCarPage.validFillEngine();
        assertTrue(addCarPage.validTipForInputEngine().equals(BaseTestValues.VALID_INPUT));

        addCarPage.invalidMinLengthFillYear();
        assertTrue(addCarPage.invalidTipForInputYear().equals(AddCarTestValues.INVALID_MIN_YEAR));
        addCarPage.emptyFillYear();
        assertTrue(addCarPage.invalidTipForInputYear().equals(BaseTestValues.REQUIRED_TIP));
        addCarPage.invalidMaxLengthFillYear();
        assertTrue(addCarPage.invalidTipForInputYear().equals(AddCarTestValues.INVALID_MAX_YEAR));
        addCarPage.emptyFillYear();
        addCarPage.validFillYear();
        assertTrue(addCarPage.validTipForInputYear().equals(BaseTestValues.VALID_INPUT));

        addCarPage.invalidMinLengthFillShortDescription();
        assertTrue(addCarPage.invalidTipForTextareaShortDescription().equals(AddCarTestValues.INVALID_MIN_SHORT_DESCRIPTION));
        addCarPage.emptyFillShortDescription();
        assertTrue(addCarPage.invalidTipForTextareaShortDescription().equals(BaseTestValues.REQUIRED_TIP));
        addCarPage.invalidMaxLengthFillShortDescription();
        assertTrue(addCarPage.invalidTipForTextareaShortDescription().equals(AddCarTestValues.INVALID_MAX_SHORT_DESCRIPTION));
        addCarPage.emptyFillShortDescription();
        addCarPage.validFillShortDescription();
        assertTrue(addCarPage.validTipForTextareaShortDescription().equals(BaseTestValues.VALID_INPUT));

        addCarPage.invalidMinLengthFillDescription();
        assertTrue(addCarPage.invalidTipForTextareaDescription().equals(AddCarTestValues.INVALID_MIN_DESCRIPTION));
        addCarPage.emptyFillDescription();
        assertTrue(addCarPage.invalidTipForTextareaDescription().equals(BaseTestValues.REQUIRED_TIP));
        addCarPage.invalidMaxLengthFillDescription();
        assertTrue(addCarPage.invalidTipForTextareaDescription().equals(AddCarTestValues.INVALID_MAX_DESCRIPTION));
        addCarPage.emptyFillDescription();
        addCarPage.validFillDescription();
        assertTrue(addCarPage.validTipForTextareaDescription().equals(BaseTestValues.VALID_INPUT));

        assertFalse(addCarPage.getCommonOptionCheckButton().isSelected());
        addCarPage.clickCommonOptionCheckButton();
        assertTrue(addCarPage.getCommonOptionCheckButton().isSelected());
        addCarPage.clickCommonOptionCheckButton();
        assertFalse(addCarPage.getCommonOptionCheckButton().isSelected());

        addCarPage.invalidPatternFillOption();
        assertTrue(addCarPage.invalidTipForInputOption().equals(AddCarTestValues.INVALID_PATTERN));
        addCarPage.emptyFillOption();
        assertFalse(addCarPage.getInvalidTipForInputOption().isDisplayed());
        assertFalse(addCarPage.getValidTipForInputOption().isDisplayed());
        addCarPage.invalidMinLengthFillOption();
        assertTrue(addCarPage.invalidTipForInputOption().equals(AddCarTestValues.INVALID_MIN_LENGTH_MODEL_OR_OPTION));
        addCarPage.invalidMaxLengthFillOption();
        assertTrue(addCarPage.invalidTipForInputOption().equals(AddCarTestValues.INVALID_MIN_LENGTH_OPTION));
        addCarPage.emptyFillOption();
        addCarPage.validFillOption();
        assertTrue(addCarPage.validTipForInputOption().equals(BaseTestValues.VALID_INPUT));
        addCarPage.emptyFillOption();

        assertFalse(addCarPage.getAddOptionButton().isEnabled());
        addCarPage.validFillOption(AddCarTestValues.VALID_OPTION);
        assertTrue(addCarPage.getAddOptionButton().isEnabled());
        addCarPage.clickAddOptionButton();
        assertFalse(addCarPage.getAddOptionButton().isEnabled());
        assertTrue(addCarPage.getAddedOption(AddCarTestValues.VALID_OPTION).isDisplayed());
        assertTrue(addCarPage.getTextAddedOption(AddCarTestValues.VALID_OPTION).equals(AddCarTestValues.VALID_OPTION));
        addCarPage.clickDropButtonAddedOption(AddCarTestValues.VALID_OPTION);

        addCarPage.clickResetButton();
        assertTrue(addCarPage.isAllFieldNotTouch());

        addCarPage.validFillFileInput().selectBrand().validFillModel(AddCarTestValues.MODEL_FOR_ADD).selectBodyType().validFillEngine()
                .validFillYear().validFillShortDescription(AddCarTestValues.SHORT_DESCRIPTION_FOR_ADD).validFillDescription(AddCarTestValues.DESCRIPTION_FOR_ADD)
                .clickCommonOptionCheckButton("4").clickCommonOptionCheckButton("7").validFillOption(AddCarTestValues.VALID_OPTION)
                .clickAddOptionButton().clickAddCarButton();

        catalogAuthPage.getMessageSuccessToast();
    }
}
