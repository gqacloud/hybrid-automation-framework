package com.framework.tests.purchase;

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

public class PurchaseTests extends BaseTest {

    // ======================================================
    // SCENARIO: End-to-End Purchase Flow
    // ======================================================
    @Test(groups = { "Regression", "Master", "E2E" })
    public void purchaseProductEndToEndScenario() {

        // -------------------- Login --------------------
        HomePage homePage = new HomePage();
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        LoginPage loginPage = new LoginPage();
        loginPage.login(
                prop.getProperty("email"),
                prop.getProperty("password")
        );

        // -------------------- Test Data --------------------
        ProductData data =
                JsonDataReader.loadJson(JSON_PATH, ProductData.class);

        // -------------------- Search Product --------------------
        SearchPage searchPage = new SearchPage();
        searchPage.searchProduct(
                data.getProductName(),
                data.getProductCategory(),
                true
        );
        searchPage.clickOnProduct();

        // -------------------- Product Details Validation --------------------
        ProductDetailsPage productPage = new ProductDetailsPage();

        Assert.assertEquals(productPage.getProductName(), data.getProductName());
        Assert.assertEquals(productPage.getBrand(), data.getBrand());
        Assert.assertEquals(productPage.getProductCode(), data.getProductCode());
        Assert.assertEquals(productPage.getRewardPoints(), data.getRewardPoints());
        Assert.assertEquals(productPage.getAvailability(), data.getAvailability());
        Assert.assertEquals(productPage.getDisplayedPrice(), data.getPrice());

        // -------------------- Add to Cart --------------------
        productPage.selectDeliveryDate(
                data.getDeliveryMonth(),
                data.getDeliveryYear(),
                data.getDeliveryDay()
        );
        productPage.addProductToCart();

        // -------------------- Cart Actions --------------------
        CartPage cartPage = new CartPage();
        cartPage.clickOnCartHeader();

        Assert.assertTrue(
                cartPage.isProductPresentInCart(data.getProductName()),
                "Product not found in cart after adding."
        );

        cartPage.getShippingTaxEstimation(
                data.getCountry(),
                data.getRegion(),
                data.getZipcode()
        );

        cartPage.clickOnCheckout();

        // -------------------- Checkout Validation --------------------
        CheckoutPage checkoutPage = new CheckoutPage();

        Assert.assertTrue(
                checkoutPage.isCheckoutPageDisplayed(),
                "Checkout page was not displayed."
        );
    }
}
