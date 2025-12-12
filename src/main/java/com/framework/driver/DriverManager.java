package com.framework.driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * DriverManager manages a ThreadLocal WebDriver instance for thread safety.
 */
public final class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);

    // Thread-safe WebDriver holder for parallel execution
    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    // Prevent object creation
    private DriverManager() {}

    /**
     * Returns the WebDriver associated with the current thread.
     */
    public static WebDriver getDriver() {
        WebDriver driver = tlDriver.get();
        if (driver == null) {
            logger.error("Attempted to access WebDriver before initialization in this thread!");
            throw new IllegalStateException("WebDriver is not initialized for this thread.");
        }
        return driver;
    }

    /**
     * Sets the WebDriver instance for the current thread.
     */
    public static void setDriver(WebDriver driver) {
        logger.info("Setting WebDriver for thread: {}", Thread.currentThread().getId());
        tlDriver.set(driver);
    }

    /**
     * Removes WebDriver instance from ThreadLocal storage.
     */
    public static void removeDriver() {
        logger.info("Removing WebDriver for thread: {}", Thread.currentThread().getId());
        tlDriver.remove();
    }

    /**
     * Quits and cleans up the WebDriver instance for the current thread.
     */
    public static void cleanUp() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            logger.info("Quitting WebDriver for thread: {}", Thread.currentThread().getId());
            driver.quit();
            tlDriver.remove();
        } else {
            logger.warn("cleanUp() called but no WebDriver found for thread: {}", Thread.currentThread().getId());
        }
    }
}
