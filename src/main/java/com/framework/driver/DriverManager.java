package com.framework.driver;

import org.openqa.selenium.WebDriver;

/**
 * DriverManager is responsible for maintaining a ThreadLocal WebDriver instance.
 * This ensures thread safety during parallel execution.
 */
public class DriverManager {

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    /**
     * Returns WebDriver for the current thread.
     */
    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    /**
     * Sets WebDriver instance for the current thread.
     */
    public static void setDriver(WebDriver driver) {
        tlDriver.set(driver);
    }

    /**
     * Removes WebDriver from ThreadLocal after quitting the browser.
     */
    public static void removeDriver() {
        tlDriver.remove();
    }

    /**
     * Quits and cleans up the WebDriver properly.
     */
    public static void cleanUp() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            driver.quit();
            tlDriver.remove();
        }
    }
}
