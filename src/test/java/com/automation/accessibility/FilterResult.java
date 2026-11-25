package com.automation.accessibility;

import java.util.List;

/**
 * The outcome of filtering a page's violations.
 *
 * @param blocking violations at or above the fail level that should fail the build
 * @param reportOnly violations below the fail level that are reported but do not fail the build
 * @param allowlisted violations matched by an allowlist entry
 */
public record FilterResult(
    List<Violation> blocking, List<Violation> reportOnly, List<Violation> allowlisted) {

  /**
   * Whether any violation should fail the build.
   *
   * @return {@code true} if there is at least one blocking violation
   */
  public boolean hasBlocking() {
    return !blocking.isEmpty();
  }
}
