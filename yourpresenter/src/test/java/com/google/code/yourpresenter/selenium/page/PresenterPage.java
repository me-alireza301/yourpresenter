package com.google.code.yourpresenter.selenium.page;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.selenium.ITConstant;

public class PresenterPage {

	private WebDriver driver;

	@FindBy(xpath = "//div[contains(@id, ':scheduleName')]")
	private WebElement scheduleNameLabel;

	@FindBy(xpath = "//td[contains(@id, ':songTable:')]")
	private List<WebElement> songNames;

	@FindBy(xpath = "//div[contains(@id, ':panelHeaderPresentation_body')]")
	private List<WebElement> presentationNames;

	@FindBy(xpath = "//div[contains(@id, ':scheduleName_body')]")
	private WebElement scheduleName;

	@FindBy(xpath = "//span[@class = 'rf-st-start']")
	private WebElement ajaxStatus;

	public PresenterPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver,
				ITConstant.DRIVER_WAIT);
		PageFactory.initElements(finder, this);
	}

	public Object getScheduleNameText() {
		return scheduleNameLabel.getText();
	}

	public void openAddSongDialog() {
		// using menu is very error prone => hotkeys are better
		scheduleNameLabel.sendKeys(Keys.ALT, Keys.CONTROL, "n");
		// Sleeper.sleepTightInSeconds(1);

		// int retry = 0;
		// do {
		// build and perform the mouseOver with Advanced User Interactions
		// API
		// new Actions(driver).moveToElement(fileMenuItem).build().perform();
		// new Actions(driver).moveToElement(fileSongMenuGroup).build()
		// .perform();
		// Sleeper.sleepTightInSeconds(1);
		// // then click when menu option is visible
		// addSongMenuItem.click();
		// } while (retry++ < 5 && !addChangeSongDialog.waitDialogDisplayed());
	}

	public void addSongToSchedule(int songIndex, int presentationIdx)
			throws YpException {
		if (songNames.size() <= songIndex) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		if (presentationNames.size() <= presentationIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		dragAndDrop(songNames.get(songIndex),
				presentationNames.get(presentationIdx));
	}

	public void addSongToScheduleBeginning(int songIndex) throws YpException {
		if (songNames.size() <= songIndex) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		dragAndDrop(songNames.get(songIndex), scheduleName);
	}

	public void waitAjaxDone() {
		(new WebDriverWait(driver, ITConstant.DRIVER_WAIT,
				ITConstant.DRIVER_CYCLIC_SLEEP))
				.until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return !ajaxStatus.isDisplayed();
					}
				});
	}

	public String getPresentationName(int presentationIdx) throws YpException {
		if (presentationNames.size() <= presentationIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		return presentationNames.get(presentationIdx).getText();
	}

	public Object getSongName(int songIdx) throws YpException {
		if (songNames.size() <= songIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		return songNames.get(songIdx).getText();
	}

	public void movePresentationToBeginning(int dragIdx) throws YpException {
		if (presentationNames.size() <= dragIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		dragAndDrop(presentationNames.get(dragIdx), scheduleName);
	}

	public void movePresentation(int dragIdx, int dropIdx) throws YpException {
		if (presentationNames.size() <= dragIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		if (presentationNames.size() <= dropIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		dragAndDrop(presentationNames.get(dragIdx),
				presentationNames.get(dropIdx));
	}

	private void dragAndDrop(WebElement drag, WebElement drop) {
		Action dragAndDrop = new Actions(driver).clickAndHold(drag)
				.moveToElement(drop).release(drop).build();
		dragAndDrop.perform();
	}
}
