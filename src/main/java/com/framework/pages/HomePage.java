package com.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

	public HomePage(WebDriver driver) {
		super(driver);
	}

	// MyAccount link to go the My Account Page
	@FindBy(xpath = "//ul[@class='list-inline']//li[@class='dropdown']")
	WebElement MyAccountLnk;

	// Register link for registration of the new user
	@FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[normalize-space()='Register']")
	WebElement registerlnk;

	@FindBy(xpath = "//ul[@class='dropdown-menu dropdown-menu-right']//a[text()='Login']")
	WebElement LogInLnk;

	// Click on the MyAccount link to open
	public void clickOnMyAccount() {

		wait.until(ExpectedConditions.elementToBeClickable(MyAccountLnk)).click();
	}

	// click on the register link to open form
	public void clickOnRegister() {
		wait.until(ExpectedConditions.elementToBeClickable(registerlnk)).click();
	}

	public void clickOnLogin() throws InterruptedException {
		
		wait.until(ExpectedConditions.elementToBeClickable(LogInLnk)).click();
	}

}
