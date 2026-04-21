package com.automation.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class ConfigReaderTest {

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
}
