package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.google.code.yourpresenter.selenium.ITConstant;

public class MainPage {

	@FindBy(xpath = "//input[contains(@id, ':okButton')]")
	private WebElement okButton;
	
	@FindBy(xpath = "//span[@class='rf-msg-err']")
	private WebElement errorMsg;

	@FindBy(xpath = "//input[contains(@id, ':scheduleEnterInput')]")
	private WebElement scheduleEnterInput;
	
	@FindBy(xpath = "//input[@value='PRESENTER']")
	private WebElement presenterRoleInput;
	
	@FindBy(xpath = "//input[@value='ADMIN']")
	private WebElement adminRoleInput;
	
	public MainPage(WebDriver driver) {
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	public void choosePresenterRole() {
		presenterRoleInput.click();
	}
	
	public void chooseAdminRole() {
		adminRoleInput.click();
	}
	
	public void clickOkButton() {
		okButton.click();
	}
	
	public String getErrorMsgText() {
		return errorMsg.getText();
	}

	public void setScheduleName(String scheduleName) {
		scheduleEnterInput.clear();
		scheduleEnterInput.sendKeys(scheduleName);
	}
}
