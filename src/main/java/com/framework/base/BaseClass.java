package com.framework.base;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.framework.driver.DriverFactory;
import com.framework.driver.DriverManager;

public class BaseClass {

    public Logger logger;
    public Properties prop;
    public static final String userDir = System.getProperty("user.dir");
    public static final String excelPath = userDir + "/testData/Users.xlsx";
    public static final String jsonPath=userDir + "/src/test/resources/ProductData.json";
    public static final String propertyfilePath=userDir +"/src/main/resources/config.properties";

    @BeforeClass(groups = { "Sanity", "Regression", "Master", "Functional" })
    @Parameters({ "os", "browser" })
    public void setup(String os, String browserName) throws IOException {

        logger = LogManager.getLogger(this.getClass());

        // Load config.properties
        FileReader file = new FileReader(propertyfilePath);
        prop = new Properties();
        prop.load(file);

        String exeEnv = prop.getProperty("exe_env").trim();
        String appUrl = prop.getProperty("url").trim();
        String gridUrl = prop.getProperty("grid_url").trim();

        boolean headless = Boolean.parseBoolean(prop.getProperty("headless").trim());
        boolean incognito = Boolean.parseBoolean(prop.getProperty("incognito").trim());

        logger.info("Execution Environment: {}", exeEnv);
        logger.info("Browser: {}", browserName);
        logger.info("OS: {}", os);

        if (exeEnv.equalsIgnoreCase("local")) {
            // Use headless/incognito only for LOCAL execution
            DriverFactory.initLocalDriver(browserName, headless, incognito);
        }
        else if (exeEnv.equalsIgnoreCase("remote")) {
            // Remote Grid execution
            try {
                DriverFactory.initRemoteDriver(os, browserName, gridUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize RemoteWebDriver", e);
            }
        }
        else {
            throw new IllegalArgumentException("Invalid exe_env value. Use: local / remote");
        }

        WebDriver driver = DriverManager.getDriver();
        driver.get(appUrl);

        logger.info("Navigated to URL: {}", appUrl);
    }

    @AfterClass(groups = { "Sanity", "Regression", "Master", "Functional" })
    public void teardown() {
        logger.info("Closing WebDriver");
        DriverManager.cleanUp();
    }

    /**
     * Capture screenshot and return its file path
     */
    public String captureScreen(String tname) {

        WebDriver driver = DriverManager.getDriver();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String screenshotsDir = userDir + "/screenshots/";
        File dir = new File(screenshotsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String destFilePath = screenshotsDir + tname + "_" + timeStamp + ".png";
        File destFile = new File(destFilePath);

        try {
            FileUtils.copyFile(sourceFile, destFile);
        } catch (Exception e) {
            return e.getMessage();
        }

        return destFilePath;
    }
}
