package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class ConfigReader {

  private final Properties properties;
  private final Function<String, String> envLookup;

  public ConfigReader(Properties properties, Function<String, String> envLookup) {
    this.properties = properties;
    this.envLookup = envLookup;
  }

  public ConfigReader(String path) {
    this(loadProperties(path), System::getenv);
  }

  public String get(String key) {
    String env = envLookup.apply(key);
    if (env != null) {
      return env;
    }
    return properties.getProperty(key);
  }

  private static Properties loadProperties(String path) {
    Properties props = new Properties();
    try (FileInputStream file = new FileInputStream(path)) {
      props.load(file);
    } catch (IOException e) {
      throw new RuntimeException("Could not load config from " + path, e);
    }
    return props;
  }
}
