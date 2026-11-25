package com.automation.accessibility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Unit tests for AllowlistEntry */
class AllowlistEntryTest {

  @Test
  void parse_shouldReadEntriesSkippingHeaderCommentsAndBlankLines() {
    List<String> lines =
        List.of(
            "# comment",
            "ruleId,page,reason",
            "",
            "color-contrast,login,Brand colours, ticket #1",
            "  region , cart , Layout ticket #2  ");

    assertThat(AllowlistEntry.parse(lines))
        .containsExactly(
            new AllowlistEntry("color-contrast", "login", "Brand colours, ticket #1"),
            new AllowlistEntry("region", "cart", "Layout ticket #2"));
  }

  @Test
  void parse_shouldIgnoreByteOrderMarkAtStartOfFile() {
    List<String> lines = List.of("﻿# comment", "region,cart,Layout ticket #2");

    assertThat(AllowlistEntry.parse(lines))
        .containsExactly(new AllowlistEntry("region", "cart", "Layout ticket #2"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"color-contrast,login", "color-contrast,login, ", ",login,reason"})
  void parse_shouldThrowForMalformedLineNamingLineNumber(String line) {
    assertThatThrownBy(() -> AllowlistEntry.parse(List.of("ruleId,page,reason", line)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("line 2");
  }

  @Test
  void load_shouldParseShippedAllowlistSkippingHeader() {
    assertThat(AllowlistEntry.load())
        .doesNotContain(new AllowlistEntry("ruleId", "page", "reason"))
        .allSatisfy(entry -> assertThat(entry.reason()).isNotBlank());
  }
}
