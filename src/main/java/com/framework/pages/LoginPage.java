package com.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@FindBy(xpath = "//input[@id='input-email']")
	WebElement Emailtxtbox;

	@FindBy(xpath = "//input[@id='input-password']")
	WebElement Pwdtxtbox;

	@FindBy(xpath = "//div[@class='form-group']//a[normalize-space()='Forgotten Password']")
	WebElement forgotpwdlnk;

	@FindBy(xpath = "//input[@value='Login']")
	WebElement LoginBtn;

	@FindBy(xpath = "//div[@class='alert alert-danger alert-dismissible']")
	WebElement warnMsg;

	public void setUserEmail(String email) {
		wait.until(ExpectedConditions.visibilityOf(Emailtxtbox));
		Emailtxtbox.clear();
		Emailtxtbox.sendKeys(email);
	}

	public void setUserPassword(String pwd) {
		wait.until(ExpectedConditions.visibilityOf(Pwdtxtbox));
		Pwdtxtbox.clear();
		Pwdtxtbox.sendKeys(pwd);
	}

	public void clickOnLogin() {
		wait.until(ExpectedConditions.elementToBeClickable(LoginBtn)).click();
	}

	public String warnMessage() {
		try {
			wait.until(ExpectedConditions.visibilityOf(warnMsg));
			return warnMsg.getText();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}
