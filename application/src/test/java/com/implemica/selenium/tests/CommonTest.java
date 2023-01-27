package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static com.implemica.selenium.helpers.BaseTestValues.DEFAULT_CAR_IMAGE_SRC;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonTest extends BaseSeleniumTest {
    private EditCarPage editCarPage = new EditCarPage();

    private CatalogPage catalogPage = new CatalogPage();

    private AddCarPage addCarPage = new AddCarPage();

    private DetailsCarPage detailsCarPage = new DetailsCarPage();

    private CatalogAuthPage catalogAuthPage = new CatalogAuthPage();

    private LogInPage logInPage = new LogInPage();

    @Test
    public void commonTest() {
        oneCase(STANDARD_CAR, CarValue.builder()
                .imageName(IMAGE_NAME_FORD_MUSTANG)
                .brand(CarBrand.FORD)
                .model("Mustang GT")
                .bodyType(CarBodyType.SPORTCAR)
                .transmissionType(CarTransmissionType.AUTOMATIC)
                .engine("4.0")
                .year("2018")
                .shortDescription("Lorem Ipsum has been the industry's standard dummy text ever since the 1500.")
                .description("Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                        "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                        "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
                        "It has survived not only five centuries, but also the leap into electronic typesetting, " +
                        "remaining essentially unchanged. " +
                        "It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages," +
                        "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
                .build());
    }

    private void oneCase(CarValue carValueAdd, CarValue carValueEdit) {
        catalogPage.clickLogIn().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
        catalogPage.scrollDown();
        catalogPage.scrollUp();

        String carAddTitle = getTitleCar(carValueAdd.brand, carValueAdd.model);
        String carAddBodyType = carValueAdd.bodyType.stringValue;
        String carAddTransmission = carValueAdd.transmissionType.stringValue;
        String carAddEngine = getEngineDetailsPage(carValueAdd.engine);
        List<String> carAddOptions = getOptionsDetailsPage(carValueAdd.options);
        addCarPage.openAddCarPage().fillForm(carValueAdd).clickSaveButton();


        assertTrue(catalogAuthPage.isToastDisplayed());
        assertEquals(TITLE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_ADD));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_ADD));
        catalogAuthPage.clickCloseToastButton();

        assertNotEquals(DEFAULT_CAR_IMAGE_SRC, catalogAuthPage.getImageSrcFirstCar());
        assertTrue(isImagesEquals(carValueAdd.imageName, catalogAuthPage.getImageSrcFirstCar()));
        assertEquals(carAddTitle, catalogAuthPage.getTitleFirstCar(carAddTitle));
        assertEquals(carValueAdd.shortDescription, catalogAuthPage.getShortDescriptionFirstCar(carValueAdd.shortDescription));

        catalogAuthPage.clickFirstCarImage();

        assertNotEquals(DEFAULT_CAR_IMAGE_SRC, detailsCarPage.getImageSrc());
        assertTrue(isImagesEquals(carValueAdd.imageName, detailsCarPage.getImageSrc()));
        assertEquals(carAddTitle, detailsCarPage.getTitleCar(carAddTitle));
        assertEquals(carAddBodyType, detailsCarPage.getBodyTypeCar(carAddBodyType));
        assertEquals(carAddTransmission, detailsCarPage.getTransmissionTypeCar(carAddTransmission));
        assertEquals(carAddEngine, detailsCarPage.getEngineCar(carAddEngine));
        assertEquals(carValueAdd.shortDescription, detailsCarPage.getShortDescriptionCar(carValueAdd.shortDescription));
        assertEquals(carValueAdd.description, detailsCarPage.getDescriptionCar(carValueAdd.description));
        assertEquals(carAddOptions, detailsCarPage.getAllOptions());

        String oldImageUrl;
        String carEditTitle = getTitleCar(carValueEdit.brand, carValueEdit.model);
        String carEditBodyType = carValueEdit.bodyType.stringValue;
        String carEditTransmission = carValueEdit.transmissionType.stringValue;
        String carEditEngine = getEngineDetailsPage(carValueAdd.engine);
        List<String> carEditOptions = getOptionsDetailsPage(carValueEdit.options);

        editCarPage.openEditFirstCarPage();
        oldImageUrl = editCarPage.getSrcImage();
        editCarPage.deleteAllOptions()
                .fillForm(CarValue.builder()
                        .options(DEFAULT_OPTIONS)
                        .build())
                .fillForm(carValueEdit)
                .clickSaveButton();


        assertTrue(catalogAuthPage.isToastDisplayed());
        assertEquals(TITLE_SUCCESSFULLY_CAR_EDIT, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_EDIT));
        assertEquals(MESSAGE_SUCCESSFULLY_CAR_EDIT, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_EDIT));
        catalogAuthPage.clickCloseToastButton();

        assertNotEquals(oldImageUrl, catalogAuthPage.getImageSrcFirstCar());
        assertTrue(isImagesEquals(carValueEdit.imageName, catalogAuthPage.getImageSrcFirstCar()));
        assertEquals(carEditTitle, catalogAuthPage.getTitleFirstCar(carEditTitle));
        assertEquals(carValueEdit.shortDescription, catalogAuthPage.getShortDescriptionFirstCar(carValueEdit.shortDescription));

        catalogAuthPage.clickFirstCarImage();

        assertNotEquals(oldImageUrl, detailsCarPage.getImageSrc());
        assertTrue(isImagesEquals(carValueEdit.imageName, detailsCarPage.getImageSrc()));
        assertEquals(carEditTitle, detailsCarPage.getTitleCar(carEditTitle));
        assertEquals(carEditBodyType, detailsCarPage.getBodyTypeCar(carEditBodyType));
        assertEquals(carEditTransmission, detailsCarPage.getTransmissionTypeCar(carEditTransmission));
        assertEquals(carEditEngine, detailsCarPage.getEngineCar(carEditEngine));
        assertEquals(carValueEdit.shortDescription, detailsCarPage.getShortDescriptionCar(carValueEdit.shortDescription));
        assertEquals(carValueEdit.description, detailsCarPage.getDescriptionCar(carValueEdit.description));
        assertEquals(carEditOptions, detailsCarPage.getAllOptions());

        detailsCarPage
                .clickLogo()
                .clickFirstCarDeleteButton()
                .clickFirstCarDeleteModalConfirm()
                .clickCloseToastButton()
                .clickAddCarButton();
    }
}
