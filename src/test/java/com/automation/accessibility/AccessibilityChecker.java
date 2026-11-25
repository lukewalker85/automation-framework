package com.automation.accessibility;

import com.automation.utils.ConfigReader;
import com.deque.html.axecore.results.Rule;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Runs axe-core against the current page, sorts the violations with a {@link ViolationFilter} and
 * fails the test when any violation is blocking.
 */
public class AccessibilityChecker {

  /** The axe-core tags scanned for: WCAG 2.0 A and AA, 2.1 AA and 2.2 AA. */
  public static final List<String> WCAG_TAGS = List.of("wcag2a", "wcag2aa", "wcag21aa", "wcag22aa");

  private static final String FAIL_LEVEL_KEY = "A11Y_FAIL_LEVEL";

  private final Supplier<List<Violation>> scanner;
  private final ViolationFilter filter;

  /**
   * @param scanner supplies the violations found on the current page
   * @param filter sorts the violations into blocking, report-only and allowlisted
   * @throws NullPointerException if either argument is null
   */
  public AccessibilityChecker(Supplier<List<Violation>> scanner, ViolationFilter filter) {
    this.scanner = Objects.requireNonNull(scanner, "scanner must not be null");
    this.filter = Objects.requireNonNull(filter, "filter must not be null");
  }

  /**
   * Reads the minimum failing impact from {@code A11Y_FAIL_LEVEL}, defaulting to {@link
   * Impact#SERIOUS} when it is missing or blank.
   *
   * @param config the configuration to read from
   * @return the configured fail level
   * @throws IllegalArgumentException if the configured value is not a known impact
   */
  public static Impact failLevel(ConfigReader config) {
    String value = config.get(FAIL_LEVEL_KEY);
    if (value == null || value.isBlank()) {
      return Impact.SERIOUS;
    }
    try {
      return Impact.from(value);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(FAIL_LEVEL_KEY + ": " + e.getMessage(), e);
    }
  }

  /**
   * Maps an axe-core rule result to a {@link Violation}, taking one selector per affected node.
   *
   * @param rule the rule result reported by axe-core
   * @return the equivalent violation
   */
  static Violation toViolation(Rule rule) {
    List<String> selectors =
        rule.getNodes().stream().map(node -> selector(node.getTarget())).toList();
    return new Violation(
        rule.getId(),
        Impact.from(rule.getImpact()),
        rule.getDescription(),
        rule.getHelpUrl(),
        selectors);
  }

  private static String selector(Object target) {
    if (target instanceof List<?> parts) {
      return parts.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }
    return String.valueOf(target);
  }

  /**
   * Scans the current page and filters the violations found.
   *
   * @param page the page being checked, matched against the allowlist
   * @return the filtered violations
   * @throws AssertionError if any violation is blocking, naming the page and the blocking rules
   */
  public FilterResult check(String page) {
    FilterResult result = filter.apply(page, scanner.get());
    if (result.hasBlocking()) {
      throw new AssertionError(failureMessage(page, result.blocking()));
    }
    return result;
  }

  private static String failureMessage(String page, List<Violation> blocking) {
    return blocking.size()
        + " blocking accessibility violation(s) on page '"
        + page
        + "':"
        + blocking.stream()
            .map(
                v ->
                    "\n  - "
                        + v.ruleId()
                        + " ("
                        + v.impact().name().toLowerCase(Locale.ROOT)
                        + "): "
                        + v.description()
                        + " "
                        + v.helpUrl())
            .collect(Collectors.joining());
  }
}
