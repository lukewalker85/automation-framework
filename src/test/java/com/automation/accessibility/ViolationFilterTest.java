package com.automation.accessibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Unit tests for ViolationFilter */
class ViolationFilterTest {

  private static final String PAGE = "login";

  private static Violation violation(String ruleId, Impact impact) {
    return new Violation(
        ruleId, impact, "description", "https://dequeuniversity.com/rules/axe", List.of("#id"));
  }

  private static ViolationFilter filter(Impact failLevel, AllowlistEntry... allowlist) {
    return new ViolationFilter(failLevel, List.of(allowlist));
  }

  @ParameterizedTest
  @EnumSource(
      value = Impact.class,
      names = {"SERIOUS", "CRITICAL"})
  void apply_shouldBlockViolationsAtOrAboveFailLevel(Impact impact) {
    Violation v = violation("color-contrast", impact);

    assertThat(filter(Impact.SERIOUS).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(v), List.of(), List.of()));
  }

  @ParameterizedTest
  @EnumSource(
      value = Impact.class,
      names = {"MINOR", "MODERATE"})
  void apply_shouldReportOnlyViolationsBelowFailLevel(Impact impact) {
    Violation v = violation("color-contrast", impact);

    assertThat(filter(Impact.SERIOUS).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(), List.of(v), List.of()));
  }

  @Test
  void apply_shouldHonourConfiguredFailLevel() {
    Violation v = violation("region", Impact.MINOR);

    assertThat(filter(Impact.MINOR).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(v), List.of(), List.of()));
  }

  @Test
  void apply_shouldAllowlistViolationMatchingRuleAndPage() {
    Violation v = violation("color-contrast", Impact.CRITICAL);
    AllowlistEntry entry = new AllowlistEntry("color-contrast", PAGE, "Brand colours, ticket #1");

    assertThat(filter(Impact.SERIOUS, entry).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(), List.of(), List.of(v)));
  }

  @Test
  void apply_shouldAllowlistBeforeApplyingFailLevel() {
    Violation v = violation("region", Impact.MINOR);
    AllowlistEntry entry = new AllowlistEntry("region", PAGE, "Legacy layout");

    assertThat(filter(Impact.SERIOUS, entry).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(), List.of(), List.of(v)));
  }

  @Test
  void apply_shouldNotAllowlistWhenPageDiffers() {
    Violation v = violation("color-contrast", Impact.CRITICAL);
    AllowlistEntry entry = new AllowlistEntry("color-contrast", "checkout", "Checkout only");

    assertThat(filter(Impact.SERIOUS, entry).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(v), List.of(), List.of()));
  }

  @Test
  void apply_shouldNotAllowlistWhenRuleDiffers() {
    Violation v = violation("color-contrast", Impact.CRITICAL);
    AllowlistEntry entry = new AllowlistEntry("label", PAGE, "Different rule");

    assertThat(filter(Impact.SERIOUS, entry).apply(PAGE, List.of(v)))
        .isEqualTo(new FilterResult(List.of(v), List.of(), List.of()));
  }

  @Test
  void apply_shouldSortMixedViolationsPreservingOrder() {
    Violation critical = violation("label", Impact.CRITICAL);
    Violation minor = violation("region", Impact.MINOR);
    Violation allowed = violation("color-contrast", Impact.SERIOUS);
    Violation serious = violation("image-alt", Impact.SERIOUS);
    Violation moderate = violation("heading-order", Impact.MODERATE);
    AllowlistEntry entry = new AllowlistEntry("color-contrast", PAGE, "Brand colours");

    assertThat(
            filter(Impact.SERIOUS, entry)
                .apply(PAGE, List.of(critical, minor, allowed, serious, moderate)))
        .isEqualTo(
            new FilterResult(
                List.of(critical, serious), List.of(minor, moderate), List.of(allowed)));
  }

  @Test
  void apply_shouldReturnEmptyResultForNoViolations() {
    assertThat(filter(Impact.SERIOUS).apply(PAGE, List.of()))
        .isEqualTo(new FilterResult(List.of(), List.of(), List.of()));
  }

  @Test
  void hasBlocking_shouldBeTrueWhenAnyViolationBlocks() {
    Violation v = violation("label", Impact.CRITICAL);

    assertThat(filter(Impact.SERIOUS).apply(PAGE, List.of(v)).hasBlocking()).isTrue();
  }

  @Test
  void hasBlocking_shouldBeFalseWhenOnlyReportOnlyOrAllowlisted() {
    Violation minor = violation("region", Impact.MINOR);
    Violation allowed = violation("label", Impact.CRITICAL);
    AllowlistEntry entry = new AllowlistEntry("label", PAGE, "Known issue");

    assertThat(filter(Impact.SERIOUS, entry).apply(PAGE, List.of(minor, allowed)).hasBlocking())
        .isFalse();
  }

  @Test
  void constructor_shouldThrowForNullFailLevel() {
    assertThatThrownBy(() -> new ViolationFilter(null, List.of()))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("failLevel");
  }

  @Test
  void constructor_shouldThrowForNullAllowlist() {
    assertThatThrownBy(() -> new ViolationFilter(Impact.SERIOUS, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("allowlist");
  }
}
