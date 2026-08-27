package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverManager {

    private static final Logger logger =
            LogManager.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<String> tlBrowser = new ThreadLocal<>();

    // ===== EXECUTION MODE =====
    private static final ThreadLocal<Boolean> tlHeadless = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> tlIncognito = new ThreadLocal<>();

    private DriverManager() {}

    // ===============================================================
    // DRIVER
    // ===============================================================
    public static boolean hasDriver() {
        return tlDriver.get() != null;
    }

    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for this thread.");
        }
        return driver;
    }

    public static void setDriver(WebDriver driver, String browserName) {
        tlDriver.set(driver);
        tlBrowser.set(browserName);

        logger.info(
                "Thread: {} | Browser: {} | Headless: {} | Incognito: {}",
                Thread.currentThread().getId(),
                browserName,
                isHeadless(),
                isIncognito()
        );
    }

    // ===============================================================
    // BROWSER INFO (DO NOT USE CAPABILITIES FOR NAME)
    // ===============================================================
    public static String getBrowserName() {
        return tlBrowser.get() != null ? tlBrowser.get() : "Unknown";
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

    // ===============================================================
    // EXECUTION MODE
    // ===============================================================
    public static void setRunMode(boolean headless, boolean incognito) {
        tlHeadless.set(headless);
        tlIncognito.set(incognito);
    }

    public static boolean isHeadless() {
        return tlHeadless.get() != null && tlHeadless.get();
    }

    public static boolean isIncognito() {
        return tlIncognito.get() != null && tlIncognito.get();
    }

    // ===============================================================
    // CLEANUP
    // ===============================================================
    public static void cleanUp() {
        try {
            WebDriver driver = tlDriver.get();
            if (driver != null) {
                driver.quit();
            }
        } finally {
            tlDriver.remove();
            tlBrowser.remove();
            tlHeadless.remove();
            tlIncognito.remove();
        }
    }
}
