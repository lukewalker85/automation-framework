package com.automation.reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Saves screenshot bytes to the local filesystem. Implements {@link ScreenshotStore}. */
public class LocalScreenshotStore implements ScreenshotStore {

  public static final Path DEFAULT_DIR = Path.of("target/screenshots");
  private static final Logger LOG = LoggerFactory.getLogger(LocalScreenshotStore.class);
  private final Path outputDir;

  /** Creates a store that saves screenshots to the specified directory. */
  public LocalScreenshotStore(Path outputDir) {
    this.outputDir = outputDir;
  }

  /** {@inheritDoc} */
  @Override
  public void storeScreenshot(byte[] bytes, String filename) throws IOException {
    Objects.requireNonNull(bytes, "bytes must not be null");
    Objects.requireNonNull(filename, "filename must not be null");
    Files.createDirectories(outputDir);
    Path filePath = outputDir.resolve(filename).normalize();
    if (!filePath.startsWith(outputDir.normalize())) {
      throw new IOException("Path traversal detected: " + filename);
    }
    Files.write(filePath, bytes);
    LOG.info("Screenshot saved: {}", filePath);
  }
}
