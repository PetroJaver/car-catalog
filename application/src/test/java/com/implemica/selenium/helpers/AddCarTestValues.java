package com.implemica.selenium.helpers;

import java.io.File;

public class AddCarTestValues extends BaseTestValues{
    public static final String INVALID_FILE_PATH = new File("src/test/resources/filesForSilenium/any.txt").getAbsolutePath();
    public static final String VALID_FILE_PATH = new File("src/test/resources/filesForSilenium/testCarImage.png").getAbsolutePath();

    public static final String INVALID_PATTERN = "Valid characters: numbers, latin alphabet characters, space, and hyphen!";

    public static final String INVALID_MIN_LENGTH_MODEL_OR_OPTION = "Min length 2 symbols!";

    public static final String INVALID_MAX_LENGTH_MODEL = "Max length 40 symbols!";

    public static final String INVALID_MIN_ENGINE = "Min engine size 0.1!";

    public static final String INVALID_MAX_ENGINE = "Max engine size 10!";

    public static final String DEFAULT_IMAGE_PATH = "http://localhost:4200/assets/add-image.png";

    public static final String NO_CHOOSE_BRAND = "Please choose brand!";

    public static final String NO_CHOOSE_BODY = "Please choose body!";

    public static final String INVALID_FILE_MESSAGE = "Please select correct image format!";

    public static final String INVALID_FILE_TITLE = "Invalid file";

    public static final String INVALID_MIN_YEAR = "Min year 1880!";

    public static final String INVALID_MAX_YEAR = "Max year 2100!";

    public static final String INVALID_MIN_SHORT_DESCRIPTION = "Min length 25 symbols!";

    public static final String INVALID_MAX_SHORT_DESCRIPTION = "Max length 150 symbols!";

    public static final String INVALID_MIN_DESCRIPTION = "Min length 50 symbols!";

    public static final String INVALID_MAX_DESCRIPTION = "Max length 5000 symbols!";

    public static final String INVALID_MIN_LENGTH_OPTION = "Max length 25 symbols!";

    public static final String VALID_OPTION = "Any car option";

    public static final String MODEL_FOR_ADD = "911";

    public static final String ENGINE_FOR_ADD = "5.1";

    public static final String YEAR_FOR_ADD = "2018";

    public static final String SHORT_DESCRIPTION_FOR_ADD = "Maecenas a suscipit sapien. Sed ut lorem mattis, molestie erat";

    public static final String DESCRIPTION_FOR_ADD = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam pharetra iaculis urna nec porttitor. Aliquam iaculis ac tortor sed posuere. Praesent pretium magna erat, quis tempor magna venenatis tempus. Duis massa lorem, viverra ac euismod et, elementum quis justo. In facilisis dui sit amet vehicula pharetra. Cras pellentesque malesuada diam eget lacinia. Pellentesque ex dolor, tempus quis cursus nec, pharetra ac turpis.";
}
