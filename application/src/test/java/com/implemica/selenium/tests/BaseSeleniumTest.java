package com.implemica.selenium.tests;

import com.implemica.model.enums.CarBodyType;
import com.implemica.model.enums.CarBrand;
import com.implemica.model.enums.CarTransmissionType;
import com.implemica.selenium.pages.BaseSeleniumPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.implemica.selenium.helpers.BaseTestValues.*;
import static org.junit.Assert.fail;

public class BaseSeleniumTest {
    protected static WebDriver driver;
    protected static WebDriverWait webDriverWait;
    protected static final DataFactory dataFactory = new DataFactory();

    @BeforeClass
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));

        BaseSeleniumPage.setDriver(driver);

        driver.get(BASE_URL);
        webDriverWait.until(ExpectedConditions.urlToBe(BASE_URL));
    }

    @AfterClass
    public static void close() {
        driver.close();
        driver.quit();
    }

    protected static String toTitleCase(String str) {
        String result = "";
        String[] worlds = str.split("_");
        String firstChar;
        String restPart;

        for (String world : worlds) {
            firstChar = Character.toString(world.charAt(0));
            restPart = world.substring(1).toLowerCase();
            result += " " + firstChar + restPart;
        }

        return result.trim();
    }

    protected static String getEngineDetailsPage(String engine) {
        if (engine == null) {
            return null;
        }

        return Double.parseDouble(engine) == 0 ? ELECTRIC : String.format(FORMAT_ENGINE_FOR_DETAILS_PAGE, engine)
                .replaceAll("\\.0", "");
    }

    protected static java.util.List<String> getOptionsDetailsPage(java.util.List<String> addedOptions) {

        return addedOptions != null ? Stream.concat(DEFAULT_OPTIONS.stream(), addedOptions.stream())
                .sorted(String::compareTo).collect(Collectors.toList()) : DEFAULT_OPTIONS.stream().sorted(String::compareTo).collect(Collectors.toList());
    }

    public String getEditUrlByDetailsUrl(String detailsUrl) {
        String[] splitDetailsUrl = detailsUrl.split("/");
        String id = splitDetailsUrl[splitDetailsUrl.length - 1];

        return String.format(EDIT_URL_REG_FORMAT, id);
    }

    public String getEditTitleByDetailsTitle(String detailsUrl) {

        return detailsUrl.replaceFirst(TITLE_PART_DETAILS, TITLE_PART_EDIT);
    }

    protected static String getTitleCar(CarBrand brand, String model) {
        return brand.stringValue + " " + model;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getCurrentTitle() {
        return driver.getTitle();
    }

    public CarBrand getRandomBrand() {
        CarBrand[] carBrands = CarBrand.values();

        return carBrands[dataFactory.getNumberBetween(0, carBrands.length - 1)];
    }

    public CarBodyType getRandomBodyTypes() {
        CarBodyType[] carBodyTypes = CarBodyType.values();

        return carBodyTypes[dataFactory.getNumberBetween(0, carBodyTypes.length - 1)];
    }

    public String getRandomModel() {
        return getRandomTextBetween(2, 40);
    }

    public String getRandomYear() {
        return Integer.toString(dataFactory.getNumberBetween(1880, 2100));
    }

    public String getRandomEngine() {
        return Double.toString(dataFactory.getNumberBetween(0, 100) / 10);
    }

    public String getRandomText(int length) {
        return dataFactory.getRandomText(length);
    }

    public String getRandomTextBetween(int from, int to) {
        return dataFactory.getRandomText(from, to);
    }

    public String getRandomShortDescription() {
        return getRandomTextBetween(25, 150);
    }

    public String getRandomDescription() {
        return getRandomTextBetween(50, 5000);
    }


    public String getRandomOption() {
        return getRandomTextBetween(2, 25);
    }

    public CarTransmissionType getRandomTransmissionTypes() {
        CarTransmissionType[] carTransmissionTypes = CarTransmissionType.values();

        return carTransmissionTypes[carTransmissionTypes.length - 1];
    }

    public Boolean isImagesEquals(String expectedImagePath, String actualImageUrl) {
        Boolean isImagesEquals = false;
        try {
            BufferedImage expectedImage = ImageIO.read(new File(expectedImagePath));
            BufferedImage actualImage = ImageIO.read(new URL(actualImageUrl));

            int weightExpected = expectedImage.getWidth();
            int weightActual = actualImage.getWidth();
            int heightExpected = expectedImage.getHeight();
            int heightActual = actualImage.getHeight();

            if ((weightExpected == weightActual) || (heightExpected == heightActual)) {
                long diff = 0;

                for (int j = 0; j < heightExpected; j++) {
                    for (int i = 0; i < weightExpected; i++) {
                        //Getting the RGB values of a pixel
                        int pixelExpected = expectedImage.getRGB(i, j);
                        Color color1 = new Color(pixelExpected, true);
                        int redExpected = color1.getRed();
                        int greenExpected = color1.getGreen();
                        int blueExpected = color1.getBlue();

                        int pixel2 = actualImage.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int redActual = color2.getRed();
                        int greenActual = color2.getGreen();
                        int blueActual = color2.getBlue();

                        //sum of differences of RGB values of the two images
                        long data = Math.abs(redExpected - redActual)
                                + Math.abs(greenExpected - greenActual)
                                + Math.abs(blueExpected - blueActual);
                        diff = diff + data;
                    }
                }

                isImagesEquals = diff == 0;
            }
        } catch (IOException e) {
            fail();
        }

        return isImagesEquals;
    }

    public Boolean isImagesEquals(BufferedImage expectedImage, String actualImageUrl) {
        Boolean isImagesEquals = false;
        try {
            BufferedImage actualImage = ImageIO.read(new URL(actualImageUrl));

            int weightExpected = expectedImage.getWidth();
            int weightActual = actualImage.getWidth();
            int heightExpected = expectedImage.getHeight();
            int heightActual = actualImage.getHeight();

            if ((weightExpected == weightActual) || (heightExpected == heightActual)) {
                long diff = 0;

                for (int j = 0; j < heightExpected; j++) {
                    for (int i = 0; i < weightExpected; i++) {
                        //Getting the RGB values of a pixel
                        int pixelExpected = expectedImage.getRGB(i, j);
                        Color color1 = new Color(pixelExpected, true);
                        int redExpected = color1.getRed();
                        int greenExpected = color1.getGreen();
                        int blueExpected = color1.getBlue();

                        int pixel2 = actualImage.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int redActual = color2.getRed();
                        int greenActual = color2.getGreen();
                        int blueActual = color2.getBlue();

                        //sum of differences of RGB values of the two images
                        long data = Math.abs(redExpected - redActual)
                                + Math.abs(greenExpected - greenActual)
                                + Math.abs(blueExpected - blueActual);
                        diff = diff + data;
                    }
                }

                isImagesEquals = diff == 0;
            }
        } catch (IOException e) {
            fail();
        }

        return isImagesEquals;
    }

    public String getFirstNameMore4Chars(){
        return Stream.generate(() -> dataFactory.getFirstName()).filter(name -> name.length() > 4).findFirst().orElseThrow();
    }

    public Boolean isImagesEqualsByUrl(String expectedImageUrl, String actualImageUrl) {
        Boolean isImagesEquals = false;
        try {
            BufferedImage expectedImage = ImageIO.read(new URL(expectedImageUrl));
            BufferedImage actualImage = ImageIO.read(new URL(actualImageUrl));

            int weightExpected = expectedImage.getWidth();
            int weightActual = actualImage.getWidth();
            int heightExpected = expectedImage.getHeight();
            int heightActual = actualImage.getHeight();

            if ((weightExpected == weightActual) || (heightExpected == heightActual)) {
                long diff = 0;

                for (int j = 0; j < heightExpected; j++) {
                    for (int i = 0; i < weightExpected; i++) {
                        //Getting the RGB values of a pixel
                        int pixelExpected = expectedImage.getRGB(i, j);
                        Color color1 = new Color(pixelExpected, true);
                        int redExpected = color1.getRed();
                        int greenExpected = color1.getGreen();
                        int blueExpected = color1.getBlue();

                        int pixel2 = actualImage.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int redActual = color2.getRed();
                        int greenActual = color2.getGreen();
                        int blueActual = color2.getBlue();

                        //sum of differences of RGB values of the two images
                        long data = Math.abs(redExpected - redActual)
                                + Math.abs(greenExpected - greenActual)
                                + Math.abs(blueExpected - blueActual);
                        diff = diff + data;
                    }
                }

                isImagesEquals = diff == 0;
            }
        } catch (IOException e) {
            fail();
        }

        return isImagesEquals;
    }

    public List<String> sortOptions(java.util.List<String> optionsList){
        return optionsList.stream().sorted(String::compareTo).collect(Collectors.toList());
    }
}
