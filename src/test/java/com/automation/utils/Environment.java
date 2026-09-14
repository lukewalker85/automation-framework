package com.automation.utils;

import java.util.Locale;

/**
 * Supported test execution environments. Each environment maps to its own classpath configuration
 * file (see {@link #getConfigFile()}), enabling the same tests to run against different deployments
 * by selecting an environment rather than editing configuration.
 */
public enum Environment {
  DEV("config-dev.properties"),
  STAGING("config-staging.properties"),
  PROD("config-prod.properties");

  private final String configFile;

  Environment(String configFile) {
    this.configFile = configFile;
  }

  /**
   * The classpath resource name of this environment's properties file, e.g. {@code
   * config-dev.properties} for {@link #DEV}.
   */
  public String getConfigFile() {
    return configFile;
  }

  /**
   * Resolves the active environment from the {@code env} system property (settable via {@code
   * -Denv=<name>} on the command line), falling back to the {@code ENV} environment variable, then
   * defaulting to {@link #DEV} when neither is set.
   *
   * @throws IllegalArgumentException if the resolved value is not a known environment
   */
  public static Environment resolve() {
    return resolve(System.getProperty("env"), System.getenv("ENV"));
  }

  static Environment resolve(String systemProperty, String envVar) {
    String value = systemProperty;
    if (value == null || value.isBlank()) {
      value = envVar;
    }
    if (value == null || value.isBlank()) {
      return DEV;
    }
    try {
      return Environment.valueOf(value.trim().toUpperCase(Locale.ROOT));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Unknown environment: '" + value + "'. Valid values: dev, staging, prod", e);
    }
  }
}
