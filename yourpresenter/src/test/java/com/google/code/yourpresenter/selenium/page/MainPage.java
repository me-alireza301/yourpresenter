package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.code.yourpresenter.selenium.ITConstant;

public class MainPage {

	private WebDriver driver;
	
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
		this.driver = driver;
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
	
	public void waitOkButtonReady() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !okButton.isEnabled();
					}
				});
	}
}
