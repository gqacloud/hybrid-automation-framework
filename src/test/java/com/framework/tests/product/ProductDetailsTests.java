package com.framework.tests.product;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.model.ProductData;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.data.JsonDataReader;

public class ProductDetailsTests extends BaseTest {

	// ======================================================
	// COMMON PRE-CONDITION: Login
	// ======================================================
	private void loginToApplication() {

		HomePage home = new HomePage();
		home.clickOnMyAccount();
		home.clickOnLogin();

		LoginPage login = new LoginPage();
		login.login(prop.getProperty("email"), prop.getProperty("password"));
	}

	// ======================================================
	// SCENARIO 1: Verify Product Details (POSITIVE)
	// ======================================================
	@Test(groups = { "Smoke", "Regression", "Product", "CoreFlow" })
	public void shouldDisplayCorrectProductDetails() {

		// -------------------- Test Data --------------------
		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		// -------------------- Login --------------------
		loginToApplication();

		// -------------------- Search & Navigate --------------------
		SearchPage search = new SearchPage();
		search.searchProduct(data.getProductName(), data.getProductCategory(), true);
		search.clickOnProduct();

		// -------------------- Product Details Validation --------------------
		ProductDetailsPage product = new ProductDetailsPage();

		Assert.assertEquals(product.getProductName(), data.getProductName(), "Product name mismatch");
		Assert.assertEquals(product.getBrand(), data.getBrand(), "Brand mismatch");
		Assert.assertEquals(product.getProductCode(), data.getProductCode(), "Product code mismatch");
		Assert.assertEquals(product.getRewardPoints(), data.getRewardPoints(), "Reward points mismatch");
		Assert.assertEquals(product.getAvailability(), data.getAvailability(), "Availability mismatch");
		Assert.assertEquals(product.getDisplayedPrice(), data.getPrice(), "Price mismatch");
	}

	// ======================================================
	// SCENARIO 2: Add Product to Cart from Details Page
	// ======================================================
	@Test(groups = { "Regression", "Product", "CoreFlow" })
	public void shouldAddProductToCartFromProductDetailsPage() {

		// -------------------- Test Data --------------------
		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		// -------------------- Login --------------------
		loginToApplication();

		// -------------------- Search & Navigate --------------------
		SearchPage search = new SearchPage();
		search.searchProduct(data.getProductName(), data.getProductCategory(), true);
		search.clickOnProduct();

		// -------------------- Add to Cart --------------------
		ProductDetailsPage product = new ProductDetailsPage();

		product.selectDeliveryDate(data.getDeliveryMonth(), data.getDeliveryYear(), data.getDeliveryDay());

		product.addProductToCart();

		// Optional validation (if available in your page)
		Assert.assertTrue(product.isAddToCartSuccessMessageDisplayed(), "Add to cart success message not displayed.");
	}
}
