package com.automation.accessibility;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Severity of an axe-core accessibility violation, ordered from lowest to highest so that impacts
 * can be compared against a configurable failure threshold (see {@link #isAtLeast(Impact)}).
 */
public enum Impact {
  MINOR,
  MODERATE,
  SERIOUS,
  CRITICAL;

  private static final String VALID_VALUES =
      Arrays.stream(values())
          .map(impact -> impact.name().toLowerCase(Locale.ROOT))
          .collect(Collectors.joining(", "));

  /**
   * Resolves an impact from the value reported by axe-core (e.g. {@code "serious"}). Matching is
   * case-insensitive and ignores surrounding whitespace.
   *
   * @param value the impact string reported by axe-core
   * @return the matching impact
   * @throws IllegalArgumentException if the value is null, blank or not a known impact
   */
  public static Impact from(String value) {
    String name = value == null ? "" : value.trim().toUpperCase(Locale.ROOT);
    for (Impact impact : values()) {
      if (impact.name().equals(name)) {
        return impact;
      }
    }
    throw new IllegalArgumentException(
        "Unknown impact: '" + value + "'. Valid values: " + VALID_VALUES);
  }

  /**
   * Whether this impact is the same as or more severe than the given threshold, e.g. {@code
   * CRITICAL.isAtLeast(SERIOUS)} is {@code true}.
   *
   * @param threshold the minimum impact to compare against
   * @return {@code true} if this impact meets or exceeds the threshold
   * @throws NullPointerException if the threshold is null
   */
  public boolean isAtLeast(Impact threshold) {
    Objects.requireNonNull(threshold, "threshold must not be null");
    return compareTo(threshold) >= 0;
  }
}
