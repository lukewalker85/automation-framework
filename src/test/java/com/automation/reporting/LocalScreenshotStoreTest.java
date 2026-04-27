package com.automation.reporting;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Unit tests for LocalScreenshotStore */
public class LocalScreenshotStoreTest {

  @Test
  void shouldSaveScreenshotinTempDir(@TempDir Path tempDir) throws IOException {
    LocalScreenshotStore store = new LocalScreenshotStore(tempDir);
    byte[] fakeImage = {10, 20, 30, 40, 50};
    String filename = "test-screenshot.png";
    store.storeScreenshot(fakeImage, filename);
    Path expectedFile = tempDir.resolve(filename);
    assertThat(expectedFile).exists();
    assertThat(expectedFile).hasBinaryContent(fakeImage);
  }
}
