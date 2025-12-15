package com.framework.utils;

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
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.base.BaseClass;

public class ExtentReportUtils implements ITestListener {

    // 🔥 Dedicated lifecycle logger (matches log4j2.xml)
    private static final Logger logger =
            LogManager.getLogger("TEST-LIFECYCLE");

    private ExtentReports extent;
    private ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private String reportName;

    private static ExtentTest getTest() {
        return test.get();
    }

    // ===============================================================
    // SUITE START
    // ===============================================================
    @Override
    public void onStart(ITestContext context) {

        File reportDir = new File("./reports/");
        reportDir.mkdirs();

        String timeStamp =
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        reportName = "Test-Report-" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter("./reports/" + reportName);
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setTheme(Theme.STANDARD);

        String env = System.getProperty("env", "QA");
        sparkReporter.config().setReportName(
                "Functional Test Execution <span style='padding:3px 8px;"
              + "font-size:12px; border-radius:12px; background:#27ae60;"
              + "color:white; margin-left:8px;'>" + env + "</span>"
        );

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System info (ONCE)
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));

        logger.info("===== TEST SUITE STARTED =====");
    }

    // ===============================================================
    // TEST START
    // ===============================================================
    @Override
    public void onTestStart(ITestResult result) {

        result.setAttribute("startTime", System.currentTimeMillis());

        String testName =
                result.getTestClass().getRealClass().getSimpleName();

        logger.info("===== TEST STARTED: {} =====", testName);
        logger.info("Groups: {}", Arrays.toString(result.getMethod().getGroups()));

        ExtentTest extentTest = extent.createTest(testName);
        extentTest.assignCategory(result.getMethod().getGroups());
        test.set(extentTest);
    }

    // ===============================================================
    // TEST PASS
    // ===============================================================
    @Override
    public void onTestSuccess(ITestResult result) {

        logCompletion(result, Status.PASS, null);
    }

    // ===============================================================
    // TEST FAIL
    // ===============================================================
    @Override
    public void onTestFailure(ITestResult result) {

        logCompletion(result, Status.FAIL, result.getThrowable());

        try {
            String imgPath = BaseClass.captureScreen(
                    result.getTestClass().getRealClass().getSimpleName());
            getTest().addScreenCaptureFromPath(imgPath);
        } catch (Exception e) {
            logger.error("Screenshot attach failed", e);
        }
    }

    // ===============================================================
    // TEST SKIP
    // ===============================================================
    @Override
    public void onTestSkipped(ITestResult result) {

        logCompletion(result, Status.SKIP, result.getThrowable());
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
    // COMMON COMPLETION HANDLER
    // ===============================================================
    private void logCompletion(ITestResult result,
                               Status status,
                               Throwable error) {

        long duration =
                System.currentTimeMillis()
                        - (long) result.getAttribute("startTime");

        String testName =
                result.getTestClass().getRealClass().getSimpleName();

        double seconds = duration / 1000.0;

        if (status == Status.PASS) {
            logger.info("===== TEST PASSED: {} ({} sec) =====",
                    testName, String.format("%.2f", seconds));
        } else if (status == Status.SKIP) {
            logger.warn("===== TEST SKIPPED: {} ({} sec) =====",
                    testName, String.format("%.2f", seconds));
        } else {
            logger.error("===== TEST FAILED: {} ({} sec) =====",
                    testName, String.format("%.2f", seconds), error);
        }

        getTest().log(status,
                testName + " " + status.toString().toLowerCase());
    }
}
