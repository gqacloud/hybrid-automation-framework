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

public class TC00_PurchaseProductTest extends BaseTest {

    @Test(groups = { "Regression", "Master" })
    public void searchProductAddToCartAndCheckout() throws Exception {

        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        SearchPage searchPage = new SearchPage();
        ProductDetailsPage productPage = new ProductDetailsPage();
        CartPage cartPage = new CartPage();

        // Login
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        loginPage.setUserEmail(prop.getProperty("email"));
        loginPage.setUserPassword(prop.getProperty("password"));
        loginPage.clickOnLogin();

        // Load product data
        ProductData searchData =
                JsonDataReader.loadJson(JSON_PATH, ProductData.class);

        // Search product
        searchPage.enterProductToSearch(searchData.getProductName());
        searchPage.clickOnSearch();
        searchPage.selectProductCategory(searchData.getProductCategory());
        searchPage.clickOnProductDescriptionCheckbox();
        searchPage.clickOnInnerSearch();
        searchPage.clickOnProduct();

        // Validate product details
        Assert.assertEquals(productPage.getProductName(), searchData.getProductName());
        Assert.assertEquals(productPage.getBrand(), searchData.getBrand());
        Assert.assertEquals(productPage.getProductCode(), searchData.getProductCode());
        Assert.assertEquals(productPage.getRewardPoints(), searchData.getRewardPoints());
        Assert.assertEquals(productPage.getAvailability(), searchData.getAvailability());
        Assert.assertEquals(productPage.getDisplayedPrice(), searchData.getPrice());

        // Select delivery date & add to cart
        productPage.selectDeliveryDate(
                searchData.getDeliveryMonth(),
                searchData.getDeliveryYear(),
                searchData.getDeliveryDay()
        );
        productPage.addProductToCart();

        // Cart actions
        cartPage.clickOnCartHeader();
        cartPage.getShippingTaxEstimation(
                searchData.getCountry(),
                searchData.getRegion(),
                searchData.getZipcode()
        );
    }
}
