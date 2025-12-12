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
    public void searchProduct() {

        // Load product data from JSON
        ProductData searchData = JsonDataReader.loadJson(jsonPath, ProductData.class);

        // Navigate and login
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.setUserEmail(prop.getProperty("email"));
        loginPage.setUserPassword(prop.getProperty("password"));
        loginPage.clickOnLogin();

        // Perform product search
        SearchPage searchPage = new SearchPage(DriverManager.getDriver());
        searchPage.enterProductToSearch(searchData.getProductName());
        searchPage.clickOnSearch();
        searchPage.selectProductCategory(searchData.getProductCategory());
        searchPage.clickOnProductDescriptionCheckbox();
        searchPage.clickOnInnerSearch();

        // Validate product name
        String actualName = searchPage.confirmProdName();
        Assert.assertEquals(actualName, searchData.getProductName(), "Product name mismatch.");
    }
}
