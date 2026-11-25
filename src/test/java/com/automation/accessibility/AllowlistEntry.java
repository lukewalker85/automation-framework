package com.automation.accessibility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A known, accepted violation that should not fail the build.
 *
 * @param ruleId the axe rule being accepted, e.g. {@code color-contrast}
 * @param page the page the acceptance applies to
 * @param reason why the violation is accepted, e.g. a ticket reference
 */
public record AllowlistEntry(String ruleId, String page, String reason) {

  /** The allowlist file on the test classpath. */
  public static final String RESOURCE = "a11y-allowlist.csv";

  /**
   * Loads the allowlist from {@link #RESOURCE} on the classpath.
   *
   * @return the entries in the file
   * @throws IllegalStateException if the file is missing
   * @throws IllegalArgumentException if a line is malformed
   */
  public static List<AllowlistEntry> load() {
    InputStream in = AllowlistEntry.class.getClassLoader().getResourceAsStream(RESOURCE);
    if (in == null) {
      throw new IllegalStateException("Allowlist not found on classpath: " + RESOURCE);
    }
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      return parse(reader.lines().toList());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  /**
   * Parses allowlist lines of the form {@code ruleId,page,reason}. Blank lines, lines starting with
   * {@code #} and the {@code ruleId,page,reason} header are skipped. The reason may contain commas.
   *
   * @param lines the lines of the allowlist file
   * @return the entries, in file order
   * @throws IllegalArgumentException if a line is missing a rule ID, page or reason, naming the
   *     line number
   */
  public static List<AllowlistEntry> parse(List<String> lines) {
    List<AllowlistEntry> entries = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      // Windows editors may save the file with a UTF-8 byte-order mark, which strip() keeps
      String line = lines.get(i).replaceFirst("^﻿", "").strip();
      if (line.isEmpty() || line.startsWith("#") || line.equals("ruleId,page,reason")) {
        continue;
      }
      String[] fields = line.split(",", 3);
      if (fields.length < 3 || fields[0].isBlank() || fields[1].isBlank() || fields[2].isBlank()) {
        throw new IllegalArgumentException(
            RESOURCE
                + " line "
                + (i + 1)
                + ": expected ruleId,page,reason with none blank, got '"
                + line
                + "'");
      }
      entries.add(new AllowlistEntry(fields[0].strip(), fields[1].strip(), fields[2].strip()));
    }
    return List.copyOf(entries);
  }
}
