package com.framework.base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.framework.constants.TimeoutConstants;
import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;

public class BaseTest {

	protected static final Logger logger = LogManager.getLogger(BaseTest.class);

	protected Properties prop;

	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String EXCEL_PATH = USER_DIR + "/testData/testdata/Users.xlsx";
	public static final String JSON_PATH = USER_DIR + "/src/test/resources/testdata/ProductData.json";
	public static final String CONFIG_PATH = USER_DIR + "/src/main/resources/config.properties";

	// ===============================================================
	// SUITE LEVEL
	// ===============================================================
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		ThreadContext.put("testName", "SUITE");
		logger.info("===== TEST SUITE STARTED =====");
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		logger.info("===== TEST SUITE FINISHED =====");
		ThreadContext.clearAll();
	}

	// ===============================================================
	// TEST METHOD LEVEL (MDC AWARE)
	// ===============================================================
	@BeforeMethod(alwaysRun = true)
	public void beforeEachTest(Method method) {

	    ThreadContext.put(
	        "testName",
	        method.getDeclaringClass().getSimpleName() + "." + method.getName()
	    );

	    logger.info("===== TEST STARTED =====");
	}

	@AfterMethod(alwaysRun = true)
	public void afterEachTest() {
	    ThreadContext.remove("testName");
	}

	// ===============================================================
	// SETUP
	// ===============================================================
	@BeforeClass(alwaysRun = true)
	@Parameters({ "os", "browser" })
	public void setup(@Optional("WIN10") String os, @Optional("chrome") String browserName) throws IOException {

		ThreadContext.put("testName", "SETUP");

		logger.info("===== TEST SETUP STARTED =====");

		prop = new Properties();
		prop.load(new FileReader(CONFIG_PATH));

		String exeEnv = prop.getProperty("exe_env").trim();
		String appUrl = prop.getProperty("url").trim();
		String gridUrl = prop.getProperty("grid_url").trim();

		boolean headless = Boolean.parseBoolean(prop.getProperty("headless", "false"));
		boolean incognito = Boolean.parseBoolean(prop.getProperty("incognito", "false"));

		logger.info("Execution Environment : {}", exeEnv);
		logger.info("Browser              : {}", browserName);
		logger.info("OS                   : {}", os);

		if (exeEnv.equalsIgnoreCase("local")) {
			DriverFactory.initLocalDriver(browserName, headless, incognito);
		} else if (exeEnv.equalsIgnoreCase("remote")) {
			DriverFactory.initRemoteDriver(os, browserName, gridUrl);
		} else {
			throw new IllegalArgumentException("Invalid exe_env (Use: local / remote)");
		}

		WebDriver driver = DriverManager.getDriver();

		driver.manage().timeouts().pageLoadTimeout(TimeoutConstants.PAGE_LOAD_TIMEOUT);
		driver.manage().timeouts().scriptTimeout(TimeoutConstants.SCRIPT_TIMEOUT);
		driver.manage().timeouts().implicitlyWait(TimeoutConstants.IMPLICIT_WAIT);

		safeNavigate(driver, appUrl);
		waitForPageLoad(driver);

		String title = driver.getTitle();
		if (title == null || title.isBlank()) {
			throw new RuntimeException("Page did not load correctly — title is empty.");
		}

		logger.info("Page Loaded Successfully. Title: {}", title);
		logger.info("===== TEST SETUP COMPLETED =====");
	}

	// ===============================================================
	// TEARDOWN
	// ===============================================================
	@AfterClass(alwaysRun = true)
	public void teardown() {

		ThreadContext.put("testName", "TEARDOWN");

		logger.info("Closing WebDriver...");
		try {
			DriverManager.cleanUp();
		} catch (Exception e) {
			logger.error("Error while closing WebDriver:", e);
		}
	}

	// ===============================================================
	// SAFE NAVIGATION
	// ===============================================================
	private void safeNavigate(WebDriver driver, String url) {

		logger.info("Navigating to URL: {}", url);

		int attempts = 0;

		while (attempts < TimeoutConstants.NAVIGATION_RETRY_COUNT) {
			try {
				attempts++;
				driver.get(url);
				return;
			} catch (TimeoutException e) {
				logger.warn("Page load timeout (attempt {})", attempts);
				if (attempts >= TimeoutConstants.NAVIGATION_RETRY_COUNT) {
					throw e;
				}
			}
		}
	}

	// ===============================================================
	// PAGE LOAD WAIT
	// ===============================================================
	private void waitForPageLoad(WebDriver driver) {

		new WebDriverWait(driver, TimeoutConstants.EXPLICIT_WAIT).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").toString().matches("complete|interactive"));
	}

	// ===============================================================
	// SCREENSHOT UTILITY
	// ===============================================================
	public static String captureScreen(String testName) {

		WebDriver driver = DriverManager.getDriver();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String screenshotDir = USER_DIR + "/reports/screenshots/";
		new File(screenshotDir).mkdirs();

		String fileName = testName + "_" + timeStamp + ".png";
		String fullPath = screenshotDir + fileName;

		try {
			FileUtils.copyFile(srcFile, new File(fullPath));
		} catch (Exception e) {
			logger.error("Screenshot failed", e);
		}

		return "screenshots/" + fileName;
	}
}
