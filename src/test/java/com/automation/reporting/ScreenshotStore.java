package com.automation.reporting;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Interface to save screenshots of failed tests to file location */
public interface ScreenshotStore {

  /** Builds filename for screenshot on failure */
  static String buildFileName(String testName) {
    return testName.replaceAll("[^a-zA-Z0-9\\-]", "_")
        + "_"
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"))
        + ".png";
  }

  /**
   * Saves a screenshot to the configured storage destination.
   *
   * @param bytes the screenshot image data
   * @param filename the name of the file to save
   * @throws IOException if the screenshot cannot be saved
   */
  void storeScreenshot(byte[] bytes, String filename) throws IOException;
}
