package com.automation.accessibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Unit tests for Impact */
class ImpactTest {

  @Test
  void values_shouldBeOrderedFromLowestToHighestImpact() {
    assertThat(Impact.values())
        .containsExactly(Impact.MINOR, Impact.MODERATE, Impact.SERIOUS, Impact.CRITICAL);
  }

  @ParameterizedTest
  @CsvSource({"minor, MINOR", "moderate, MODERATE", "serious, SERIOUS", "critical, CRITICAL"})
  void from_shouldParseEachAxeImpactValue(String value, Impact expected) {
    assertThat(Impact.from(value)).isEqualTo(expected);
  }

  @Test
  void from_shouldBeCaseInsensitiveAndTrimWhitespace() {
    assertThat(Impact.from(" SERIOUS ")).isEqualTo(Impact.SERIOUS);
  }

  @Test
  void from_shouldThrowForUnknownImpact() {
    assertThatThrownBy(() -> Impact.from("severe"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("severe")
        .hasMessageContaining("minor, moderate, serious, critical");
  }

  @Test
  void isAtLeast_shouldThrowForNullThreshold() {
    assertThatThrownBy(() -> Impact.SERIOUS.isAtLeast(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("threshold");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  "})
  void from_shouldThrowForNullOrBlank(String value) {
    assertThatThrownBy(() -> Impact.from(value)).isInstanceOf(IllegalArgumentException.class);
  }

  @ParameterizedTest
  @CsvSource({"CRITICAL, SERIOUS", "SERIOUS, SERIOUS"})
  void isAtLeast_shouldBeTrueForSameOrHigherImpact(Impact impact, Impact threshold) {
    assertThat(impact.isAtLeast(threshold)).isTrue();
  }

  @ParameterizedTest
  @CsvSource({"MODERATE, SERIOUS", "MINOR, MODERATE"})
  void isAtLeast_shouldBeFalseForLowerImpact(Impact impact, Impact threshold) {
    assertThat(impact.isAtLeast(threshold)).isFalse();
  }
}
