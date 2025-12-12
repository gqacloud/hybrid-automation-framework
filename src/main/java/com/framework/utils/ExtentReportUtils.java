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

    private static final Logger logger = LogManager.getLogger(ExtentReportUtils.class);

    private ExtentSparkReporter sparkReporter;
    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private String repName;

    private static ExtentTest getTest() {
        return test.get();
    }

    @Override
    public void onStart(ITestContext context) {

        File reportDir = new File("./reports/");
        if (!reportDir.exists()) {
            reportDir.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        repName = "Test-Report-" + timeStamp + ".html";

        sparkReporter = new ExtentSparkReporter("./reports/" + repName);
        sparkReporter.config().setDocumentTitle("Automation Report");
        sparkReporter.config().setReportName("Functional Test Execution");
        sparkReporter.config().setTheme(Theme.STANDARD);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("OS", context.getCurrentXmlTest().getParameter("os"));
        extent.setSystemInfo("Browser", context.getCurrentXmlTest().getParameter("browser"));
    }

    @Override
    public void onTestStart(ITestResult result) {

        long startTime = System.currentTimeMillis();
        result.setAttribute("startTime", startTime);

        logger.info("Starting test: {}", result.getName());
        logger.info("Groups: {}", Arrays.toString(result.getMethod().getGroups()));

        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        extentTest.assignCategory(result.getMethod().getGroups());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = System.currentTimeMillis() - (long) result.getAttribute("startTime");
        double seconds = duration / 1000.0;

        logger.info("PASS: {} ({}s)", result.getName(), String.format("%.2f", seconds));

        getTest().log(Status.PASS, result.getName() + " passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        long duration = System.currentTimeMillis() - (long) result.getAttribute("startTime");
        double seconds = duration / 1000.0;

        logger.error("FAIL: {} ({}s)", result.getName(), String.format("%.2f", seconds));

        getTest().log(Status.FAIL, result.getName() + " failed");
        getTest().log(Status.INFO, result.getThrowable().getMessage());

        try {
            String imgPath = BaseClass.captureScreen(result.getName());
            getTest().addScreenCaptureFromPath(imgPath);
        } catch (Exception e) {
            logger.error("Failed to attach screenshot: {}", e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        long duration = System.currentTimeMillis() - (long) result.getAttribute("startTime");
        double seconds = duration / 1000.0;

        logger.warn("SKIP: {} ({}s)", result.getName(), String.format("%.2f", seconds));

        getTest().log(Status.SKIP, result.getName() + " skipped");

        if (result.getThrowable() != null) {
            getTest().log(Status.INFO, result.getThrowable().getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        extent.flush();

        File reportFile = new File("./reports/" + repName);

        // Auto-open disabled by your request:
        // if (Desktop.isDesktopSupported()) {
        //     Desktop.getDesktop().browse(reportFile.toURI());
        // }
    }
}
