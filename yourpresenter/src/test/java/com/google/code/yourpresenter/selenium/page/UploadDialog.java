package com.google.code.yourpresenter.selenium.page;

import java.io.File;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.code.yourpresenter.selenium.ITConstant;

public class UploadDialog {

	private WebDriver driver;

	@FindBy(id = "dialogUploadForm:buttonCancel")
	private WebElement cancelButton;

	@FindBy(id = "dialogUploadForm:buttonImport")
	private WebElement importButton;

	@FindBy(xpath = "//input[@type='file']")
	private WebElement uploadAddButton;

//	@FindBy(xpath = "//input[@type='submit' and @value='Upload']")
//	@FindBy(id = "dialogUploadForm:upload")
	@FindBy(css = "span.rf-fu-btn-cnt-upl")
	private WebElement uploadUploadButton;

	public UploadDialog(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	public void waitDialogNotDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !cancelButton.isDisplayed();
					}
				});
	}

	public void waitDialogDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return cancelButton.isDisplayed();
					}
				});
	}

	public void clickCancelButton() {
		cancelButton.click();
	}

	public void clickImportButton() {
		importButton.click();
	}

	public void waitImportDone() {
		// TODO
	}

	public void addImportFiles(File[] files) {
		for (File file : files) {
			Assert.assertTrue(file.exists());
			// WebElement browseButton = driver.findElement(By.id("myfile"));
			addUploadFile(file);
		}
	}

	public void clickUploadButton() {
		uploadUploadButton.click();
	}

	private void addUploadFile(File file) {
		// fileUpload selenium solution found on:
		// http://stackoverflow.com/questions/5610256/file-upload-using-selenium-webdriver-and-java/6130887#6130887
		uploadAddButton.sendKeys(file.getAbsolutePath());
	}

	public void waitImportEnabled() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return importButton.isEnabled();
					}
				});
	}

	public void waitUploadDisplayed() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return uploadUploadButton.isDisplayed();
					}
				});
	}
}
