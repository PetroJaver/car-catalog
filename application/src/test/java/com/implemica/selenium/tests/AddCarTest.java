package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.selenium.helpers.CarValue;
import com.implemica.selenium.pages.AddCarPage;
import com.implemica.selenium.pages.CatalogAuthPage;
import com.implemica.selenium.pages.DetailsCarPage;
import com.implemica.selenium.pages.LogInPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.implemica.model.enums.CarBodyType.*;
import static com.implemica.model.enums.CarBrand.*;
import static com.implemica.model.enums.CarTransmissionType.AUTOMATIC;
import static com.implemica.model.enums.CarTransmissionType.MANUAL;
import static com.implemica.selenium.helpers.BaseTestValues.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class AddCarTest extends BaseSeleniumTest {
    private static AddCarPage addCarPage;
    private static CatalogAuthPage catalogAuthPage;

    private static DetailsCarPage detailsCarPage;

    @BeforeAll
    public static void beforeAll() {
        detailsCarPage = new DetailsCarPage();
        catalogAuthPage = new LogInPage().doLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
        addCarPage = catalogAuthPage.clickAddCarButton();
    }

    @AfterAll
    public static void afterAll() {
        catalogAuthPage.doLogout();
    }

    @Test
    public void selectBrandFocusedAfterOpenAddCarPageTest() {
        addCarPage.openAddCarPage();
        assertTrue(addCarPage.isSelectBrandActive());
    }

    @Test
    public void selectBrandTest() {
        addCarPage.openAddCarPage();

        selectBrandCase(AUDI);
        selectBrandCase(ACURA);
        selectBrandCase(ALFA);
        selectBrandCase(ROMEO);
        selectBrandCase(ASTON_MARTIN);
        selectBrandCase(BENTLEY);
        selectBrandCase(BYD);
        selectBrandCase(BMW);
        selectBrandCase(BRILLIANCE);
        selectBrandCase(BUICK);
        selectBrandCase(BUGATTI);
        selectBrandCase(CADILLAC);
        selectBrandCase(CHANGAN);
        selectBrandCase(CHEVROLET);
        selectBrandCase(CHERY);
        selectBrandCase(CHRYSLER);
        selectBrandCase(CITROEN);
        selectBrandCase(DAEWOO);
        selectBrandCase(DACIA);
        selectBrandCase(DAIHATSU);
        selectBrandCase(DODGE);
        selectBrandCase(FAW);
        selectBrandCase(FERRARI);
        selectBrandCase(FIAT);
        selectBrandCase(FORD);
        selectBrandCase(GEELY);
        selectBrandCase(GMC);
        selectBrandCase(GREAT_WALL);
        selectBrandCase(HONDA);
        selectBrandCase(HUMMER);
        selectBrandCase(HYUNDAI);
        selectBrandCase(INFINITI);
        selectBrandCase(JAGUAR);
        selectBrandCase(CarBrand.JEEP);
        selectBrandCase(KIA);
        selectBrandCase(LAMBORGHINI);
        selectBrandCase(LAND_ROVER);
        selectBrandCase(LANCIA);
        selectBrandCase(LEXUS);
        selectBrandCase(LIFAN);
        selectBrandCase(LINCOLN);
        selectBrandCase(LOTUS);
        selectBrandCase(MARUSSIA);
        selectBrandCase(MAYBACH);
        selectBrandCase(MAZDA);
        selectBrandCase(MERCEDES);
        selectBrandCase(MASERATI);
        selectBrandCase(MINI);
        selectBrandCase(MCLAREN);
        selectBrandCase(MITSUBISHI);
        selectBrandCase(NISSAN);
        selectBrandCase(OPEL);
        selectBrandCase(PEUGEOT);
        selectBrandCase(PORSCHE);
        selectBrandCase(RENAULT);
        selectBrandCase(SAAB);
        selectBrandCase(SEAT);
        selectBrandCase(SKODA);
        selectBrandCase(SUBARU);
        selectBrandCase(SUZUKI);
        selectBrandCase(TOYOTA);
        selectBrandCase(PONTIAC);
        selectBrandCase(ROLLS_ROYCE);
        selectBrandCase(SMART);
        selectBrandCase(SANGYONG);
        selectBrandCase(TESLA);
        selectBrandCase(VOLVO);
        selectBrandCase(DATSUN);
        selectBrandCase(VOLKSWAGEN);
        selectBrandCase(TAGAZ);
        selectBrandCase(GENESIS);
    }

    private void selectBrandCase(CarBrand brand) {
        addCarPage.fillForm(CarValue.builder().brand(brand).build());

        assertTrue(addCarPage.getClassBrand().matches(VALIDATION_CLASS_REG_VALID));
        assertEquals(brand.name(), addCarPage.getValueBrand(brand.name()));
    }

    @Test
    public void inputModelValidationTest() {
        addCarPage.openAddCarPage();

        //region valid model
        inputModelValidCase("aa");
        inputModelValidCase("AAaaa bBbbb-cCcCc ddd'dd AAaaa bBbbb-cCc");
        inputModelValidCase("RAV 4");
        inputModelValidCase("Prado");
        inputModelValidCase(getRandomText(40));
        inputModelValidCase(getRandomText(39));
        inputModelValidCase(getRandomText(2));
        inputModelValidCase(getRandomText(3));
        inputModelValidCase(getRandomModel());
        //endregion

        //region invalid model
        inputModelTipRequiredCase("1234");
        inputModelTipRequiredCase("user_user_user_use_r");
        inputModelTipRequiredCase("####");
        inputModelTipRequiredCase("&&&&");
        inputModelTipRequiredCase("1234567890");
        inputModelTipRequiredCase("op.jkl");
        inputModelTipRequiredCase("user-name@345yy");
        inputModelTipRequiredCase("USER _ NAME");
        inputModelTipRequiredCase("USER@Er *_NAME");
        inputModelTipRequiredCase("USER-  NAME");

        inputModelInvalidCase("a", TIP_MIN_LENGTH_2);
        inputModelInvalidCase(getRandomText(1), TIP_MIN_LENGTH_2);
        inputModelInvalidCase("aaaaa aaaaa aaaaa aaaa aaaaa aaaaa aaaaaa", TIP_MAX_LENGTH_40);
        inputModelInvalidCase(getRandomText(41), TIP_MAX_LENGTH_40);
        inputModelInvalidCase("%", TIP_INCORRECT_MODEL);
        inputModelInvalidCase("prado%", TIP_INCORRECT_MODEL);
        //endregion
    }

    private void inputModelInvalidCase(String model, String expectedTextTip) {
        addCarPage.fillForm(CarValue.builder().model(model).build());

        assertTrue(addCarPage.getClassModel().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getModelTipText(expectedTextTip));
    }

    private void inputModelValidCase(String model) {
        addCarPage.fillForm(CarValue.builder().model(model).build());

        assertTrue(addCarPage.getClassModel().matches(VALIDATION_CLASS_REG_VALID));
    }

    private void inputModelTipRequiredCase(String model) {
        addCarPage.fillForm(CarValue.builder().model(model).build()).clearModel();

        assertTrue(addCarPage.getClassModel().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(TIP_REQUIRED, addCarPage.getModelTipText(TIP_REQUIRED));
    }

    @Test
    public void selectBodyTypeTest() {
        addCarPage.openAddCarPage();

        selectBodyTypeCase(HATCHBACK);
        selectBodyTypeCase(SEDAN);
        selectBodyTypeCase(MUV);
        selectBodyTypeCase(SUV);
        selectBodyTypeCase(COUPE);
        selectBodyTypeCase(CONVERTIBLE);
        selectBodyTypeCase(WAGON);
        selectBodyTypeCase(VAN);
        selectBodyTypeCase(CarBodyType.JEEP);
        selectBodyTypeCase(SPORTCAR);
    }

    private void selectBodyTypeCase(CarBodyType bodyType) {
        addCarPage.fillForm(CarValue.builder().bodyType(bodyType).build());

        assertTrue(addCarPage.getClassBodyType().matches(VALIDATION_CLASS_REG_VALID));
        assertEquals(bodyType.name(), addCarPage.getValueBodyType(bodyType.name()));
    }

    @Test
    public void selectTransmissionAutomaticTest() {
        addCarPage.openAddCarPage().clickTransmissionAutomatic();
        assertTrue(addCarPage.isTransmissionButtonAutomaticSelected());
    }

    @Test
    public void selectTransmissionManualTest() {
        addCarPage.openAddCarPage().clickTransmissionManual();
        assertTrue(addCarPage.isTransmissionManualButtonSelected());
    }

    @Test
    public void inputEngineValidationTest() {
        addCarPage.openAddCarPage();

        //region valid engine
        inputEngineValidCase("0");
        inputEngineValidCase("0.1");
        inputEngineValidCase("10");
        inputEngineValidCase("9.9");
        inputEngineValidCase("5.4");
        inputEngineValidCase("4.4");
        inputEngineValidCase("6.7");
        inputEngineValidCase(getRandomEngine());
        //endregion

        //region invalid engine
        inputEngineInvalidCase("-0.1", TIP_MIN_ENGINE);
        inputEngineInvalidCase("10.1", TIP_MAX_ENGINE);
        //endregion
    }

    private void inputEngineValidCase(String engine) {
        addCarPage.fillForm(CarValue.builder().engine(engine).build());

        assertTrue(addCarPage.getClassEngine().matches(VALIDATION_CLASS_REG_VALID));
    }

    private void inputEngineInvalidCase(String engine, String expectedTextTip) {
        addCarPage.fillForm(CarValue.builder().engine(engine).build());

        assertTrue(addCarPage.getClassEngine().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getEngineTipText(expectedTextTip));
    }

    @Test
    public void inputYearValidationTest() {
        addCarPage.openAddCarPage();

        //region valid year
        inputYearValidCase("2000");
        inputYearValidCase("2010");
        inputYearValidCase("2005");
        inputYearValidCase("1904");
        inputYearValidCase("2100");
        inputYearValidCase("2099");
        inputYearValidCase("1880");
        inputYearValidCase("1881");
        inputYearValidCase("1970");
        inputYearValidCase(getRandomYear());
        //endregion

        //region invalid year
        inputYearInvalidCase("1879.9", TIP_MIN_YEAR);
        inputYearInvalidCase("1879", TIP_MIN_YEAR);
        inputYearInvalidCase("2100.1", TIP_MAX_YEAR);
        inputYearInvalidCase("2101", TIP_MAX_YEAR);
        inputYearInvalidCase("2099.9", TIP_YEAR_NOT_INTEGER);
        inputYearInvalidCase("1880.1", TIP_YEAR_NOT_INTEGER);
        //endregion
    }

    private void inputYearValidCase(String year) {
        addCarPage.fillForm(CarValue.builder().year(year).build());

        assertTrue(addCarPage.getClassYear().matches(VALIDATION_CLASS_REG_VALID));
    }

    private void inputYearInvalidCase(String year, String expectedTextTip) {
        addCarPage.fillForm(CarValue.builder().year(year).build());

        assertTrue(addCarPage.getClassYear().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getYearTipText(expectedTextTip));
    }

    @Test
    public void inputShortDescriptionValidationTest() {
        addCarPage.openAddCarPage();

        //region valid short description
        inputShortDescriptionValidCase("sadlkadsf klkads hfjhdsa f hakldshfkhdsakf hlkdsalh fjldsaf kjlhdsaf k");
        inputShortDescriptionValidCase("ads fdsafsdf adsf dsaf dsaf  3233221#@&(*)");
        inputShortDescriptionValidCase("*)*)&%**^)(*(KKHL HLKJ HLH KHLKh hkjhewkjhewrkh");
        inputShortDescriptionValidCase("JSodja;ls lkj;lkdsajfoiuewqp j ;laskdufoiew, wef");
        inputShortDescriptionValidCase("aaaa aaaaaa aaaaa aaaaa a");
        inputShortDescriptionValidCase(LESS_MAX_SHORT_DESCRIPTION);
        inputShortDescriptionValidCase(MAX_SHORT_DESCRIPTION);
        inputShortDescriptionValidCase(getRandomText(25));
        inputShortDescriptionValidCase(getRandomText(26));
        inputShortDescriptionValidCase(getRandomShortDescription());
        //endregion

        //region invalid short description
        inputShortDescriptionInvalidCase("aaa aaa ^ aaa*(098aaaa", TIP_MIN_LENGTH_SHORT_DESCRIPTION);
        inputShortDescriptionInvalidCase(getRandomTextBetween(1, 24), TIP_MIN_LENGTH_SHORT_DESCRIPTION);
        inputShortDescriptionInvalidCase(getRandomText(151), TIP_MAX_LENGTH_SHORT_DESCRIPTION);
        inputShortDescriptionInvalidCase(MORE_MAX_SHORT_DESCRIPTION, TIP_MAX_LENGTH_SHORT_DESCRIPTION);
        //endregion
    }

    private void inputShortDescriptionValidCase(String shortDescription) {
        addCarPage.fillForm(CarValue.builder().shortDescription(shortDescription).build());

        assertTrue(addCarPage.getClassShortDescription().matches(VALIDATION_CLASS_REG_VALID));
    }

    private void inputShortDescriptionInvalidCase(String shortDescription, String expectedTextTip) {
        addCarPage.fillForm(CarValue.builder().shortDescription(shortDescription).build());

        assertTrue(addCarPage.getClassShortDescription().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getShortDescriptionTipText(expectedTextTip));
    }

    @Test
    public void inputDescriptionValidationTest() {
        addCarPage.openAddCarPage();

        //region valid description
        inputDescriptionValidCase(LESS_MAX_DESCRIPTION);
        inputDescriptionValidCase(MAX_DESCRIPTION);
        inputDescriptionValidCase(getRandomText(50));
        inputDescriptionValidCase(getRandomText(51));
        inputDescriptionValidCase(getRandomDescription());
        //endregion

        //region invalid description
        inputDescriptionInvalidCase(getRandomTextBetween(1, 49), TIP_MIN_LENGTH_DESCRIPTION);
        inputDescriptionInvalidCase(getRandomText(5001), TIP_MAX_LENGTH_DESCRIPTION);
        inputDescriptionInvalidCase(MORE_MAX_DESCRIPTION, TIP_MAX_LENGTH_DESCRIPTION);
        //endregion
    }

    private void inputDescriptionValidCase(String description) {
        addCarPage.fillForm(CarValue.builder().description(description).build());

        assertTrue(addCarPage.getClassDescription().matches(VALIDATION_CLASS_REG_VALID));
    }

    private void inputDescriptionInvalidCase(String description, String expectedTextTip) {
        addCarPage.fillForm(CarValue.builder().description(description).build());

        assertTrue(addCarPage.getClassDescription().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getDescriptionTipText(expectedTextTip));
    }

    @Test
    public void inputOptionValidationTest() {
        addCarPage.openAddCarPage();

        //region valid option
        inputOptionValidCase("Any option");
        inputOptionValidCase("Color");
        inputOptionValidCase("Wheels");
        inputOptionValidCase("Performance");
        inputOptionValidCase("All-Wheel Drive");
        inputOptionValidCase("Safety");
        inputOptionValidCase("Luxury");
        inputOptionValidCase("Tech Infotainment");
        inputOptionValidCase("Dealer Installed");
        inputOptionValidCase(dataFactory.getRandomText(2, 25));
        //endregion

        //region invalid option
        inputOptionInvalidCase("a", TIP_MIN_LENGTH_2);
        inputOptionInvalidCase("aaaaa aaaaa aaaaa aaaaa aa", TIP_MAX_LENGTH_25);
        inputOptionInvalidCase("%", TIP_INCORRECT_OPTION);
        inputOptionInvalidCase("aaaaa aaaaa aaaaa aaaa aa%", TIP_INCORRECT_OPTION);
        inputOptionInvalidCase("prado@", TIP_INCORRECT_OPTION);
        //endregion
    }

    private void inputOptionInvalidCase(String option, String expectedTextTip) {
        addCarPage.inputOption(option);

        assertTrue(addCarPage.getClassOptionField().matches(VALIDATION_CLASS_REG_INVALID));
        assertEquals(expectedTextTip, addCarPage.getOptionTipText(expectedTextTip));
    }

    private void inputOptionValidCase(String option) {
        addCarPage.inputOption(option);

        assertTrue(addCarPage.getClassOptionField().matches(VALIDATION_CLASS_REG_VALID));
    }

    @Test
    public void chooseImageTest() {
        //region choose valid image
        chooseValidImageCase(IMAGE_NAME_ANY_CAR);
        chooseValidImageCase(IMAGE_NAME_PORSHE_911);
        chooseValidImageCase(IMAGE_NAME_FORD_MUSTANG);
        chooseValidImageCase(IMAGE_NAME_TOYOTA_HILUX);
        chooseValidImageCase(IMAGE_NAME_MERCEDES_GLA);
        chooseValidImageCase(IMAGE_NAME_ASTON_MARTIN_VANTAGE_DB_11);
        //endregion

        //region choose invalid image
        chooseInvalidImageCase(IMAGE_NAME_INVALID);
        //endregion
    }

    private void chooseValidImageCase(String imagePath) {
        addCarPage.openAddCarPage();
        assertEquals(ADD_CAR_PAGE_DEFAULT_CAR_IMAGE_PATH, addCarPage.getSrcImage());

        addCarPage.fillForm(CarValue.builder().imageName(imagePath).build());
        assertNotEquals(ADD_CAR_PAGE_DEFAULT_CAR_IMAGE_PATH, addCarPage.getSrcImage());
    }

    private void chooseInvalidImageCase(String imagePath) {
        addCarPage.openAddCarPage();
        assertEquals(ADD_CAR_PAGE_DEFAULT_CAR_IMAGE_PATH, addCarPage.getSrcImage());

        addCarPage.fillForm(CarValue.builder().imageName(imagePath).build());
        assertTrue(addCarPage.isToastDisplayed());
        assertEquals(TITLE_INVALID_FILE, addCarPage.getTitleToast(TITLE_INVALID_FILE));
        assertEquals(MESSAGE_INVALID_FILE, addCarPage.getMessageToast(MESSAGE_INVALID_FILE));
        catalogAuthPage.clickCloseToastButton();

        assertEquals(ADD_CAR_PAGE_DEFAULT_CAR_IMAGE_PATH, addCarPage.getSrcImage());
    }

    @Test
    public void enableAddOptionButtonTest() {
        addCarPage.openAddCarPage();

        //region enable add option button
        enableAddOptionButtonCase("Any option");
        enableAddOptionButtonCase("Color");
        enableAddOptionButtonCase("Wheels");
        enableAddOptionButtonCase("Performance");
        enableAddOptionButtonCase("All-Wheel Drive");
        enableAddOptionButtonCase("Safety");
        enableAddOptionButtonCase("Luxury");
        enableAddOptionButtonCase("Tech Infotainment");
        enableAddOptionButtonCase("Dealer Installed");
        enableAddOptionButtonCase(dataFactory.getRandomText(2, 25));
        //endregion

        //region disable add option button
        disableAddOptionButtonCase(getRandomText(1));
        disableAddOptionButtonCase("AAaaa bBbbb-cCcCc ddd'd23a");
        disableAddOptionButtonCase("ABS^%");
        disableAddOptionButtonCase("Climate_Control");
        //endregion
    }

    private void disableAddOptionButtonCase(String option) {
        addCarPage.inputOption(option);

        assertFalse(addCarPage.isAddOptionButtonEnable());
    }

    private void enableAddOptionButtonCase(String option) {
        addCarPage.inputOption(option);

        assertTrue(addCarPage.isAddOptionButtonEnable());
    }

    @Test
    public void optionTest() {
        addCarPage.openAddCarPage().deleteAllOptions();

        //region delete option
        addAndDeleteOptionCase("Any option");
        addAndDeleteOptionCase("Color");
        addAndDeleteOptionCase("Wheels");
        addAndDeleteOptionCase("Performance");
        addAndDeleteOptionCase("All-Wheel Drive");
        addAndDeleteOptionCase("Safety");
        addAndDeleteOptionCase("Luxury");
        addAndDeleteOptionCase("Tech Infotainment");
        addAndDeleteOptionCase("Dealer Installed");
        addAndDeleteOptionCase(dataFactory.getRandomText(2, 25));
        //endregion
    }

    private void addAndDeleteOptionCase(String option) {
        addCarPage.fillForm(CarValue.builder().options(List.of(option)).build());
        assertEquals(List.of(option), addCarPage.getAllOptions());
        addCarPage.clickDeleteOption(option);

        assertTrue(addCarPage.getAllOptions().isEmpty());
    }

    @Test
    public void clickCancelButtonModalConfirmTest() {
        addCarPage.openAddCarPage();
        assertEquals(ADD_CAR_URL, getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, getCurrentTitle());

        addCarPage.clickCancelFormButton().clickConfirmModal();

        assertEquals(BASE_URL, getCurrentUrl());
        assertEquals(BASE_TITLE, getCurrentTitle());
    }

    @Test
    public void clickCancelButtonModalCancelTest() {
        addCarPage.openAddCarPage();
        assertEquals(ADD_CAR_URL, getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, getCurrentTitle());

        addCarPage.clickCancelFormButton().clickCancelModal();

        assertEquals(ADD_CAR_URL, getCurrentUrl());
        assertEquals(ADD_CAR_TITLE, getCurrentTitle());
    }

    @Test
    public void enableSaveCarButtonTest() {
        //region enable save button
        enableSaveCarButtonCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(AUTOMATIC)
                        .engine(getRandomEngine())
                        .year(getRandomYear())
                        .shortDescription(getRandomShortDescription())
                        .description(getRandomDescription())
                        .options(List.of(getRandomOption(), getRandomOption(), getRandomOption()))
                        .build());
        //endregion

        //region disable save button
        disableSaveCarButtonCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(getRandomBrand())
                        .model(getRandomText(1))
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(getRandomTransmissionTypes())
                        .engine("-0.1")
                        .year("1879")
                        .shortDescription(getRandomText(24))
                        .description(getRandomText(49))
                        .options(List.of(getRandomText(1)))
                        .build());

        disableSaveCarButtonCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(getRandomBrand())
                        .model(getRandomText(41))
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(getRandomTransmissionTypes())
                        .engine("10.1")
                        .year("2101")
                        .shortDescription(getRandomText(151))
                        .description(getRandomText(5001))
                        .options(List.of(getRandomText(26)))
                        .build());
        //endregion
    }

    private void enableSaveCarButtonCase(CarValue carValue) {
        addCarPage.openAddCarPage().fillForm(carValue);

        assertTrue(addCarPage.isSaveCarButtonEnable());
    }

    private void disableSaveCarButtonCase(CarValue carValue) {
        addCarPage.openAddCarPage().fillForm(carValue);

        assertFalse(addCarPage.isSaveCarButtonEnable());
    }

    @Test
    public void addCarTest() {
                addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_MERCEDES_GLA)
                        .brand(MERCEDES)
                        .transmissionType(AUTOMATIC)
                        .model("GLA")
                        .bodyType(SUV)
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_MERCEDES_GLA)
                        .brand(MERCEDES)
                        .transmissionType(AUTOMATIC)
                        .model("GLA")
                        .bodyType(SUV)
                        .engine("3")
                        .year("2018")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("Dealer Installed", "Wheels"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_FORD_MUSTANG)
                        .brand(FORD)
                        .transmissionType(MANUAL)
                        .model("Mustang GT")
                        .bodyType(SPORTCAR)
                        .engine("3.5")
                        .year("2020")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("All-Wheel Drive"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_PORSHE_911)
                        .brand(PORSCHE)
                        .transmissionType(AUTOMATIC)
                        .model("911")
                        .bodyType(SPORTCAR)
                        .engine("3.0")
                        .year("2016")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("All-Wheel Drive"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_TOYOTA_HILUX)
                        .brand(TOYOTA)
                        .transmissionType(AUTOMATIC)
                        .model("Hilux")
                        .bodyType(CarBodyType.JEEP)
                        .engine("4.0")
                        .year("2019")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("Safety", "Tech Infotainment", "Dealer Installed"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ASTON_MARTIN_VANTAGE_DB_11)
                        .brand(ASTON_MARTIN)
                        .transmissionType(AUTOMATIC)
                        .model("Vantage DB 11")
                        .bodyType(SPORTCAR)
                        .engine("4.0")
                        .year("2021")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("Safety", "Tech Infotainment"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(TESLA)
                        .transmissionType(AUTOMATIC)
                        .model("Model S")
                        .bodyType(SEDAN)
                        .engine("0.0")
                        .year("2016")
                        .shortDescription("Sed cursus dui sit amet eros molestie feugiat. Phasellus interdum lectus lacus, vel eleifend magna aliquam nec. Nam id lorem venenatis.")
                        .description("In varius porttitor ultricies. Nunc dictum tortor in quam scelerisque, vestibulum hendrerit ligula gravida. Vivamus at ligula eleifend, porttitor nibh in, condimentum arcu. Sed felis lectus, tristique vitae ligula vitae, sagittis imperdiet nunc. Aliquam in arcu cursus, fringilla orci ac, dignissim neque. Vivamus a accumsan ex, quis scelerisque felis. Integer nec arcu metus. Sed pellentesque, magna eu pulvinar egestas, ante felis volutpat risus, at efficitur ipsum neque id tortor. Ut felis est, euismod vitae ornare quis, pretium eu sapien. Ut accumsan pellentesque metus. Donec blandit aliquet auctor. In nec elementum eros. Duis gravida odio leo, vitae aliquam urna rhoncus vitae. Nam sed dictum est. In at velit feugiat, tincidunt erat scelerisque, finibus diam.\n" +
                                "\n" +
                                "Pellentesque eu elementum tortor, id blandit orci. Nulla rhoncus enim ut urna aliquet, at ullamcorper odio pharetra. Fusce eu urna at augue bibendum condimentum ac sed nisl. Duis ullamcorper congue lacus. Sed leo ex, fermentum vitae sem a, aliquam dapibus est. Nulla eget turpis quis elit ultrices faucibus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque sit amet massa iaculis nisl vestibulum mollis quis vitae sapien. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut ut eleifend quam. Aliquam ornare vehicula massa, sit amet pulvinar justo tempor sed. Phasellus volutpat mauris dui, sed lacinia lacus sagittis a. Suspendisse cursus risus sit amet condimentum mattis. Proin dapibus, elit et bibendum placerat, tortor lectus pretium leo, a laoreet diam purus elementum dui. Mauris consectetur iaculis justo id congue.\n" +
                                "\n" +
                                "Vestibulum finibus lectus erat, tristique dignissim libero interdum in. Nunc tincidunt tortor sed ipsum pharetra, quis ultrices felis pretium. Sed vehicula lobortis nunc, sit amet ultricies leo fringilla eleifend. Phasellus ut commodo nisl, at fermentum dui. Ut ullamcorper fringilla viverra. Nullam eu lacus fringilla, placerat tortor in, lacinia dui. Morbi semper nibh quis nunc pulvinar, a auctor tortor vehicula. Ut a lorem nulla. Nulla erat nunc, pharetra at tincidunt quis, elementum at nisl.")
                        .options(List.of("All-Wheel Drive"))
                        .build()
        );

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(TOYOTA)
                        .transmissionType(MANUAL)
                        .model(getRandomText(39))
                        .bodyType(CarBodyType.JEEP)
                        .transmissionType(AUTOMATIC)
                        .engine("9.9")
                        .year("2099")
                        .shortDescription(getRandomText(149))
                        .description(getRandomText(4999))
                        .options(List.of(getRandomText(24)))
                        .build());

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .transmissionType(MANUAL)
                        .brand(TOYOTA)
                        .model(getRandomText(40))
                        .bodyType(CarBodyType.JEEP)
                        .transmissionType(AUTOMATIC)
                        .engine("10.0")
                        .year("2100")
                        .shortDescription(getRandomText(150))
                        .description(getRandomText(5000))
                        .options(List.of(getRandomText(25)))
                        .build());

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .transmissionType(MANUAL)
                        .brand(KIA)
                        .model(getRandomText(2))
                        .bodyType(SPORTCAR)
                        .transmissionType(MANUAL)
                        .engine("0")
                        .year("1880")
                        .shortDescription(getRandomText(25))
                        .description(getRandomText(50))
                        .options(List.of(getRandomText(2)))
                        .build());

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(KIA)
                        .transmissionType(MANUAL)
                        .model(getRandomText(3))
                        .bodyType(SPORTCAR)
                        .transmissionType(MANUAL)
                        .engine("0.1")
                        .year("1881")
                        .shortDescription(getRandomText(26))
                        .description(getRandomText(51))
                        .options(List.of(getRandomText(3)))
                        .build());

        addCarWithImageCase(
                CarValue.builder()
                        .imageName(IMAGE_NAME_PORSHE_911)
                        .brand(getRandomBrand())
                        .transmissionType(MANUAL)
                        .model(getRandomModel())
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(getRandomTransmissionTypes())
                        .engine(getRandomEngine())
                        .year(getRandomYear())
                        .shortDescription(getRandomShortDescription())
                        .description(getRandomDescription())
                        .options(List.of(
                                getRandomOption(),
                                getRandomOption(),
                                getRandomOption()))
                        .build());

        addCarWithoutImageCase(
                CarValue.builder()
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .transmissionType(MANUAL)
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(getRandomTransmissionTypes())
                        .engine(getRandomEngine())
                        .year(getRandomYear())
                        .shortDescription(getRandomShortDescription())
                        .description(getRandomDescription())
                        .options(List.of(
                                getRandomOption(),
                                getRandomOption(),
                                getRandomOption()))
                        .build());

        addExistCar(
                CarValue.builder()
                        .imageName(IMAGE_NAME_ANY_CAR)
                        .brand(getRandomBrand())
                        .model(getRandomModel())
                        .transmissionType(MANUAL)
                        .bodyType(getRandomBodyTypes())
                        .transmissionType(getRandomTransmissionTypes())
                        .engine(getRandomEngine())
                        .year(getRandomYear())
                        .shortDescription(getRandomShortDescription())
                        .description(getRandomDescription())
                        .options(List.of(
                                getRandomOption(),
                                getRandomOption(),
                                getRandomOption()))
                        .build());
    }

    private void addCarWithImageCase(CarValue carValue) {
        String carTitle = getTitleCar(carValue.brand, carValue.model);
        String carBodyType = carValue.bodyType.stringValue;
        String carTransmission = carValue.transmissionType.stringValue;
        String carEngine = getEngineDetailsPage(carValue.engine);
        List<String> carOptions = getOptionsDetailsPage(carValue.options);

        //Adding a car
        addCarPage.openAddCarPage()
                .fillForm(carValue)
                .clickSaveButton();

        try {
            //Check message
            assertTrue(catalogAuthPage.isToastDisplayed());
            assertEquals(TITLE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_ADD));
            assertEquals(MESSAGE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_ADD));
            catalogAuthPage.clickCloseToastButton();

            //Added car check in catalog
            assertNotEquals(DEFAULT_CAR_IMAGE_SRC, catalogAuthPage.getImageSrcFirstCar());
            assertTrue(isImagesEquals(carValue.imageName, catalogAuthPage.getImageSrcFirstCar()));
            assertEquals(carTitle, catalogAuthPage.getTitleFirstCar(carTitle));
            assertEquals(carValue.shortDescription, catalogAuthPage.getShortDescriptionFirstCar(carValue.shortDescription));

            //Added car check in details page
            catalogAuthPage.clickFirstCarImage();
            assertNotEquals(DEFAULT_CAR_IMAGE_SRC, detailsCarPage.getImageSrc());
            assertTrue(isImagesEquals(carValue.imageName, detailsCarPage.getImageSrc()));
            assertEquals(carTitle, detailsCarPage.getTitleCar(carTitle));
            assertEquals(carBodyType, detailsCarPage.getBodyTypeCar(carBodyType));
            assertEquals(carTransmission, detailsCarPage.getTransmissionTypeCar(carTransmission));
            assertEquals(carEngine, detailsCarPage.getEngineCar(carEngine));
            assertEquals(carValue.shortDescription, detailsCarPage.getShortDescriptionCar(carValue.shortDescription));
            assertEquals(carValue.description, detailsCarPage.getDescriptionCar(carValue.description));
            assertEquals(carOptions, detailsCarPage.getAllOptions());
        } finally {
            //Delete car from catalog
            detailsCarPage
                    .clickLogo()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton()
                    .clickAddCarButton();
        }
    }

    private void addExistCar(CarValue carValue) {
        String oldImageUrl;
        String carTitle = getTitleCar(CAR_FOR_EXIST_CAR_TEST.brand, CAR_FOR_EXIST_CAR_TEST.model);
        String carBodyType = CAR_FOR_EXIST_CAR_TEST.bodyType.stringValue;
        String carEngine = getEngineDetailsPage(CAR_FOR_EXIST_CAR_TEST.engine);
        String carTransmission = CAR_FOR_EXIST_CAR_TEST.transmissionType.stringValue;
        List<String> carOptions = getOptionsDetailsPage(CAR_FOR_EXIST_CAR_TEST.options);

        //Adding a cars
        addCarPage.openAddCarPage().fillForm(carValue).clickSaveButton().clickCloseToastButton();
        addCarPage.openAddCarPage().fillForm(CAR_FOR_EXIST_CAR_TEST).clickSaveButton().clickCloseToastButton();
        oldImageUrl = catalogAuthPage.getImageSrcFirstCar();
        addCarPage.openAddCarPage().fillForm(carValue).clickSaveButton();

        try {
            //Check message
            assertTrue(catalogAuthPage.isToastDisplayed());
            assertEquals(TITLE_EXIST_CAR, addCarPage.getTitleToast(TITLE_EXIST_CAR));
            assertEquals(MESSAGE_EXIST_CAR, addCarPage.getMessageToast(MESSAGE_EXIST_CAR));
            addCarPage.clickCloseToastButton().clickLogo();

            //Check car not add in catalog
            assertEquals(oldImageUrl, catalogAuthPage.getImageSrcFirstCar());
            assertTrue(isImagesEqualsByUrl(oldImageUrl, catalogAuthPage.getImageSrcFirstCar()));
            assertEquals(carTitle, catalogAuthPage.getTitleFirstCar(carTitle));
            assertEquals(CAR_FOR_EXIST_CAR_TEST.shortDescription, catalogAuthPage.getShortDescriptionFirstCar(CAR_FOR_EXIST_CAR_TEST.shortDescription));

            //Check details page
            catalogAuthPage.clickFirstCarImage();
            assertEquals(oldImageUrl, detailsCarPage.getImageSrc());
            assertTrue(isImagesEqualsByUrl(oldImageUrl, detailsCarPage.getImageSrc()));
            assertEquals(carTitle, detailsCarPage.getTitleCar(carTitle));
            assertEquals(carBodyType, detailsCarPage.getBodyTypeCar(carBodyType));
            assertEquals(carTransmission, detailsCarPage.getTransmissionTypeCar(carTransmission));
            assertEquals(carEngine, detailsCarPage.getEngineCar(carEngine));
            assertEquals(CAR_FOR_EXIST_CAR_TEST.shortDescription, detailsCarPage.getShortDescriptionCar(CAR_FOR_EXIST_CAR_TEST.shortDescription));
            assertEquals(CAR_FOR_EXIST_CAR_TEST.description, detailsCarPage.getDescriptionCar(CAR_FOR_EXIST_CAR_TEST.description));
            assertEquals(carOptions, detailsCarPage.getAllOptions());
        } finally {
            //Delete car from catalog
            catalogAuthPage.clickLogo()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton();
        }
    }

    private void addCarWithoutImageCase(CarValue carValue) {
        String carTitle = getTitleCar(carValue.brand, carValue.model);
        String carEngine = getEngineDetailsPage(carValue.engine);
        String carBodyType = carValue.bodyType.stringValue;
        String carTransmission = carValue.transmissionType.stringValue;
        List<String> carOptions = getOptionsDetailsPage(carValue.options);

        //Adding a car
        addCarPage.openAddCarPage()
                .fillForm(carValue)
                .clickSaveButton();

        try {
            //Check message
            assertTrue(catalogAuthPage.isToastDisplayed());
            assertEquals(TITLE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getTitleToast(TITLE_SUCCESSFULLY_CAR_ADD));
            assertEquals(MESSAGE_SUCCESSFULLY_CAR_ADD, catalogAuthPage.getMessageToast(MESSAGE_SUCCESSFULLY_CAR_ADD));
            catalogAuthPage.clickCloseToastButton();

            //Added car check in catalog
            assertEquals(DEFAULT_CAR_IMAGE_SRC, catalogAuthPage.getImageSrcFirstCar());
            assertTrue(isImagesEqualsByUrl(DEFAULT_CAR_IMAGE_SRC, catalogAuthPage.getImageSrcFirstCar()));
            assertEquals(carTitle, catalogAuthPage.getTitleFirstCar(carTitle));
            assertEquals(carValue.shortDescription, catalogAuthPage.getShortDescriptionFirstCar(carValue.shortDescription));

            //Added car check in details page
            catalogAuthPage.clickFirstCarImage();
            assertEquals(DEFAULT_CAR_IMAGE_SRC, detailsCarPage.getImageSrc());
            assertTrue(isImagesEqualsByUrl(DEFAULT_CAR_IMAGE_SRC, detailsCarPage.getImageSrc()));
            assertEquals(carTitle, detailsCarPage.getTitleCar(carTitle));
            assertEquals(carBodyType, detailsCarPage.getBodyTypeCar(carBodyType));
            assertEquals(carTransmission, detailsCarPage.getTransmissionTypeCar(carTransmission));
            assertEquals(carEngine, detailsCarPage.getEngineCar(carEngine));
            assertEquals(carValue.shortDescription, detailsCarPage.getShortDescriptionCar(carValue.shortDescription));
            assertEquals(carValue.description, detailsCarPage.getDescriptionCar(carValue.description));
            assertEquals(carOptions, detailsCarPage.getAllOptions());
        } finally {
            //Delete car from catalog
            detailsCarPage
                    .clickLogo()
                    .clickFirstCarDeleteButton()
                    .clickFirstCarDeleteModalConfirm()
                    .clickCloseToastButton()
                    .clickAddCarButton();
        }
    }
}
