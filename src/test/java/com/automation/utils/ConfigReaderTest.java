package com.automation.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class ConfigReaderTest {

  // Test 1: valid property returns expected value
  @Test
  void shouldReturnPropertyValue() {
    Properties props = new Properties();
    props.setProperty("BROWSER", "FIREFOX");
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);
    String result = config.get("BROWSER");
    assertThat(result).isEqualTo("FIREFOX");
  }

  // Test 2: missing property returns null
  @Test
  void shouldReturnNullForMissingProperty() {
    Properties props = new Properties();
    Function<String, String> noEnv = key -> null;
    ConfigReader config = new ConfigReader(props, noEnv);
    String result = config.get("MISSINGPROP");
    assertThat(result).isNull();
  }

  // Test 3: env var overrides property
  @Test
  void shouldReturnEnvVarOverProperty() {
    Properties props = new Properties();
    props.setProperty("BROWSER", "FIREFOX");
    Function<String, String> alwaysChrome = key -> key.equals("BROWSER") ? "CHROME" : null;
    ConfigReader config = new ConfigReader(props, alwaysChrome);
    String result = config.get("BROWSER");
    assertThat(result).isEqualTo("CHROME");
  }

  // Test 4: env lookup uses the injected function
  @Test
  void shouldUseInjectedEnvLookup() {
    Properties props = new Properties();
    Function<String, String> fakeEnv = key -> "injected-value";
    ConfigReader config = new ConfigReader(props, fakeEnv);
    String result = config.get("PATH");
    assertThat(result).isEqualTo("injected-value");
  }
}
