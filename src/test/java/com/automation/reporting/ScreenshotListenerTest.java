package com.automation.reporting;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/** Unit tests for ScreenshotListener */
public class ScreenshotListenerTest {

  @Test
  void shouldBuildFileName() {
    String testName = "successfulLoginTest";
    String filename = ScreenshotListener.buildFileName(testName);
    assertThat(filename).startsWith("successfulLoginTest_");
    assertThat(filename).endsWith(".png");
    assertThat(filename)
        .matches("successfulLoginTest_\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}-\\d{3}.png");
  }

  @Test
  void shouldReplaceSpacesInFileName() {
    String filename = ScreenshotListener.buildFileName("Successful login");
    assertThat(filename).startsWith("Successful_login_");
    assertThat(filename).doesNotContain(" ");
    assertThat(filename).endsWith(".png");
  }

  @Test
  void shouldReplaceIllegalCharactersInFileName() {
    String filename = ScreenshotListener.buildFileName("myTest[0](foo:bar)");
    assertThat(filename).startsWith("myTest_0__foo_bar_");
    assertThat(filename).endsWith(".png");
    assertThat(filename).doesNotContain("[", "]", "(", ")", ":");
  }
}
