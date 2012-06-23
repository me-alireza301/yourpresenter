package com.google.code.yourpresenter.selenium.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.code.yourpresenter.selenium.ITConstant;

public abstract class AbstractActionDialog {

	private WebDriver driver;
	
	public AbstractActionDialog(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	protected abstract WebElement getActionButton(); 
	
	public void waitDialogNotDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !getActionButton().isDisplayed();
					}
				});
	}

	public void waitDialogDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT, ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return getActionButton().isDisplayed();
					}
				});
	}

	public void clickActionButton() {
		getActionButton().click();
	}
}
