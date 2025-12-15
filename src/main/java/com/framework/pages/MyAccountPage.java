package com.framework.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * MyAccountPage
 * -------------
 * Page Object representing My Account page.
 * Uses centralized actions from BasePage.
 */
public class MyAccountPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(MyAccountPage.class);

    // ❌ No WebDriver constructor
    // ❌ No waits / JS / ExpectedConditions
    // ✅ BasePage constructor is used automatically

    // -------------------- Web Elements --------------------

    @FindBy(xpath = "//h2[normalize-space()='My Account']")
    private WebElement myAccountMsg;

    @FindBy(xpath = "//div[@class='list-group']//a[text()='Logout']")
    private WebElement logoutBtn;

    // -------------------- Action / Validation Methods --------------------

    /**
     * Verifies that the "My Account" heading is displayed after login.
     */
    public boolean isMyAccountMsgDisplayed() {
        logger.info("Validating the presence of 'My Account' message.");
        return isDisplayed(myAccountMsg);
    }

    /**
     * Clicks on the Logout button.
     */
    public void clickOnLogout() {
        logger.info("Clicking Logout button.");
        click(logoutBtn);
    }
}
