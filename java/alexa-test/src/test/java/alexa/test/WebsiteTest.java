package alexa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Generated by Selenium IDE
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class WebsiteTest {
  private WebDriver driver;

  private Map<String, Object> vars;

  JavascriptExecutor js;

  @Before
  public void setUp() throws InterruptedException {

    System.setProperty("webdriver.chrome.driver",
        "C:\\projects\\my-project\\workspaces\\main\\my-thai-star\\java\\alexa-test\\chromedriver.exe");
    this.driver = new ChromeDriver();
    this.js = (JavascriptExecutor) this.driver;
    this.vars = new HashMap<String, Object>();

    this.driver.get("http://localhost:4200/restaurant");
    this.driver.manage().window().setSize(new Dimension(1536, 824));
    Thread.sleep(1000);
    {
      WebElement element = this.driver.findElement(By.name("login"));
      Actions builder = new Actions(this.driver);
      builder.moveToElement(element).perform();
    }
    {
      WebElement element = this.driver.findElement(By.tagName("body"));
      Actions builder = new Actions(this.driver);
      builder.moveToElement(element, 0, 0).perform();
    }
    this.driver.findElement(By.cssSelector(
        "body > app-public-main > div > div > mat-toolbar.mat-toolbar.headerContainer.ng-tns-c277-0.mat-primary.mat-toolbar-single-row > app-public-header > div > button:nth-child(6)"))
        .click();
    Thread.sleep(1000);
  }

  @After
  public void tearDown() {

    this.driver.quit();
  }

  @Test
  @Ignore
  public void openAllTabsOfWaiterCockpit() throws InterruptedException {

    this.driver.findElement(By.id("mat-input-1")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys(Keys.ENTER);
    Thread.sleep(1000);
    assertEquals("http://localhost:4200/orders", this.driver.getCurrentUrl());
    this.driver.findElement(By.linkText("ARCHIV")).click();
    Thread.sleep(50);
    assertEquals("http://localhost:4200/order-archive", this.driver.getCurrentUrl());
    this.driver.findElement(By.linkText("RESERVIERUNGEN")).click();
    Thread.sleep(50);
    assertEquals("http://localhost:4200/reservations", this.driver.getCurrentUrl());
    this.driver.findElement(By.linkText("TISCHE")).click();
    Thread.sleep(50);
    assertEquals("http://localhost:4200/tables", this.driver.getCurrentUrl());
  }

  @Test
  @Ignore
  public void openAllTabsOfAdminCockpit() throws InterruptedException {

    this.driver.findElement(By.id("mat-input-1")).sendKeys("admin");
    this.driver.findElement(By.id("mat-input-2")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys(Keys.ENTER);
    Thread.sleep(1000);
    assertEquals("http://localhost:4200/admin", this.driver.getCurrentUrl());
  }

  @Test
  public void openAllTabsOfUserCockpit() throws InterruptedException {

    this.driver.findElement(By.id("mat-input-1")).sendKeys("user0");
    this.driver.findElement(By.id("mat-input-2")).sendKeys("password");
    this.driver.findElement(By.id("mat-input-2")).sendKeys(Keys.ENTER);
    Thread.sleep(1000);
    assertEquals("http://localhost:4200/restaurant", this.driver.getCurrentUrl());

    this.driver.findElement(By.linkText("MENÜ")).click();
    Thread.sleep(50);
    assertEquals("http://localhost:4200/menu", this.driver.getCurrentUrl());

    this.driver.findElement(By.linkText("TISCH BUCHEN")).click();
    Thread.sleep(50);
    assertEquals("http://localhost:4200/bookTable", this.driver.getCurrentUrl());
  }

  @Test
  public void deleteOrderline() throws InterruptedException {

    this.driver.findElement(By.id("mat-input-1")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys(Keys.ENTER);
    Thread.sleep(1000);
    this.driver.findElement(By.cssSelector(
        "body > app-public-main > div > div > div > mat-sidenav-container > mat-sidenav-content > app-cockpit-order-cockpit > mat-card > div > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-cell.cdk-column-orderEdit.mat-column-orderEdit.ng-star-inserted > button > span"))
        .click();
    Thread.sleep(1000);
    this.driver.findElement(By.cssSelector(".mat-row:nth-child(3) #deleteButton > .mat-button-wrapper")).click();
    Thread.sleep(1000);
    this.driver.findElement(By.cssSelector("#applyButton")).click();

    this.driver.findElement(By.cssSelector(
        "body > app-public-main > div > div > div > mat-sidenav-container > mat-sidenav-content > app-cockpit-order-cockpit > mat-card > div > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-cell.cdk-column-orderEdit.mat-column-orderEdit.ng-star-inserted > button > span"))
        .click();
    Thread.sleep(1000);
    try {
      this.driver.findElement(By.cssSelector(".mat-row:nth-child(3)")).click();
      fail("this row should have been deleted");
    } catch (Exception e) {
      assertTrue(true);
    }

  }

  @Test
  public void addOrderline() throws InterruptedException {

    this.driver.findElement(By.id("mat-input-1")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys("waiter");
    this.driver.findElement(By.id("mat-input-2")).sendKeys(Keys.ENTER);
    Thread.sleep(2000);

    {
      WebElement element = this.driver.findElement(By.cssSelector(".mat-row:nth-child(1) .mat-button-wrapper"));
      Actions builder = new Actions(this.driver);
      builder.moveToElement(element).perform();
    }
    this.driver.findElement(By.cssSelector(
        "body > app-public-main > div > div > div > mat-sidenav-container > mat-sidenav-content > app-cockpit-order-cockpit > mat-card > div > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-cell.cdk-column-orderEdit.mat-column-orderEdit.ng-star-inserted > button > span"))
        .click();
    Thread.sleep(3000);
    this.driver.findElement(By.cssSelector("#dishSelect")).click();
    this.driver.findElement(By.xpath("//mat-option[@id=\'entryOption\']/span")).click();
    this.driver.findElement(By.cssSelector("#addButton")).click();
    {
      WebElement element = this.driver.findElement(By.cssSelector("#applyButton > .mat-button-wrapper"));
      Actions builder = new Actions(this.driver);
      builder.moveToElement(element).perform();
    }
    this.driver.findElement(By.cssSelector("#applyButton > .mat-button-wrapper")).click();

    this.driver.findElement(By.cssSelector(
        "body > app-public-main > div > div > div > mat-sidenav-container > mat-sidenav-content > app-cockpit-order-cockpit > mat-card > div > table > tbody > tr:nth-child(1) > td.mat-cell.cdk-cell.cdk-column-orderEdit.mat-column-orderEdit.ng-star-inserted > button > span"))
        .click();
    Thread.sleep(1000);

    this.driver.findElement(By.cssSelector(".mat-row:nth-child(4) #deleteButton > .mat-button-wrapper")).click();
  }
}