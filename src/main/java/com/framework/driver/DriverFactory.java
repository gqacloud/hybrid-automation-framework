package com.framework.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.framework.constants.TimeoutConstants;

public final class DriverFactory {

	private static final Logger logger = LogManager.getLogger(DriverFactory.class);

	private DriverFactory() {
	}

	// ================= LOCAL =================
	public static void initLocalDriver(String browser, boolean headless, boolean incognito) {

		WebDriver driver;

		switch (browser.toLowerCase()) {

		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			if (headless) {
				chromeOptions.addArguments("--headless=new");
			}
			if (incognito) {
				chromeOptions.addArguments("--incognito");
			}
			chromeOptions.addArguments("--window-size=1920,1080");

			driver = new ChromeDriver(chromeOptions);
			configureDriver(driver);

			// ✅ Correct: use run flags directly
			DriverManager.setRunMode(headless, incognito);
			break;

		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			if (headless) {
				edgeOptions.addArguments("--headless=new");
			}
			if (incognito) {
				edgeOptions.addArguments("--inprivate");
			}
			edgeOptions.addArguments("--window-size=1920,1080");

			driver = new EdgeDriver(edgeOptions);
			configureDriver(driver);

			// ✅ Correct
			DriverManager.setRunMode(headless, incognito);
			break;

		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			if (headless) {
				firefoxOptions.addArguments("--headless");
			}
			firefoxOptions.addArguments("--width=1920");
			firefoxOptions.addArguments("--height=1080");

			driver = new FirefoxDriver(firefoxOptions);
			configureDriver(driver);

			// ❗ Firefox does not support true incognito via args
			DriverManager.setRunMode(headless, false);
			break;

		default:
			throw new IllegalArgumentException("Unsupported local browser: " + browser);
		}

		registerDriver(driver);
	}

	// ================= REMOTE =================
	public static void initRemoteDriver(String os, String browser, String gridUrl) throws MalformedURLException {

		WebDriver driver;
		URL remoteURL = new URL(gridUrl);

		switch (browser.toLowerCase()) {

		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(remoteURL, chromeOptions);
			break;

		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(remoteURL, firefoxOptions);
			break;

		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(remoteURL, edgeOptions);
			break;

		default:
			throw new IllegalArgumentException("Unsupported remote browser: " + browser);
		}

		configureDriver(driver);

		// Grid controls profile mode
		DriverManager.setRunMode(false, false);

		registerDriver(driver);
	}

	// ================= COMMON =================
	private static void configureDriver(WebDriver driver) {
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
	}

	/**
	 * Registers driver and REAL browser name from capabilities
	 */
	private static void registerDriver(WebDriver driver) {

		String actualBrowser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();

		logger.info("Registering WebDriver | Browser: {} | Thread: {}", actualBrowser, Thread.currentThread().getId());

		DriverManager.setDriver(driver, actualBrowser);
	}
}
