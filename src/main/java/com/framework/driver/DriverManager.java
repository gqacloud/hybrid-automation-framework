package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public final class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static final ThreadLocal<String> tlBrowser = new ThreadLocal<>();

    // ===== ADDED FOR BADGES =====
    private static final ThreadLocal<Boolean> tlHeadless = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> tlIncognito = new ThreadLocal<>();
    // ============================

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for this thread.");
        }
        return driver;
    }

    public static void setDriver(WebDriver driver, String browserName) {
        logger.info("Thread: {} | Browser: {}",
                Thread.currentThread().getId(),
                browserName);

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

    // ===== BADGE SUPPORT =====
    public static void setRunMode(boolean headless, boolean incognito) {
        tlHeadless.set(headless);
        tlIncognito.set(incognito);
    }

    public static boolean isHeadless() {
        return Boolean.TRUE.equals(tlHeadless.get());
    }

    public static boolean isIncognito() {
        return Boolean.TRUE.equals(tlIncognito.get());
    }
    // =========================

    public static void cleanUp() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
        }
        tlDriver.remove();
        tlBrowser.remove();
        tlHeadless.remove();
        tlIncognito.remove();
    }
}
