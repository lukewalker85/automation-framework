package com.automation.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for ConfigReader */
class ConfigReaderTest {

  @BeforeEach
  void clearLogLevel() {
    System.clearProperty("logLevel");
  }

  @AfterEach
  void resetLogLevel() {
    System.clearProperty("logLevel");
  }

  @Test
  void shouldReturnPropertyValue() {
    Properties props = new Properties();
    props.setProperty("BROWSER", "FIREFOX");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);
    String result = config.get("BROWSER");
    assertThat(result).isEqualTo("FIREFOX");
  }

  @Test
  void shouldReturnNullForMissingProperty() {
    Properties props = new Properties();
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);
    String result = config.get("MISSINGPROP");
    assertThat(result).isNull();
  }

  @Test
  void shouldReturnEnvVarOverProperty() {
    Properties props = new Properties();
    props.setProperty("BROWSER", "FIREFOX");
    Function<String, String> alwaysChrome = key -> key.equals("BROWSER") ? "CHROME" : null;
    ConfigReader config = new ConfigReader(props, alwaysChrome);
    String result = config.get("BROWSER");
    assertThat(result).isEqualTo("CHROME");
  }

  @Test
  void shouldUseInjectedEnvLookup() {
    Properties props = new Properties();
    Function<String, String> fakeEnv = key -> "injected-value";
    ConfigReader config = new ConfigReader(props, fakeEnv);
    String result = config.get("PATH");
    assertThat(result).isEqualTo("injected-value");
  }

  @Test
  void shouldFallBackToPropertyWhenEnvVarIsEmpty() {
    Properties props = new Properties();
    props.setProperty("KEY", "VALUE");
    Function<String, String> emptyString = key -> key.equals("KEY") ? "" : null;
    ConfigReader config = new ConfigReader(props, emptyString);
    String result = config.get("KEY");
    assertThat(result).isEqualTo("VALUE");
  }

  @Test
  void constructor_shouldApplyLogLevelFromClasspath() {
    new ConfigReader("config.properties");
    assertThat(System.getProperty("logLevel")).isNotNull();
  }

  @Test
  void applyLogLevel_shouldTrimWhitespace() {
    Properties props = new Properties();
    props.setProperty("LOG_LEVEL", "  debug  ");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    config.applyLogLevel();

    assertThat(System.getProperty("logLevel")).isEqualTo("DEBUG");
  }

  @Test
  void applyLogLevel_shouldNormaliseToUpperCase() {
    Properties props = new Properties();
    props.setProperty("LOG_LEVEL", "WaRn");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    config.applyLogLevel();

    assertThat(System.getProperty("logLevel")).isEqualTo("WARN");
  }

  @Test
  void applyLogLevel_shouldDefaultToInfoWhenMissing() {
    Properties props = new Properties();
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    config.applyLogLevel();

    assertThat(System.getProperty("logLevel")).isEqualTo("INFO");
  }

  @Test
  void applyLogLevel_shouldDefaultToInfoWhenEmpty() {
    Properties props = new Properties();
    props.setProperty("LOG_LEVEL", "");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));
    try {
      config.applyLogLevel();
      String stderrOutput = errContent.toString();
      assertThat(System.getProperty("logLevel")).isEqualTo("INFO");
      assertThat(stderrOutput).contains("LOG_LEVEL not set, defaulting to INFO");
    } finally {
      System.setErr(originalErr);
    }
  }

  @Test
  void getInt_shouldReturnParsedValue() {
    Properties props = new Properties();
    props.setProperty("RETRY_COUNT", "3");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    assertThat(config.getInt("RETRY_COUNT", 0)).isEqualTo(3);
  }

  @Test
  void getInt_shouldReturnDefaultWhenMissing() {
    Properties props = new Properties();
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    assertThat(config.getInt("RETRY_COUNT", 2)).isEqualTo(2);
  }

  @Test
  void getInt_shouldReturnDefaultWhenBlank() {
    Properties props = new Properties();
    props.setProperty("RETRY_COUNT", "   ");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    assertThat(config.getInt("RETRY_COUNT", 2)).isEqualTo(2);
  }

  @Test
  void getInt_shouldThrowForInvalidValue() {
    Properties props = new Properties();
    props.setProperty("RETRY_COUNT", "not-a-number");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    assertThat(
            org.assertj.core.api.Assertions.catchThrowable(() -> config.getInt("RETRY_COUNT", 2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("RETRY_COUNT");
  }

  @Test
  void applyLogLevel_shouldWarnToStderrWhenMissing() {
    Properties props = new Properties();
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);

    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    PrintStream originalErr = System.err;
    System.setErr(new PrintStream(errContent));

    try {
      config.applyLogLevel();
      String stderrOutput = errContent.toString();
      assertThat(stderrOutput).contains("LOG_LEVEL not set, defaulting to INFO");
    } finally {
      System.setErr(originalErr);
    }
  }
}
