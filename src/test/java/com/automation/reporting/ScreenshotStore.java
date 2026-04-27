package com.automation.reporting;

import java.io.IOException;

/** Interface to save screenshots of failed tests to file location */
public interface ScreenshotStore {

  /**
   * Saves a screenshot to the configured storage destination.
   *
   * @param bytes the screenshot image data
   * @param filename the name of the file to save
   * @throws IOException if the screenshot cannot be saved
   */
  void storeScreenshot(byte[] bytes, String filename) throws IOException;
}
