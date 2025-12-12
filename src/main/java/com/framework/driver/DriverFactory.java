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

    // ===============================================================
    // LOCAL DRIVER INITIALIZATION
    // ===============================================================
    public static void initLocalDriver(String browser, boolean headless, boolean incognito) {

        logger.info("Initializing LOCAL WebDriver: {}", browser);

        WebDriver driver;

        switch (browser.toLowerCase()) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless) {
                    logger.info("Chrome: Headless Enabled");
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }
                if (incognito) {
                    logger.info("Chrome: Incognito Enabled");
                    chromeOptions.addArguments("--incognito");
                }

                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");

                driver = new ChromeDriver(chromeOptions);
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();

                if (headless) {
                    logger.info("Edge: Headless Enabled");
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                }
                if (incognito) {
                    logger.info("Edge: InPrivate Enabled");
                    edgeOptions.addArguments("--inprivate");
                }

                edgeOptions.addArguments("--disable-gpu");
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-dev-shm-usage");

                driver = new EdgeDriver(edgeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless) {
                    logger.info("Firefox: Headless Enabled");
                    firefoxOptions.addArguments("--headless");
                }

                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");

                driver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported local browser: " + browser);
        }

        configureDriver(driver);
        DriverManager.setDriver(driver);
    }

    // ===============================================================
    // REMOTE DRIVER INITIALIZATION (SELENIUM GRID)
    // ===============================================================
    public static void initRemoteDriver(String os, String browser, String gridUrl)
            throws MalformedURLException {

        logger.info("Initializing REMOTE WebDriver: {} on OS: {}", browser, os);

        if (gridUrl == null || gridUrl.isBlank()) {
            throw new IllegalArgumentException("Grid URL is missing or empty.");
        }

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
            case "microsoftedge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPlatformName(os.toUpperCase());
                edgeOptions.setCapability("browserName", "MicrosoftEdge");
                driver = new RemoteWebDriver(remoteURL, edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }

        configureDriver(driver);
        DriverManager.setDriver(driver);
    }

    // ===============================================================
    // COMMON DRIVER CONFIGURATION
    // ===============================================================
    private static void configureDriver(WebDriver driver) {
        logger.info("Configuring WebDriver defaults");

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
}
