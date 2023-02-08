package testBase;

import static org.testng.Assert.assertTrue;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

public class BaseClass {

	public static WebDriver driver;
	public static Properties Config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;

	@BeforeSuite
	public void setUp() throws IOException {

		if (driver == null) {

			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\java\\properties\\Config.properties");

			Config.load(fis);

			fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\java\\properties\\OR.properties");

			OR.load(fis);
		}

		if (Config.getProperty("browser").equalsIgnoreCase("firefox")) {

			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\lib\\geckodriver.exe");

			driver = new FirefoxDriver();

		} else if (Config.getProperty("browser").equalsIgnoreCase("chrome")) {

			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\lib\\chromedriver.exe");

			driver = new ChromeDriver();

		} else if (Config.getProperty("browser").equalsIgnoreCase("ie")) {

			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\lib\\IEDriverServer.exe");

			driver = new InternetExplorerDriver();

		} else {

			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "\\lib\\chromedriver.exe");

			driver = new ChromeDriver();

		}

		driver.manage().timeouts().implicitlyWait(Integer.parseInt(Config.getProperty("implicit.wait")),
				TimeUnit.SECONDS);

		driver.manage().window().maximize();

	}

	/**************************
	 * Methods Start
	 * @throws Exception 
	 ***********************************************************************************/

	/************************Generic methods********************************************/

	//Click generic method
	public void click(String locator) throws Exception {

		overlayCheckAndClose();

		WebElement element = getLocator(locator);
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", element);

	}

	//Verify element is present generic method
	public boolean isElementPresent(String locator) throws Exception {

		try {

			overlayCheckAndClose();
			WebElement element = getLocator(locator);
			element.isDisplayed();
			return true;

		} catch (NoSuchElementException e) {

			return false;

		}

	}

	//Get list of web elements
	public List<WebElement> getListOfWebElements(String locator) {

		overlayCheckAndClose();

		List<WebElement> webElement_list = driver.findElements(By.xpath(locator));

		return webElement_list;

	}

	//Verify and close if any random overlay is present on current page
	public void overlayCheckAndClose() {

		waitForPageToLoadCompletely(driver);

		if(driver.findElements(By.linkText("No thanks")).size() > 0) {

			driver.findElement(By.linkText("No thanks")).click();

		}

	}

	//Assert generic methods
	public void assertTrueVerification(String locatorLabel) throws Exception {

		Assert.assertTrue(isElementPresent(locatorLabel), "Element not found");

		Thread.sleep(2000);
	}
	
	public void assertStringContainsVerification(String locatorLabel, String stringToCompare) throws Exception {

		overlayCheckAndClose();

		WebElement element = getLocator(locatorLabel);

		String element_getText = element.getText();

		assertTrue(element_getText.contains(stringToCompare), "Text not matched");

	}


	// Highlighter generic method for WebElement
	public void highLighterMethod(WebDriver driver, WebElement element) throws Exception {

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].setAttribute('style', 'border: 5px solid yellow;');", element);

	}

	//Generic method for splitting OR by locator type and value
	public WebElement getLocator(String OR_locatorLabel) throws Exception {

		String locator = OR.getProperty(OR_locatorLabel);

		// extract the locator type and value from the object
		String locatorType = locator.split(";")[0];
		String locatorValue = locator.split(";")[1];

		// return an instance of the By class based on the type of the locator
		if(locatorType.toLowerCase().equals("id")) {

			WebElement element = driver.findElement(By.id(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if(locatorType.toLowerCase().equals("name")) {

			WebElement element = driver.findElement(By.name(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class"))) {

			WebElement element = driver.findElement(By.className(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if((locatorType.toLowerCase().equals("tagname")) || (locatorType.toLowerCase().equals("tag"))) {

			WebElement element = driver.findElement(By.tagName(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if((locatorType.toLowerCase().equals("linktext")) || (locatorType.toLowerCase().equals("link"))) {

			WebElement element = driver.findElement(By.linkText(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if(locatorType.toLowerCase().equals("partiallinktext")) {

			WebElement element = driver.findElement(By.partialLinkText(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if((locatorType.toLowerCase().equals("cssselector")) || (locatorType.toLowerCase().equals("css"))) {

			WebElement element = driver.findElement(By.cssSelector(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else if(locatorType.toLowerCase().equals("xpath")) {

			WebElement element = driver.findElement(By.xpath(locatorValue));

			highLighterMethod(driver, element);

			return element;
		}
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}

	//Mouse hover generic method
	public void mouseHover(String locatorLabel) throws Exception {

		WebElement element = getLocator(locatorLabel);

		//Create object 'action' of an Actions class
		Actions action = new Actions(driver);

		overlayCheckAndClose();

		//Mouseover on an element
		action.moveToElement(element)/* .build() */.perform();

	}

	// Method to wait until page loads

	public void waitForPageToLoadCompletely(WebDriver driver) {

		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		WebDriverWait wait = new WebDriverWait(driver, 30);

		wait.until(pageLoadCondition);
	}

	public void explicitWait(WebElement waitforLocator) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, 10);

		wait.until(ExpectedConditions.elementToBeClickable(waitforLocator));
	}

	public void addProductInBag(String topNavLocator, String topSubNavLocator, String cartQuantity) throws Exception {

		//Mouse Hover
		mouseHover(topNavLocator);

		//Click on Foundation sub menu
		click(topSubNavLocator);
		
		//Validation
		assertTrueVerification("sort_by.ddl");

		//Get list of options
		List<WebElement> allProduct = getListOfWebElements("//a[@class='name-link']");
		
		//Get text of product to be clicked for its name
		String selectedProduct1Text = allProduct.get(1).getText();

		//Click on first element of list
		allProduct.get(1).click();

		assertStringContainsVerification("product_name", selectedProduct1Text);

		click("add_to_bag_Btn");

		Thread.sleep(3000);

		assertStringContainsVerification("minicart_quantity", cartQuantity);
	}


	/**************************
	 * Methods End
	 ***********************************************************************************/

	@AfterSuite
	public void tearDown() {

		if (driver != null) {

			driver.quit();

		}

	}

}

