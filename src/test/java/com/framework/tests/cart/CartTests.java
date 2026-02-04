package com.framework.tests.cart;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.model.ProductData;
import com.framework.pages.CartPage;
import com.framework.pages.CheckoutPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.data.JsonDataReader;

public class CartTests extends BaseTest {

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
	// COMMON FLOW: Add Product to Cart
	// ======================================================
	private void shouldAddProductToCartSuccessfully(ProductData data) {

		SearchPage search = new SearchPage();
		search.searchProduct(data.getProductName(), data.getProductCategory(), true);
		search.clickOnProduct();

		ProductDetailsPage product = new ProductDetailsPage();
		product.selectDeliveryDate(data.getDeliveryMonth(), data.getDeliveryYear(), data.getDeliveryDay());
		product.addProductToCart();
	}

	// ======================================================
	// SCENARIO 1: Verify Product Is Added to Cart
	// ======================================================
	@Test(groups = { "Smoke", "Regression", "Cart", "CoreFlow" })
	public void shouldDisplayProductInCartAfterAdding() {

		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		loginToApplication();
		shouldAddProductToCartSuccessfully(data);

		CartPage cart = new CartPage();
		cart.clickOnCartHeader();

		Assert.assertTrue(cart.isProductPresentInCart(data.getProductName()), "Product is not present in cart.");
	}

	// ======================================================
	// SCENARIO 2: Verify Product Quantity in Cart
	// ======================================================
	@Test(groups = { "Regression", "Cart" })
	public void shouldUpdateProductQuantityCorrectlyInCart() {

		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		loginToApplication();
		shouldAddProductToCartSuccessfully(data);

		CartPage cart = new CartPage();
		cart.clickOnCartHeader();

		Assert.assertEquals(cart.getProductQuantity(data.getProductName()), "1", "Product quantity mismatch in cart.");
	}

	// ======================================================
	// SCENARIO 3: Verify Product Total Price in Cart
	// ======================================================
	@Test(groups = { "Regression", "Cart" })
	public void shouldCalculateCorrectTotalPriceInCart() {

		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		loginToApplication();
		shouldAddProductToCartSuccessfully(data);

		CartPage cart = new CartPage();
		cart.clickOnCartHeader();

		Assert.assertNotNull(cart.getProductTotalPrice(data.getProductName()),
				"Product total price not displayed in cart.");
	}

	// ======================================================
	// SCENARIO 4: Verify Cart Is Not Empty After Adding Product
	// ======================================================
	@Test(groups = { "Regression", "Cart" })
	public void shouldNotAllowCartToBeEmptyAfterAddingProduct() {

		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		loginToApplication();
		shouldAddProductToCartSuccessfully(data);

		CartPage cart = new CartPage();
		cart.clickOnCartHeader();

		Assert.assertTrue(cart.getCartTable().getRowCount() > 0, "Cart should not be empty after adding product.");
	}

	// ======================================================
	// SCENARIO 5: Proceed to Checkout from Cart
	// ======================================================
	@Test(groups = { "Regression", "Cart", "Checkout", "CoreFlow" })
	public void shouldProceedToCheckoutFromCart() {

		ProductData data = JsonDataReader.loadJson(JSON_PATH, ProductData.class);

		loginToApplication();
		shouldAddProductToCartSuccessfully(data);

		CartPage cart = new CartPage();
		cart.clickOnCartHeader();
		cart.clickOnCheckout();

		CheckoutPage checkout = new CheckoutPage();

		Assert.assertTrue(checkout.isCheckoutPageDisplayed(), "User was not navigated to Checkout page.");
	}
}
