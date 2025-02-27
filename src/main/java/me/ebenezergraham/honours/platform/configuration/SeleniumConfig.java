package me.ebenezergraham.honours.platform.configuration;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class SeleniumConfig {

  private WebDriver driver;

  public SeleniumConfig() {
    Capabilities capabilities = DesiredCapabilities.chrome();
    driver = new ChromeDriver(capabilities);
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  static {
    System.setProperty("webdriver.gecko.driver", findFile("geckodriver.mac"));
  }

  static private String findFile(String filename) {
    String paths[] = {"", "bin/", "target/classes"};
    for (String path : paths) {
      if (new File(path + filename).exists())
        return path + filename;
    }
    return "";
  }

  public void clickElement(WebElement element) {
    element.click();
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void navigateTo(String url) {
    driver.navigate().to(url);
  }

}
