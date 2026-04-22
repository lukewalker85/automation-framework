package com.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;

public class ConfigReader {

  private final Properties properties;
  private final Function<String, String> envLookup;

  public ConfigReader(Properties properties, Function<String, String> envLookup) {
    this.properties = Objects.requireNonNull(properties, "properties must not be null");
    this.envLookup = Objects.requireNonNull(envLookup, "envLookup must not be null");
  }

  public ConfigReader(String classpathResource) {
    this(loadFromClasspath(classpathResource), System::getenv);
    applyLogLevel();
  }

  private static Properties loadFromClasspath(String resource) {
    Properties props = new Properties();
    try (InputStream stream = ConfigReader.class.getClassLoader().getResourceAsStream(resource)) {
      if (stream == null) {
        throw new RuntimeException("Resource not found on classpath: " + resource);
      }
      props.load(stream);
    } catch (IOException e) {
      throw new RuntimeException("Could not load config from " + resource, e);
    }
    return props;
  }

  public String get(String key) {
    String env = envLookup.apply(key);
    if (env != null && !env.isEmpty()) {
      return env;
    }
    return properties.getProperty(key);
  }

  final void applyLogLevel() {
    String logLevel = get("LOG_LEVEL");
    if (logLevel == null) {
      System.err.println("LOG_LEVEL not set, defaulting to INFO");
      logLevel = "INFO";
    }
    logLevel = logLevel.trim();
    if (logLevel.isEmpty()) {
      System.err.println("LOG_LEVEL not set, defaulting to INFO");
      logLevel = "INFO";
    }
    logLevel = logLevel.toUpperCase(Locale.ROOT);
    System.setProperty("logLevel", logLevel);
  }
}
