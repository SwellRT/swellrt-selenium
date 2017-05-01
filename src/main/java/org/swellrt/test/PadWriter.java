package org.swellrt.test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author pablojan@gmail.com
 *
 */
public class PadWriter implements Callable<Optional<String>> {

  String waveEditorDivContainerId = "editor";

  final String inputFileUri;
  final String padUrl;
  final int startLine;
  final String browser;

  final Random random = new Random(System.currentTimeMillis());

  WebDriver driver = null;

  public PadWriter(String inputFileUri, String padUrl, int startLine, String browser) {
    this.inputFileUri = inputFileUri;
    this.padUrl = padUrl;
    this.startLine = startLine;
    this.browser = browser != null && (browser.equals("chrome") || browser.equals("firefox"))
        ? browser : "chrome";

  }

  private void writeChars(WebElement contentEditable, CharSequence chars)
      throws InterruptedException {
    for (int i = 0; i < chars.length(); i++) {
      contentEditable.sendKeys(chars.charAt(i) + "");
      Thread.sleep((int) (113 * random.nextFloat() * 2)); // 227
    }
  }

  protected boolean isChrome() {
    return "chrome".equals(this.browser);
  }

  protected boolean isFirefox() {
    return "firefox".equals(this.browser);
  }

  @Override
  public Optional<String> call() {

    Optional<String> result = Optional.empty();


    try (Stream<String> lines = Files.lines(Paths.get(this.inputFileUri))) {

      if (isChrome())
        driver = new ChromeDriver();
      else if (isFirefox())
        driver = new FirefoxDriver();

      // Open debug console
      // driver.get("chrome://settings/");
      // driver.findElement(By.tagName("body")).sendKeys(Keys.F12);
      // Thread.sleep(2000);

      // Navigate to the target pad
      driver.get(padUrl);
      // The document can take a while to load even after DOM is built
      Thread.sleep(6000);

      // Looking for wave-editor-on to ensure document was loaded
      WebElement contentEditable = driver.findElement(By.className("wave-editor-on"));

      WebElement line = null;
      if (browser.equals("chrome")) {
        line = (WebElement) ((JavascriptExecutor) driver).executeScript(
            "return  document.getElementsByClassName('wave-editor-on')[0].firstChild.children["
                + startLine + "]");
      } else {
        line = contentEditable
            .findElement(
                By.xpath("//div[@id='" + waveEditorDivContainerId + "']//div[" + startLine + "]"));
      }

      // Place cursor
      line.click();

      lines.flatMap(l -> Arrays.stream(l.split(" "))).forEach(w -> {

        try {

          writeChars(contentEditable, w + " ");



          // simulate delete and rewrite some chars
          if (browser.equals("chrome") && w.length() > 2 && w.length() % 2 == 0) {
            writeChars(contentEditable, "\b\b\b\b");
            writeChars(contentEditable, w.substring(w.length() - 3, w.length()) + " ");
          }

        } catch (InterruptedException e) {
          System.out.println("PadWriter Thread interrupted");
        }


      });


    } catch (Exception e) {
      e.printStackTrace(System.out);
    } finally {

    }

    return result;

  }

  public void close() {
    if (driver != null)
      driver.close();
  }

}
