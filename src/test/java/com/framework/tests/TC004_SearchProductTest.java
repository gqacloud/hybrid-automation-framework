package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.model.ProductData;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import com.framework.utils.JsonDataReader;

public class TC004_SearchProductTest extends BaseClass {

	@Test(groups = "Functional")
	public void searchProduct() throws InterruptedException {

		logger.info("===== TC004 Search Product Test Started =====");

		try {
			HomePage homepage = new HomePage(DriverManager.getDriver());

			logger.info("Navigating to My Account");
			homepage.clickOnMyAccount();

			logger.info("Clicking on Login");
			homepage.clickOnLogin();

			LoginPage loginpage = new LoginPage(DriverManager.getDriver());

			logger.info("Entering email: {}", prop.getProperty("email"));
			loginpage.setUserEmail(prop.getProperty("email"));

			logger.info("Entering password");
			loginpage.setUserPassword(prop.getProperty("password"));

			logger.info("Clicking on Login button");
			loginpage.clickOnLogin();

			SearchPage searchpage = new SearchPage(DriverManager.getDriver());

			ProductData searchData = JsonDataReader.loadJson(jsonPath,ProductData.class);
					

			logger.info("Entering product to search: {}", searchData.productName);
			searchpage.enterProductToSearch(searchData.productName);

			logger.info("Clicking on Search button");
			searchpage.clickOnSearch();

			logger.info("Selecting category:{}", searchData.ProductCategory);
			searchpage.selectProductCategory(searchData.ProductCategory);

			logger.info("Selecting 'Search in product descriptions'");
			searchpage.clickOnProductDescriptionCheckbox();

			logger.info("Clicking inner Search button");
			searchpage.clickOnInnerSearch();

			logger.info("Getting and validating the product name: {}",searchData.productName);
			searchpage.confirmProdName();
			Assert.assertEquals(searchpage.confirmProdName(), searchData.productName);

		} catch (Exception e) {

			logger.error("Unexpected Exception Occurred During Test Execution", e);
			logger.error("Test FAILED due to an unhandled exception.");

			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
			throw e;
		} finally {
			logger.info("===== TC004 Search Product Test Completed Successfully =====");
		}
	}
}
