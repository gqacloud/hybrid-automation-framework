package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.model.ProductData;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.JsonDataReader;

public class TC005_ProductDetailsTest extends BaseClass {

	@Test(groups = "Functional")
	public void verifyProductDetails() throws Exception {
		logger.info("===== TC005 Product Details Test started =====");
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

			ProductData searchData = JsonDataReader.loadJson(jsonPath, ProductData.class);

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

//			logger.info("Getting and validating the product name: {}",searchData.productName);
//			searchpage.confirmProdName();
//			Assert.assertEquals(searchpage.confirmProdName(), searchData.productName);

			searchpage.clickOnProduct();

			ProductDetailsPage product = new ProductDetailsPage(DriverManager.getDriver());

			logger.info("Validating Product Name: {}", searchData.productName);
			product.getProductName();
			Assert.assertEquals(product.getProductName(), searchData.productName);

			logger.info("Validating Product Brand: {}", searchData.brand);
			product.getBrand();
			Assert.assertEquals(product.getBrand(), searchData.brand);

			logger.info("Validating Product Code: {}", searchData.productCode);
			product.getProductCode();
			Assert.assertEquals(product.getProductCode(), searchData.productCode);

			logger.info("Validating Product Code: {}", searchData.rewardPoints);
			product.getRewardpoints();
			Assert.assertEquals(product.getRewardpoints(), searchData.rewardPoints);

			logger.info("Validating Product Code: {}", searchData.availability);
			product.getAvailability();
			Assert.assertEquals(product.getAvailability(), searchData.availability);

			logger.info("Validating Product Price: {}", searchData.price);
			product.getDisplayedPrice();
			Assert.assertEquals(product.getDisplayedPrice(), searchData.price);

			logger.info("selecting delivery date {}:", searchData.deliveryMonth, searchData.deliveryYear,
					searchData.deliveryDay);

			product.selectDeliveryDate(searchData.deliveryMonth, searchData.deliveryYear, searchData.deliveryDay);

			logger.info("Clicling on add to cart button");
			product.addProductToCart();

		} catch (Exception e) {

			logger.error("Unexpected Exception Occurred During Test Execution", e);
			logger.error("Test FAILED due to an unhandled exception.");

			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
			throw e;
		} finally {
			logger.info("===== TC005 Product Details Test Completed Successfully =====");
		}
	}
}
