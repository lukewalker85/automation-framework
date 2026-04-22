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

  /**
   * Creates a ConfigReader from a classpath resource with system environment variable override.
   *
   * <p>Calls {@link #applyLogLevel()} to set the logLevel system property before any Logger is
   * created.
   */
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

  /**
   * Reads LOG_LEVEL from config, normalises it, and sets the logLevel system property for Log4j 2.
   *
   * <p>Trims whitespace and converts to uppercase (Locale.ROOT). If LOG_LEVEL is missing or empty,
   * defaults to INFO and emits a warning to stderr. Must be called before any Logger is created so
   * that log4j2.xml's {@code ${sys:logLevel:-INFO}} resolves correctly.
   */
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
