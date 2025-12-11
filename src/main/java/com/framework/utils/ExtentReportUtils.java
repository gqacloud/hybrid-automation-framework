package com.framework.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
// Extent report 5.x... version
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

	// Declare ExtentReport objects
	public ExtentSparkReporter sparkReporter; // Spark reporter that writes the report
	public ExtentReports extent; // ExtentReports object for managing the report
	public ExtentTest test; // Represents the test in the Extent report

	String repName; // Name of the report file

	// onStart method is invoked when the test suite starts
	public void onStart(ITestContext testContext) {

		// Step 1: Create report directory if it doesn't exist
		File reportDir = new File(".\\reports\\");
		if (!reportDir.exists()) {
			reportDir.mkdir(); // Create the "reports" directory if it doesn't exist
		}

		// Step 2: Generate a unique report name with a timestamp
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); // Get the current timestamp
		repName = "Test-Report-" + timeStamp + ".html"; // Construct the report name with timestamp

		// Step 3: Initialize ExtentSparkReporter to generate the report at a specified
		// location
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName); // Set the path for the report file
		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Set the document title of the report
		sparkReporter.config().setReportName("opencart Functional Testing"); // Set the name of the report
		sparkReporter.config().setTheme(Theme.STANDARD); // Set the theme of the report (Standard theme)

		// Step 4: Initialize ExtentReports and attach the spark reporter to it
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter); // Attach the spark reporter to the ExtentReports object

		extent.setSystemInfo("Application", "opencart"); // Set the application name
		extent.setSystemInfo("Module", "Admin"); // Set the module name
		extent.setSystemInfo("Sub Module", "Customers"); // Set the sub-module name
		extent.setSystemInfo("User Name", System.getProperty("user.name")); // Set the username of the system executing the test
		extent.setSystemInfo("Environment", "QA"); // Set the environment (here it’s set to QA)
		String os = testContext.getCurrentXmlTest().getParameter("os"); // Get OS from test XML parameters
		extent.setSystemInfo("Operating System", os); // Set the OS info in the report
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser"); // Get browser from test XML parameters
		extent.setSystemInfo("Browser", browser); // Set the browser info in the report

		
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups(); // Get the groups of tests
		if (!includedGroups.isEmpty()) {
			extent.setSystemInfo("Groups", includedGroups.toString()); // Add groups to system info in report
		}
	}

	// onTestSuccess method is invoked when a test passes
	public void onTestSuccess(ITestResult result) {
		
		test = extent.createTest(result.getTestClass().getName()); // Create a test with the test class name
		test.assignCategory(result.getMethod().getGroups()); // Assign test groups to the report
		test.log(Status.PASS, result.getName() + " got successfully executed"); // Log success message
	}

	// onTestFailure method is invoked when a test fails
	public void onTestFailure(ITestResult result) {

		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, result.getName() + " got failed"); // Log failure message
		test.log(Status.INFO, result.getThrowable().getMessage()); // Log the exception message

		
		String imgPath = new BaseClass().captureScreen(result.getName()); // Capture screenshot for the failed test
		test.addScreenCaptureFromPath(imgPath); // Add the screenshot to the report

	}

	// onTestSkipped method is invoked when a test is skipped
	public void onTestSkipped(ITestResult result) {

		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName() + " got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	// onFinish method is invoked when the test suite finishes
	public void onFinish(ITestContext testContext) {

		extent.flush(); // Write all logs and test results to the report file
		
		String pathOfExtentReport = System.getProperty("user.dir") + "\\reports\\" + repName; // Generate the file path for the report
		File extentReport = new File(pathOfExtentReport); // Create a File object for the report
		try {
			Desktop.getDesktop().browse(extentReport.toURI()); // Open the report in the default browser
		} catch (IOException e) {
			e.printStackTrace(); // Handle IOException if there is an issue opening the report
		}

		/*
		 * Uncomment and modify the email functionality if needed. Ensure that email
		 * credentials are securely handled through environment variables.
		 */
		/*
		 * 
		 * try { String emailPassword = System.getenv("EMAIL_PASSWORD"); // Get email
		 * password from environment variable
		 * 
		 * URL url = new URL("file:///" + System.getProperty("user.dir") + "\\reports\\"
		 * + repName); ImageHtmlEmail email = new ImageHtmlEmail();
		 * email.setDataSourceResolver(new DataSourceUrlResolver(url));
		 * email.setHostName("smtp.googlemail.com"); email.setSmtpPort(465);
		 * email.setAuthenticator(new DefaultAuthenticator("your-email@gmail.com",
		 * emailPassword)); // Use environment variable for password
		 * email.setSSLOnConnect(true); email.setFrom("your-email@gmail.com"); // Sender
		 * email.setSubject("Test Results");
		 * email.setMsg("Please find the attached report...");
		 * email.addTo("recipient-email@example.com"); // Receiver email.attach(url,
		 * "Extent Report", "Please check the attached report..."); email.send(); //
		 * Send the email } catch (Exception e) { e.printStackTrace(); // Handle any
		 * exception that occurs while sending the email }
		 */
	}
}
