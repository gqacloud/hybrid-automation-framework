package com.framework.base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;

public class BaseTest {

	protected static final Logger logger = LogManager.getLogger(BaseTest.class);

	public Properties prop;

	public static final String userDir = System.getProperty("user.dir");
	public static final String excelPath = userDir + "/testData/testdata/Users.xlsx";
	public static final String jsonPath = userDir + "/src/test/resources/testdata/ProductData.json";
	public static final String propertyfilePath = userDir + "/src/main/resources/config.properties";

	@BeforeClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	@Parameters({ "os", "browser" })
	public void setup(@Optional("mac") String os, @Optional("chrome") String browserName) throws IOException {

		logger.info("=== Test Setup Started ===");

		prop = new Properties();
		prop.load(new FileReader(propertyfilePath));

		String exeEnv = prop.getProperty("exe_env").trim();
		String appUrl = prop.getProperty("url").trim();
		String gridUrl = prop.getProperty("grid_url").trim();

		boolean headless = Boolean.parseBoolean(prop.getProperty("headless").trim());
		boolean incognito = Boolean.parseBoolean(prop.getProperty("incognito").trim());

		logger.info("Execution Environment : {}", exeEnv);
		logger.info("Browser              : {}", browserName);
		logger.info("OS                   : {}", os);

		// Initialize WebDriver
		if (exeEnv.equalsIgnoreCase("local")) {
			DriverFactory.initLocalDriver(browserName, headless, incognito);
		} else if (exeEnv.equalsIgnoreCase("remote")) {
			DriverFactory.initRemoteDriver(os, browserName, gridUrl);
		} else {
			throw new IllegalArgumentException("Invalid exe_env value (Use: local / remote)");
		}

		WebDriver driver = DriverManager.getDriver();

		logger.info("Navigating to URL: {}", appUrl);
		safeNavigate(driver, appUrl, 30); // ✅ increased for stability

		waitForPageLoad(driver);

		String title = driver.getTitle();
		if (title == null || title.isBlank()) {
			logger.error("Page title is empty — application may not have loaded correctly.");
			throw new RuntimeException("Page did not load correctly.");
		}

		logger.info("Page Loaded Successfully. Title: {}", title);
		logger.info("=== Test Setup Completed ===");
	}

	@AfterClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	public void teardown() {
		logger.info("Closing WebDriver...");
		try {
			DriverManager.cleanUp();
		} catch (Exception e) {
			logger.error("Error while closing WebDriver: {}", e.getMessage());
		}
	}

	// ===============================================================
	// ✅ FIXED: SAFE NAVIGATION (NO THREADS)
	// ===============================================================
	private void safeNavigate(WebDriver driver, String url, int timeoutSec) {

		logger.info("Navigating to URL with pageLoadTimeout {} seconds", timeoutSec);

		driver.manage().timeouts().pageLoadTimeout(java.time.Duration.ofSeconds(timeoutSec));

		int attempts = 0;

		while (attempts < 2) {
			try {
				attempts++;
				logger.info("Navigation attempt {} to {}", attempts, url);

				driver.get(url);

				logger.info("Navigation successful on attempt {}", attempts);
				return;

			} catch (org.openqa.selenium.TimeoutException e) {

				logger.warn("Page load timeout on attempt {} ({} seconds)", attempts, timeoutSec);

				if (attempts >= 2) {
					logger.error("Navigation failed after retry: {}", url);
					throw e;
				}

				logger.info("Retrying navigation once...");
			}
		}
	}

	// ===============================================================
	// ✅ FIXED: STABLE PAGE LOAD WAIT
	// ===============================================================
	private void waitForPageLoad(WebDriver driver) {

		logger.info("Waiting for page readiness...");

		new WebDriverWait(driver, java.time.Duration.ofSeconds(15)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").toString().matches("complete|interactive"));

		logger.info("Page ready.");
	}

	// ===============================================================
	// SCREENSHOT
	// ===============================================================
	public static String captureScreen(String testName) {

		logger.info("Capturing screenshot for test: {}", testName);

		WebDriver driver = DriverManager.getDriver();

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String screenshotDir = userDir + "/reports/screenshots/";
		new File(screenshotDir).mkdirs();

		String fileName = testName + "_" + timeStamp + ".png";
		String fullPath = screenshotDir + fileName;

		try {
			FileUtils.copyFile(srcFile, new File(fullPath));
			logger.info("Screenshot captured at: {}", fullPath);
		} catch (Exception e) {
			logger.error("Screenshot failed: {}", e.getMessage());
		}

		return "screenshots/" + fileName;
	}
}
