package com.framework.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseClass;
import com.framework.model.ProductData;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.ProductDetailsPage;
import com.framework.pages.SearchPage;
import com.framework.utils.JsonDataReader;

public class TC005_ProductDetailsTest extends BaseClass {

    @Test(groups = "Functional")
    public void verifyProductDetails() {

        // Load test data
        ProductData data = JsonDataReader.loadJson(jsonPath, ProductData.class);

        // Navigate & Login
        HomePage home = new HomePage();
        home.clickOnMyAccount();
        home.clickOnLogin();

        LoginPage login = new LoginPage();
        login.setUserEmail(prop.getProperty("email"));
        login.setUserPassword(prop.getProperty("password"));
        login.clickOnLogin();

        // Search product
        SearchPage search = new SearchPage();
        search.enterProductToSearch(data.getProductName());
        search.clickOnSearch();
        search.selectProductCategory(data.getProductCategory());
        search.clickOnProductDescriptionCheckbox();
        search.clickOnInnerSearch();
        search.clickOnProduct();

        // Validate product details
        ProductDetailsPage product = new ProductDetailsPage();

        Assert.assertEquals(product.getProductName(), data.getProductName(), "Product name mismatch");
        Assert.assertEquals(product.getBrand(), data.getBrand(), "Brand mismatch");
        Assert.assertEquals(product.getProductCode(), data.getProductCode(), "Product code mismatch");
        Assert.assertEquals(product.getRewardPoints(), data.getRewardPoints(), "Reward points mismatch");
        Assert.assertEquals(product.getAvailability(), data.getAvailability(), "Availability mismatch");
        Assert.assertEquals(product.getDisplayedPrice(), data.getPrice(), "Price mismatch");

        // Select delivery date
        product.selectDeliveryDate(data.getDeliveryMonth(), data.getDeliveryYear(), data.getDeliveryDay());

        // Add to cart
        product.addProductToCart();
    }
}
