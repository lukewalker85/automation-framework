package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Page object for the SauceDemo sidebar component. */
public class SidebarPage extends BasePage {

  private static final Logger LOG = LoggerFactory.getLogger(SidebarPage.class);

  @FindBy(id = "react-burger-menu-btn")
  private WebElement burgerMenuBtn;

  @FindBy(id = "logout_sidebar_link")
  private WebElement logoutSidebarLink;

  @FindBy(id = "react-burger-cross-btn")
  private WebElement closeSidebarBtn;

  public SidebarPage(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /** Logout of current user by clicking link in sidebar */
  public void logoutUsingSidebar() {
    LOG.info("Logging out with sidebar link");
    click(burgerMenuBtn);
    click(logoutSidebarLink);
  }
}
