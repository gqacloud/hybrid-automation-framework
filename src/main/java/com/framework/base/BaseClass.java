package com.framework.base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.*;

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
import org.testng.annotations.Parameters;

import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;

public class BaseClass {

	protected static final Logger logger = LogManager.getLogger(BaseClass.class);
	public Properties prop;

	public static final String userDir = System.getProperty("user.dir");
	public static final String excelPath = userDir + "/testData/Users.xlsx";
	public static final String jsonPath = userDir + "/src/test/resources/ProductData.json";
	public static final String propertyfilePath = userDir + "/src/main/resources/config.properties";

	// ========================================================================
	// BEFORE CLASS — CORE SETUP
	// ========================================================================

	@BeforeClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	@Parameters({ "os", "browser" })
	public void setup(String os, String browserName) throws IOException {

		logger.info("========== Test Setup Started ==========");

		// Load config.properties
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

		// ----------------------------- DRIVER INIT -----------------------------

		if (exeEnv.equalsIgnoreCase("local")) {
			DriverFactory.initLocalDriver(browserName, headless, incognito);
		} else if (exeEnv.equalsIgnoreCase("remote")) {
			DriverFactory.initRemoteDriver(os, browserName, gridUrl);
		} else {
			throw new IllegalArgumentException("Invalid exe_env value (Use: local / remote)");
		}

		WebDriver driver = DriverManager.getDriver();

		// ----------------------------- URL NAVIGATION -----------------------------

		logger.info("Navigating to: {}", appUrl);
		safeNavigate(driver, appUrl, 15);

		// ----------------------------- BASIC VALIDATION -----------------------------

		waitForPageLoad(driver);

		// Title check (generic)
		String title = driver.getTitle();
		if (title == null || title.isBlank()) {
			logger.error("❌ Page title is empty — Page may not have loaded correctly.");
			throw new RuntimeException("Invalid or blank page title.");
		}

		logger.info("Page loaded → Title: {}", title);
		logger.info("========== Test Setup Completed ==========");
	}

	// ========================================================================
	// AFTER CLASS — CLEANUP
	// ========================================================================
	@AfterClass(groups = { "Sanity", "Regression", "Master", "Functional" })
	public void teardown() {
		logger.info("Closing WebDriver");
		DriverManager.cleanUp();
	}

	// ========================================================================
	// SAFELY NAVIGATE TO A URL WITH HARD TIMEOUT PROTECTION (Industry Standard)
	// Prevents browser freeze on unreachable sites
	// ========================================================================
	private void safeNavigate(WebDriver driver, String url, int timeoutSec) {

		ExecutorService executor = Executors.newSingleThreadExecutor();

		Future<?> future = executor.submit(() -> driver.get(url));

		try {
			future.get(timeoutSec, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			logger.error("❌ Navigation timed out after {} seconds → {}", timeoutSec, url);
			future.cancel(true);
			throw new RuntimeException("Page navigation timeout.");
		} catch (Exception e) {
			logger.error("❌ Error during navigation: {}", e.getMessage());
			throw new RuntimeException("Failed to navigate to URL.", e);
		} finally {
			executor.shutdownNow();
		}
	}

	// ========================================================================
	// VALIDATE DOM READY STATE ONLY (Generic — No Domain Logic)
	// ========================================================================
	private void waitForPageLoad(WebDriver driver) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}

		logger.info("Waiting for page DOM to reach 'complete' state...");

		new WebDriverWait(driver, java.time.Duration.ofSeconds(15)).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return document.readyState").equals("complete"));

		logger.info("DOM Ready");
	}

	// ========================================================================
	// SCREENSHOT UTILITY
	// ========================================================================
	public String captureScreen(String tname) {

		WebDriver driver = DriverManager.getDriver();

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		String dir = userDir + "/screenshots/";
		new File(dir).mkdirs();

		String destPath = dir + tname + "_" + timeStamp + ".png";

		try {
			FileUtils.copyFile(srcFile, new File(destPath));
		} catch (Exception e) {
			logger.error("❌ Screenshot failed: {}", e.getMessage());
		}

		return destPath;
	}
}
