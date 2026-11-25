package com.automation.accessibility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sorts a page's violations into blocking, report-only and allowlisted, based on a minimum failing
 * impact and an allowlist of accepted violations. The allowlist is checked before the fail level.
 */
public class ViolationFilter {

  private final Impact failLevel;
  private final List<AllowlistEntry> allowlist;

  /**
   * @param failLevel the minimum impact that fails the build
   * @param allowlist accepted violations, matched on rule ID and page
   * @throws NullPointerException if either argument is null
   */
  public ViolationFilter(Impact failLevel, List<AllowlistEntry> allowlist) {
    this.failLevel = Objects.requireNonNull(failLevel, "failLevel must not be null");
    this.allowlist = List.copyOf(Objects.requireNonNull(allowlist, "allowlist must not be null"));
  }

  /**
   * Filters the violations found on a page. Each bucket keeps the input order.
   *
   * @param page the page the violations were found on, matched exactly against the allowlist
   * @param violations the violations reported by axe-core
   * @return the violations sorted into blocking, report-only and allowlisted
   */
  public FilterResult apply(String page, List<Violation> violations) {
    List<Violation> blocking = new ArrayList<>();
    List<Violation> reportOnly = new ArrayList<>();
    List<Violation> allowlisted = new ArrayList<>();
    for (Violation violation : violations) {
      if (isAllowlisted(page, violation)) {
        allowlisted.add(violation);
      } else if (violation.impact().isAtLeast(failLevel)) {
        blocking.add(violation);
      } else {
        reportOnly.add(violation);
      }
    }
    return new FilterResult(
        List.copyOf(blocking), List.copyOf(reportOnly), List.copyOf(allowlisted));
  }

  private boolean isAllowlisted(String page, Violation violation) {
    return allowlist.stream()
        .anyMatch(entry -> entry.ruleId().equals(violation.ruleId()) && entry.page().equals(page));
  }
}
