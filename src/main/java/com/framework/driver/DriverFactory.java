package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {

	private static final Logger logger = LogManager.getLogger(DriverFactory.class);

	/**
	 * Initialize LOCAL WebDriver instance.
	 */
	public static void initLocalDriver(String browser, boolean headless, boolean incognito) {
		logger.info("Initializing LOCAL driver for browser: {}", browser);

		WebDriver driver;

		switch (browser.toLowerCase()) {

		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			if (headless)
				chromeOptions.addArguments("--headless=new");
				chromeOptions.addArguments("--window-size=1920,1080");
				chromeOptions.addArguments("--disable-gpu");
				chromeOptions.addArguments("--no-sandbox");
				chromeOptions.addArguments("--disable-dev-shm-usage");
			
			if (incognito)
				chromeOptions.addArguments("--incognito");
				chromeOptions.addArguments("--disable-gpu");
				driver = new ChromeDriver(chromeOptions);
			break;

		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			if (headless)
				edgeOptions.addArguments("--headless=new");

			edgeOptions.addArguments("--window-size=1920,1080");
			edgeOptions.addArguments("--disable-gpu");
			edgeOptions.addArguments("--no-sandbox");
			edgeOptions.addArguments("--disable-dev-shm-usage");
			if (incognito)
				edgeOptions.addArguments("--inprivate");
			driver = new EdgeDriver(edgeOptions);
			break;

		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			if (headless)
				firefoxOptions.addArguments("--headless");

			firefoxOptions.addArguments("--window-size=1920,1080");
			firefoxOptions.addArguments("--disable-gpu");
			firefoxOptions.addArguments("--no-sandbox");
			firefoxOptions.addArguments("--disable-dev-shm-usage");

			driver = new FirefoxDriver(firefoxOptions);
			break;

		default:
			throw new IllegalArgumentException("Unsupported local browser: " + browser);
		}

		configureDriver(driver);
		DriverManager.setDriver(driver);
	}

	/**
	 * Initialize REMOTE WebDriver (Selenium Grid). Headless/incognito are NOT
	 * applied unless needed by environment.
	 */
	public static void initRemoteDriver(String os, String browser, String gridUrl) throws MalformedURLException {
		logger.info("Initializing REMOTE WebDriver for {} on OS {}", browser, os);

		WebDriver driver;

		switch (browser.toLowerCase()) {

		case "chrome":
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
			break;

		case "firefox":
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(new URL(gridUrl), firefoxOptions);
			break;

		case "microsoftedge":
		case "edge":
			EdgeOptions edgeOptions = new EdgeOptions();
			edgeOptions.setCapability("browserName", "MicrosoftEdge");
			edgeOptions.setPlatformName(os.toUpperCase());
			driver = new RemoteWebDriver(new URL(gridUrl), edgeOptions);
			break;

		default:
			throw new IllegalArgumentException("Unsupported remote browser: " + browser);
		}

		configureDriver(driver);
		DriverManager.setDriver(driver);
	}

	/**
	 * Common driver configuration: maximize, delete cookies, implicit wait.
	 */
	private static void configureDriver(WebDriver driver) {
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
}
