package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<String> tlBrowser = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for this thread.");
        }
        return driver;
    }

    public static void setDriver(WebDriver driver, String browserName) {
        logger.info("Setting WebDriver for thread {} | Browser: {}",
                Thread.currentThread().getId(), browserName);
        tlDriver.set(driver);
        tlBrowser.set(browserName);
    }

    public static String getBrowserName() {
        return tlBrowser.get();
    }

    public static String getBrowserVersion() {
        try {
            return ((RemoteWebDriver) getDriver())
                    .getCapabilities()
                    .getBrowserVersion();
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public static void cleanUp() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            logger.info("Quitting WebDriver for thread {}", Thread.currentThread().getId());
            driver.quit();
        }
        tlDriver.remove();
        tlBrowser.remove();
    }
}
