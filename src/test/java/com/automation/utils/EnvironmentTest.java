package com.automation.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/** Unit tests for Environment */
class EnvironmentTest {

  @Test
  void resolve_shouldDefaultToDevWhenNeitherSystemPropertyNorEnvVarSet() {
    assertThat(Environment.resolve(null, null)).isEqualTo(Environment.DEV);
  }

  @Test
  void resolve_shouldPreferSystemPropertyOverEnvVar() {
    assertThat(Environment.resolve("staging", "prod")).isEqualTo(Environment.STAGING);
  }

  @Test
  void resolve_shouldFallBackToEnvVarWhenSystemPropertyIsNull() {
    assertThat(Environment.resolve(null, "prod")).isEqualTo(Environment.PROD);
  }

  @Test
  void resolve_shouldFallBackToEnvVarWhenSystemPropertyIsBlank() {
    assertThat(Environment.resolve("   ", "prod")).isEqualTo(Environment.PROD);
  }

  @Test
  void resolve_shouldBeCaseInsensitiveAndTrimWhitespace() {
    assertThat(Environment.resolve(" STAGING ", null)).isEqualTo(Environment.STAGING);
  }

  @Test
  void resolve_shouldThrowForUnknownEnvironment() {
    assertThatThrownBy(() -> Environment.resolve("qa", null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("qa");
  }

  @Test
  void getConfigFile_shouldMapEachEnvironmentToItsOwnPropertiesFile() {
    assertThat(Environment.DEV.getConfigFile()).isEqualTo("config-dev.properties");
    assertThat(Environment.STAGING.getConfigFile()).isEqualTo("config-staging.properties");
    assertThat(Environment.PROD.getConfigFile()).isEqualTo("config-prod.properties");
  }
}
