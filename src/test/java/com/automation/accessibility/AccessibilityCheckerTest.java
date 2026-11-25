package com.automation.accessibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.automation.utils.ConfigReader;
import com.deque.html.axecore.results.CheckedNode;
import com.deque.html.axecore.results.Rule;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Unit tests for AccessibilityChecker */
class AccessibilityCheckerTest {

  private static final String PAGE = "login";

  private static Violation violation(String ruleId, Impact impact) {
    return new Violation(
        ruleId, impact, "description", "https://dequeuniversity.com/rules/axe", List.of("#id"));
  }

  private static AccessibilityChecker checker(
      List<Violation> scanned, AllowlistEntry... allowlist) {
    return new AccessibilityChecker(
        () -> scanned, new ViolationFilter(Impact.SERIOUS, List.of(allowlist)));
  }

  private static ConfigReader config(Map<String, String> values) {
    Properties properties = new Properties();
    properties.putAll(values);
    return new ConfigReader(properties, key -> null);
  }

  private static CheckedNode node(Object target) {
    CheckedNode node = new CheckedNode();
    node.setTarget(target);
    return node;
  }

  @Test
  void wcagTags_shouldCoverWcag22AaAndBelow() {
    assertThat(AccessibilityChecker.WCAG_TAGS)
        .containsExactly("wcag2a", "wcag2aa", "wcag21aa", "wcag22aa");
  }

  @Test
  void failLevel_shouldReadConfiguredValue() {
    assertThat(AccessibilityChecker.failLevel(config(Map.of("A11Y_FAIL_LEVEL", "moderate"))))
        .isEqualTo(Impact.MODERATE);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"  "})
  void failLevel_shouldDefaultToSeriousWhenMissingOrBlank(String value) {
    Map<String, String> values = value == null ? Map.of() : Map.of("A11Y_FAIL_LEVEL", value);

    assertThat(AccessibilityChecker.failLevel(config(values))).isEqualTo(Impact.SERIOUS);
  }

  @Test
  void failLevel_shouldThrowForInvalidValueNamingTheKey() {
    assertThatThrownBy(
            () -> AccessibilityChecker.failLevel(config(Map.of("A11Y_FAIL_LEVEL", "severe"))))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("A11Y_FAIL_LEVEL")
        .hasMessageContaining("severe");
  }

  @Test
  void toViolation_shouldMapAxeRule() {
    Rule rule = new Rule();
    rule.setId("color-contrast");
    rule.setImpact("serious");
    rule.setDescription("Ensures sufficient colour contrast");
    rule.setHelpUrl("https://dequeuniversity.com/rules/axe/4.10/color-contrast");
    rule.setNodes(List.of(node(List.of("#username")), node(List.of(".error"))));

    assertThat(AccessibilityChecker.toViolation(rule))
        .isEqualTo(
            new Violation(
                "color-contrast",
                Impact.SERIOUS,
                "Ensures sufficient colour contrast",
                "https://dequeuniversity.com/rules/axe/4.10/color-contrast",
                List.of("#username", ".error")));
  }

  @Test
  void check_shouldReturnFilterResultWhenNothingBlocks() {
    Violation minor = violation("region", Impact.MINOR);

    assertThat(checker(List.of(minor)).check(PAGE))
        .isEqualTo(new FilterResult(List.of(), List.of(minor), List.of()));
  }

  @Test
  void check_shouldNotThrowWhenBlockingViolationIsAllowlisted() {
    Violation critical = violation("label", Impact.CRITICAL);
    AllowlistEntry entry = new AllowlistEntry("label", PAGE, "Known issue");

    assertThat(checker(List.of(critical), entry).check(PAGE))
        .isEqualTo(new FilterResult(List.of(), List.of(), List.of(critical)));
  }

  @Test
  void check_shouldThrowAssertionErrorNamingPageAndBlockingRules() {
    List<Violation> scanned =
        List.of(violation("label", Impact.CRITICAL), violation("image-alt", Impact.SERIOUS));

    assertThatThrownBy(() -> checker(scanned).check(PAGE))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining(PAGE)
        .hasMessageContaining("label")
        .hasMessageContaining("image-alt");
  }

  @Test
  void check_shouldNotNameReportOnlyRulesInFailureMessage() {
    List<Violation> scanned =
        List.of(violation("label", Impact.CRITICAL), violation("region", Impact.MINOR));

    assertThatThrownBy(() -> checker(scanned).check(PAGE))
        .isInstanceOf(AssertionError.class)
        .hasMessageNotContaining("region");
  }

  @Test
  void report_shouldListEveryViolationWithAllDetailsGroupedByOutcome() {
    Violation blocking =
        new Violation(
            "label",
            Impact.CRITICAL,
            "Form elements must have labels",
            "https://dequeuniversity.com/rules/axe/4.10/label",
            List.of("#user-name", "#password"));
    Violation reportOnly = violation("region", Impact.MINOR);
    Violation allowlisted = violation("color-contrast", Impact.SERIOUS);

    String report =
        AccessibilityChecker.report(
            new FilterResult(List.of(blocking), List.of(reportOnly), List.of(allowlisted)));

    assertThat(report)
        .contains("Blocking (1)", "Report only (1)", "Allowlisted (1)")
        .contains(
            "label (critical)",
            "Form elements must have labels",
            "https://dequeuniversity.com/rules/axe/4.10/label",
            "Selectors: #user-name, #password")
        .contains("region (minor)", "color-contrast (serious)");
  }

  @Test
  void report_shouldShowEmptyGroupsWhenNoViolations() {
    assertThat(AccessibilityChecker.report(new FilterResult(List.of(), List.of(), List.of())))
        .contains("Blocking (0)", "Report only (0)", "Allowlisted (0)");
  }

  @Test
  void constructor_shouldThrowForNullScanner() {
    assertThatThrownBy(
            () -> new AccessibilityChecker(null, new ViolationFilter(Impact.SERIOUS, List.of())))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("scanner");
  }

  @Test
  void constructor_shouldThrowForNullFilter() {
    assertThatThrownBy(() -> new AccessibilityChecker(List::of, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("filter");
  }
}
