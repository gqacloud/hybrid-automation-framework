package com.framework.tests;

import org.testng.Assert;

import com.framework.base.BaseClass;
import com.framework.driver.DriverManager;
import com.framework.model.ProductData;
import com.framework.pages.CartPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.JsonDataReader;

public class TC00_PurchaseProductTest extends BaseClass {

	public void searchProductAddToCartAndCheckout() throws Exception {
		logger.info("===== TC00 Purchase Product Test started =====");
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

			logger.info("Entering product to search: {}", searchData.getProductName());
			searchpage.enterProductToSearch(searchData.getProductName());

			logger.info("Clicking on Search button");
			searchpage.clickOnSearch();

			logger.info("Selecting category:{}", searchData.getProductCategory());
			searchpage.selectProductCategory(searchData.getProductCategory());

			logger.info("Selecting 'Search in product descriptions'");
			searchpage.clickOnProductDescriptionCheckbox();

			logger.info("Clicking inner Search button");
			searchpage.clickOnInnerSearch();

			searchpage.clickOnProduct();

			ProductDetailsPage product = new ProductDetailsPage(DriverManager.getDriver());

			logger.info("Validating Product Name: {}", searchData.getProductName());
			product.getProductName();
			Assert.assertEquals(product.getProductName(), searchData.getProductName());

			logger.info("Validating Product Brand: {}", searchData.getBrand());
			product.getBrand();
			Assert.assertEquals(product.getBrand(), searchData.getBrand());

			logger.info("Validating Product Code: {}", searchData.getProductCode());
			product.getProductCode();
			Assert.assertEquals(product.getProductCode(), searchData.getProductCode());

			logger.info("Validating Product Code: {}", searchData.getRewardPoints());
			product.getRewardpoints();
			Assert.assertEquals(product.getRewardpoints(), searchData.getRewardPoints());

			logger.info("Validating Product Code: {}", searchData.getAvailability());
			product.getAvailability();
			Assert.assertEquals(product.getAvailability(), searchData.getAvailability());

			logger.info("Validating Product Price: {}", searchData.getPrice());
			product.getDisplayedPrice();
			Assert.assertEquals(product.getDisplayedPrice(), searchData.getPrice());

			logger.info("selecting delivery date {}:", searchData.getDeliveryMonth(), searchData.getDeliveryYear(),
					searchData.getDeliveryDay());

			product.selectDeliveryDate(searchData.getDeliveryMonth(), searchData.getDeliveryYear(),
					searchData.getDeliveryDay());

			logger.info("Clicling on add to cart button");
			product.addProductToCart();

			CartPage cart = new CartPage(DriverManager.getDriver());

			logger.info("Clicling on Cart headrer at the top");
			cart.clickOnCartHeader();
			
			logger.info("Getting shipping tax estimation");
			cart.getShippingTaxEstimation(searchData.getCountry(), searchData.getRegion(), searchData.getZipcode());
			

		} catch (Exception e) {

			logger.error("Unexpected Exception Occurred During Test Execution", e);
			logger.error("Test FAILED due to an unhandled exception.");

			Assert.fail("Test failed due to unexpected exception: " + e.getMessage());
			throw e;
		} finally {
			logger.info("===== TC00 Purchase Product Test Completed Successfully =====");
		}

	}

}
