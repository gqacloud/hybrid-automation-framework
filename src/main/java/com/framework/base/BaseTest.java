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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
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
	// TEST-LEVEL LOG CONTEXT (MDC)
	// ===============================================================
	@BeforeMethod(alwaysRun = true)
	public void beforeEachTest(Method method) {

		ThreadContext.put("testName", method.getDeclaringClass().getSimpleName());

		ThreadContext.put("threadId", String.valueOf(Thread.currentThread().getId()));

		logger.info("===== TEST STARTED: {} =====", method.getDeclaringClass().getSimpleName());
	}

	@AfterMethod(alwaysRun = true)
	public void afterEachTest(ITestResult result) {

	    // Do NOT log PASS / FAIL / SKIP here
	    // Test lifecycle is handled by ExtentReportListener

	    logger.info("Closing WebDriver...");

	    ThreadContext.clearAll();
	}


	// ===============================================================
	// SETUP
	// ===============================================================
	@BeforeClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	@Parameters({ "os", "browser" })
	public void setup(@Optional("WIN10") String os, @Optional("chrome") String browserName) throws IOException {

		logger.info("===== Test Setup Started =====");

		// Load config
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

		// Initialize driver
		if (exeEnv.equalsIgnoreCase("local")) {
			DriverFactory.initLocalDriver(browserName, headless, incognito);
		} else if (exeEnv.equalsIgnoreCase("remote")) {
			DriverFactory.initRemoteDriver(os, browserName, gridUrl);
		} else {
			throw new IllegalArgumentException("Invalid exe_env value (Use: local / remote)");
		}

		WebDriver driver = DriverManager.getDriver();

		// Apply timeouts
		driver.manage().timeouts().pageLoadTimeout(TimeoutConstants.PAGE_LOAD_TIMEOUT);
		driver.manage().timeouts().scriptTimeout(TimeoutConstants.SCRIPT_TIMEOUT);
		driver.manage().timeouts().implicitlyWait(TimeoutConstants.IMPLICIT_WAIT);

		// Navigate safely
		safeNavigate(driver, appUrl);
		waitForPageLoad(driver);

		String title = driver.getTitle();
		if (title == null || title.isBlank()) {
			throw new RuntimeException("Page did not load correctly — title is empty.");
		}

		logger.info("Page Loaded Successfully. Title: {}", title);
		logger.info("===== Test Setup Completed =====");
	}

	// ===============================================================
	// TEARDOWN
	// ===============================================================
	@AfterClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	public void teardown() {
		logger.info("Closing WebDriver...");
		try {
			DriverManager.cleanUp();
		} catch (Exception e) {
			logger.error("Error while closing WebDriver:", e);
		}
	}

	// ===============================================================
	// SAFE NAVIGATION WITH RETRY
	// ===============================================================
	private void safeNavigate(WebDriver driver, String url) {

		logger.info("Navigating to URL: {}", url);

		int attempts = 0;

		while (attempts < TimeoutConstants.NAVIGATION_RETRY_COUNT) {
			try {
				attempts++;
				logger.info("Navigation attempt {}", attempts);

				driver.get(url);
				logger.info("Navigation successful");
				return;

			} catch (TimeoutException e) {
				logger.warn("Page load timeout on attempt {}", attempts);

				if (attempts >= TimeoutConstants.NAVIGATION_RETRY_COUNT) {
					logger.error("Navigation failed after retries: {}", url);
					throw e;
				}

				logger.info("Retrying navigation...");
			}
		}
	}

	// ===============================================================
	// PAGE LOAD WAIT
	// ===============================================================
	private void waitForPageLoad(WebDriver driver) {

		logger.info("Waiting for page readiness...");

		new WebDriverWait(driver, TimeoutConstants.EXPLICIT_WAIT).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").toString().matches("complete|interactive"));

		logger.info("Page is ready.");
	}

	public String getText(By locator) {
		return new WebDriverWait(DriverManager.getDriver(), TimeoutConstants.EXPLICIT_WAIT)
				.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
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
			logger.info("Screenshot captured for test [{}] at: {}", testName, fullPath);
		} catch (Exception e) {
			logger.error("Screenshot failed:", e);
		}

		return "screenshots/" + fileName;
	}
}
