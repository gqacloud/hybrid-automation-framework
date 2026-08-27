package com.framework.utils.reports;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.base.BaseTest;
import com.framework.driver.DriverManager;

public class ExtentReportListener implements ITestListener {

    private static final Logger logger =
            LogManager.getLogger("TEST-LIFECYCLE");

    private ExtentReports extent;
    private ExtentSparkReporter spark;

    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    private ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext context) {

        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        new File("./reports/").mkdirs();

        String time =
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
                        .format(new Date());

        spark = new ExtentSparkReporter(
                "./reports/Test-Report-" + time + ".html");

        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Automation Report");
        spark.config().setTimeStampFormat(
                "MMM dd, yyyy hh:mm:ss a z");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java", System.getProperty("java.version"));
        String qaName =
                System.getProperty(
                        "qa.name",
                        System.getProperty("user.name")
                );

        extent.setSystemInfo("QA Engineer", qaName);
    }

    @Override
    public void onTestStart(ITestResult result) {

        result.setAttribute("startTime",
                System.currentTimeMillis());

        String name =
                result.getTestClass().getRealClass().getSimpleName()
                        + "." + result.getMethod().getMethodName();

        if (result.getParameters().length > 0) {
            name += Arrays.toString(result.getParameters());
        }

        ExtentTest et = extent.createTest(name);
        et.assignCategory(result.getMethod().getGroups());

        test.set(et);

        String browser = DriverManager.getBrowserName();
        String version = DriverManager.getBrowserVersion();

        et.assignDevice(browser + " " + version);
        et.info("<b>" + browser + " " + version + "</b>"
                + getBadges());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logResult(result, Status.PASS, null);
        test.remove();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logResult(result, Status.FAIL, result.getThrowable());

        String path =
                BaseTest.captureScreen(
                        result.getMethod().getMethodName());

        getTest().fail("Screenshot",
                MediaEntityBuilder
                        .createScreenCaptureFromPath(path)
                        .build());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logResult(result, Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        test.remove();
    }

    private void logResult(
            ITestResult result,
            Status status,
            Throwable error) {

        long duration =
                System.currentTimeMillis()
                        - (long) result.getAttribute("startTime");

        getTest().log(
                status,
                result.getMethod().getMethodName()
                        + " " + status.toString().toLowerCase()
                        + " (" + duration / 1000.0 + " sec)");

        if (error != null) {
            getTest().log(status, error);
        }
    }

    private String getBadges() {

        StringBuilder sb = new StringBuilder();

        if (DriverManager.isHeadless()) {
            sb.append(
                    "<span style='background:#2c3e50;"
                            + "color:white;padding:3px 8px;"
                            + "border-radius:10px;"
                            + "margin-left:8px;'>HEADLESS</span>");
        }

        if (DriverManager.isIncognito()) {
            sb.append(
                    "<span style='background:#8e44ad;"
                            + "color:white;padding:3px 8px;"
                            + "border-radius:10px;"
                            + "margin-left:5px;'>INCOGNITO</span>");
        }
        return sb.toString();
    }
}
