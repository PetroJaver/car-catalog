package com.implemica.application.util.helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class HelpMethods {
    public static byte[] readFileToByteArray(String filePath) {
        byte[] file;

        try {
            file = FileUtils.readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            file = null;
        }

        return file;
    }
}
