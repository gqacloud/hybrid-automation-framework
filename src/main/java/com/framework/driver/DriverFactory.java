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

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    // ================= LOCAL =================
    public static void initLocalDriver(String browser, boolean headless, boolean incognito) {

        WebDriver driver;

        switch (browser.toLowerCase()) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) chromeOptions.addArguments("--headless=new");
                if (incognito) chromeOptions.addArguments("--incognito");
                chromeOptions.addArguments("--window-size=1920,1080");

                driver = new ChromeDriver(chromeOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Chrome");
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless=new");
                if (incognito) edgeOptions.addArguments("--inprivate");

                driver = new EdgeDriver(edgeOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Edge");
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) firefoxOptions.addArguments("--headless");

                driver = new FirefoxDriver(firefoxOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Firefox");
                break;

            default:
                throw new IllegalArgumentException("Unsupported local browser: " + browser);
        }
    }

    // ================= REMOTE =================
    public static void initRemoteDriver(String os, String browser, String gridUrl)
            throws MalformedURLException {

        WebDriver driver;
        URL remoteURL = new URL(gridUrl);

        switch (browser.toLowerCase()) {

            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setPlatformName(os.toUpperCase());
                driver = new RemoteWebDriver(remoteURL, chromeOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Chrome");
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setPlatformName(os.toUpperCase());
                driver = new RemoteWebDriver(remoteURL, firefoxOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Firefox");
                break;

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPlatformName(os.toUpperCase());
                driver = new RemoteWebDriver(remoteURL, edgeOptions);
                configureDriver(driver);
                DriverManager.setDriver(driver, "Edge");
                break;

            default:
                throw new IllegalArgumentException("Unsupported remote browser: " + browser);
        }
    }

    private static void configureDriver(WebDriver driver) {
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
}
