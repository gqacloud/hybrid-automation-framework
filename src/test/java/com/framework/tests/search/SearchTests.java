package com.framework.tests.search;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.framework.base.BaseTest;
import com.framework.model.ProductData;
import com.framework.pages.HomePage;
import com.framework.pages.LoginPage;
import com.framework.pages.SearchPage;
import com.framework.utils.data.JsonDataReader;

public class SearchTests extends BaseTest {

    // ======================================================
    // COMMON PRE-CONDITION: Login
    // ======================================================
    private void loginToApplication() {

        HomePage homePage = new HomePage();
        homePage.clickOnMyAccount();
        homePage.clickOnLogin();

        LoginPage loginPage = new LoginPage();
        loginPage.login(
                prop.getProperty("email"),
                prop.getProperty("password")
        );
    }

    // ======================================================
    // SCENARIO 1: Search Product with Valid Data (POSITIVE)
    // ======================================================
    @Test(groups = { "Functional", "Regression", "Search" })
    public void searchProductWithValidDataScenario() {

        ProductData searchData =
                JsonDataReader.loadJson(JSON_PATH, ProductData.class);

        loginToApplication();

        SearchPage searchPage = new SearchPage();
        searchPage.searchProduct(
                searchData.getProductName(),
                searchData.getProductCategory(),
                true
        );

        Assert.assertEquals(
                searchPage.confirmProdName(),
                searchData.getProductName(),
                "Product name mismatch in search results."
        );
    }

    // ======================================================
    // SCENARIO 2: Search with Invalid Product (NEGATIVE)
    // ======================================================
    @Test(groups = { "Regression", "Search" })
    public void searchWithInvalidProductScenario() {

        ProductData searchData =
                JsonDataReader.loadJson(JSON_PATH, ProductData.class);

        loginToApplication();

        SearchPage searchPage = new SearchPage();
        searchPage.searchProduct(
                "InvalidProduct_12345",
                searchData.getProductCategory(),
                true
        );

        Assert.assertTrue(
                searchPage.isNoProductFoundDisplayed(),
                "No product found message should be displayed for invalid search."
        );
    }

    // ======================================================
    // SCENARIO 3: Search with Empty Product Name (NEGATIVE)
    // ======================================================
    @Test(groups = { "Regression", "Search" })
    public void searchWithEmptyProductScenario() {

        loginToApplication();

        SearchPage searchPage = new SearchPage();
        searchPage.searchProduct(
                "",
                "",
                false
        );

        Assert.assertTrue(
                searchPage.isNoProductFoundDisplayed(),
                "No product found message should be displayed for empty search."
        );
    }

    // ======================================================
    // SCENARIO 4: Search with Category Only (EDGE CASE)
    // ======================================================
    @Test(groups = { "Regression", "Search" })
    public void searchWithCategoryOnlyScenario() {

        ProductData searchData =
                JsonDataReader.loadJson(JSON_PATH, ProductData.class);

        loginToApplication();

        SearchPage searchPage = new SearchPage();
        searchPage.searchProduct(
                "",
                searchData.getProductCategory(),
                false
        );

        Assert.assertTrue(
                searchPage.isSearchResultDisplayed(),
                "Search results should be displayed when category filter is used."
        );
    }
}
