package com.framework.utils.reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.base.BaseTest;
import com.framework.driver.DriverManager;

public class ExtentReportListener implements ITestListener {

    // ===============================================================
    // LOGGER (Lifecycle only)
    // ===============================================================
    private static final Logger logger =
            LogManager.getLogger("TEST-LIFECYCLE");

    // ===============================================================
    // EXTENT OBJECTS
    // ===============================================================
    private ExtentReports extent;
    private ExtentSparkReporter sparkReporter;

    // Thread-safe ExtentTest (mandatory for parallel runs)
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static ExtentTest getTest() {
        return test.get();
    }

    // ===============================================================
    // SUITE START
    // ===============================================================
    @Override
    public void onStart(ITestContext context) {

        new File("./reports/").mkdirs();

        String timeStamp =
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                        .format(new Date());

        String reportName = "Test-Report-" + timeStamp + ".html";

        sparkReporter =
                new ExtentSparkReporter("./reports/" + reportName);

        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setTheme(Theme.STANDARD);

        String env = System.getProperty("env", "QA");

        sparkReporter.config().setReportName(
                "Automation Execution"
                        + " <span style='padding:3px 8px;"
                        + "font-size:12px;border-radius:12px;"
                        + "background:#27ae60;color:white;"
                        + "margin-left:8px;'>"
                        + env
                        + "</span>"
        );

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        extent.setSystemInfo("Environment", env);

        logger.info("===== TEST SUITE STARTED =====");
    }

    // ===============================================================
    // TEST START (SCENARIO LEVEL)
    // ===============================================================
    @Override
    public void onTestStart(ITestResult result) {

        result.setAttribute("startTime", System.currentTimeMillis());

        // -----------------------------------------------------------
        // SCENARIO NAME (CLASS + METHOD [+ DATA])
        // -----------------------------------------------------------
        String testName =
                result.getTestClass()
                        .getRealClass()
                        .getSimpleName()
                        + "." + result.getMethod().getMethodName();

        if (result.getParameters() != null
                && result.getParameters().length > 0) {
            testName += " " + Arrays.toString(result.getParameters());
        }

        ExtentTest extentTest = extent.createTest(testName);

        // Assign TestNG groups as categories
        extentTest.assignCategory(result.getMethod().getGroups());

        test.set(extentTest);

        // -----------------------------------------------------------
        // DEVICE / EXECUTION INFO
        // -----------------------------------------------------------
        String browser = DriverManager.getBrowserName();
        String version = DriverManager.getBrowserVersion();

        extentTest.assignDevice(browser + " " + version);

        extentTest.info(
                "<b>" + browser + " " + version + "</b>"
                        + getModeBadges()
        );
    }

    // ===============================================================
    // TEST SUCCESS
    // ===============================================================
    @Override
    public void onTestSuccess(ITestResult result) {
        logCompletion(result, Status.PASS, null);
        attachExecutionLog();
    }

    // ===============================================================
    // TEST FAILURE
    // ===============================================================
    @Override
    public void onTestFailure(ITestResult result) {

        logCompletion(result, Status.FAIL, result.getThrowable());

        try {
            String screenshotPath =
                    BaseTest.captureScreen(
                            result.getMethod().getMethodName()
                    );

            getTest().fail(
                    "Screenshot on Failure",
                    MediaEntityBuilder
                            .createScreenCaptureFromPath(screenshotPath)
                            .build()
            );

        } catch (Exception e) {
            logger.error("Screenshot attachment failed", e);
        }

        attachExecutionLog();
    }

    // ===============================================================
    // TEST SKIPPED
    // ===============================================================
    @Override
    public void onTestSkipped(ITestResult result) {
        logCompletion(result, Status.SKIP, result.getThrowable());
        attachExecutionLog();
    }

    // ===============================================================
    // SUITE FINISH
    // ===============================================================
    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        logger.info("===== TEST SUITE FINISHED =====");
    }

    // ===============================================================
    // COMMON COMPLETION LOGIC
    // ===============================================================
    private void logCompletion(ITestResult result,
                               Status status,
                               Throwable error) {

        long duration =
                System.currentTimeMillis()
                        - (long) result.getAttribute("startTime");

        double seconds = duration / 1000.0;
        String methodName = result.getMethod().getMethodName();

        if (status == Status.PASS) {
            logger.info(
                    "===== TEST PASSED: {} ({} sec) =====",
                    methodName,
                    String.format("%.2f", seconds)
            );
        } else if (status == Status.SKIP) {
            logger.warn(
                    "===== TEST SKIPPED: {} ({} sec) =====",
                    methodName,
                    String.format("%.2f", seconds)
            );
        } else {
            logger.error(
                    "===== TEST FAILED: {} ({} sec) =====",
                    methodName,
                    String.format("%.2f", seconds),
                    error
            );
        }

        getTest().log(
                status,
                methodName + " "
                        + status.toString().toLowerCase()
        );
    }

    // ===============================================================
    // ATTACH EXECUTION LOG FILE
    // ===============================================================
    private void attachExecutionLog() {

        String runId = System.getProperty("runId");
        if (runId == null) {
            return;
        }

        String logPath =
                System.getProperty("user.dir")
                        + "/logs/"
                        + runId
                        + "/automation.log";

        File logFile = new File(logPath);

        if (logFile.exists()) {
            getTest().info(
                    "Execution Log: <a href='file:///"
                            + logFile.getAbsolutePath()
                            + "' target='_blank'>automation.log</a>"
            );
        } else {
            logger.warn("Log file not found: {}", logPath);
        }
    }

    // ===============================================================
    // EXECUTION BADGES
    // ===============================================================
    private String getModeBadges() {

        StringBuilder badge = new StringBuilder();

        if (DriverManager.isHeadless()) {
            badge.append(
                    "<span style='background:#2c3e50;"
                            + "color:white;padding:3px 8px;"
                            + "border-radius:10px;font-size:11px;"
                            + "margin-left:8px;'>HEADLESS</span>"
            );
        }

        if (DriverManager.isIncognito()) {
            badge.append(
                    "<span style='background:#8e44ad;"
                            + "color:white;padding:3px 8px;"
                            + "border-radius:10px;font-size:11px;"
                            + "margin-left:5px;'>INCOGNITO</span>"
            );
        }

        return badge.toString();
    }
}
