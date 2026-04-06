package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static String get(String key) {
    String env = System.getenv(key);
    if (env != null) {
        return env;
    }
    return properties.getProperty(key);
}
    
}