package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.model.ProductData;
import com.framework.pages.CartPage;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.data.JsonDataReader;

public class TC006_CartPageTest extends BaseTest {

    @Test(groups = "Functional")
    public void verifyCartFunctionality() {

        // Load product data
        ProductData searchData = JsonDataReader.loadJson(jsonPath, ProductData.class);

        // Login
        HomePage homePage = new HomePage();
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        LoginPage loginPage = new LoginPage();
        loginPage.setUserEmail(prop.getProperty("email"));
        loginPage.setUserPassword(prop.getProperty("password"));
        loginPage.clickOnLogin();

        // Search product
        SearchPage searchPage = new SearchPage();
        searchPage.enterProductToSearch(searchData.getProductName());
        searchPage.clickOnSearch();
        searchPage.selectProductCategory(searchData.getProductCategory());
        searchPage.clickOnProductDescriptionCheckbox();
        searchPage.clickOnInnerSearch();
        searchPage.clickOnProduct();

        // Verify product details
        ProductDetailsPage product = new ProductDetailsPage();

        Assert.assertEquals(product.getProductName(), searchData.getProductName(), "Product name mismatch.");
        Assert.assertEquals(product.getBrand(), searchData.getBrand(), "Brand mismatch.");
        Assert.assertEquals(product.getProductCode(), searchData.getProductCode(), "Product code mismatch.");
        Assert.assertEquals(product.getRewardPoints(), searchData.getRewardPoints(), "Reward points mismatch.");
        Assert.assertEquals(product.getAvailability(), searchData.getAvailability(), "Availability mismatch.");
        Assert.assertEquals(product.getDisplayedPrice(), searchData.getPrice(), "Price mismatch.");

        // Select delivery date & add to cart
        product.selectDeliveryDate(searchData.getDeliveryMonth(), searchData.getDeliveryYear(), searchData.getDeliveryDay());
        product.addProductToCart();

        // Navigate to Cart
        CartPage cart = new CartPage();
        cart.clickOnCartHeader();

        // (Optional future assertions: product exists in cart, price matches, etc.)
    }
}
