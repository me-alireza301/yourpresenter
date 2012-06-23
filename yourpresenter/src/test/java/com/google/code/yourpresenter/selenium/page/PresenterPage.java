package com.google.code.yourpresenter.selenium.page;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.browserlaunchers.Sleeper;
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

	@FindBy(xpath = "//span[contains(@id, ':songTableSongName')]")
	private List<WebElement> songNames;

	@FindBy(xpath = "//div[contains(@id, ':songEdit')]")
	private List<WebElement> songEdits;
	
	@FindBy(xpath = "//div[contains(@id, ':songDelete')]")
	private List<WebElement> songDeletes;
	
	@FindBy(xpath = "//div[contains(@id, 'presentationDelete')]")
	private List<WebElement> presentationDeletes;
	
	@FindBy(xpath = "//div[contains(@id, ':panelHeaderPresentation_body')]")
	private List<WebElement> presentationNames;

	@FindBy(xpath = "//div[contains(@id, ':scheduleName_body')]")
	private WebElement scheduleName;

	@FindBy(xpath = "//span[@class = 'rf-st-start']")
	private WebElement ajaxStatus;

	@FindBy(xpath = "//div[contains(@class, 'media-image')]")
	private List<WebElement> bgImages;
	
	@FindBy(xpath = "//div[contains(@class, 'media-misc')]")
	private List<WebElement> mediaMiscs;

	@FindBy(xpath = "//div[contains(@class, 'slidebox')]")
	private List<WebElement> slides;

//	@FindBy(xpath = "//td[contains(@style, 'table-cell')]")
//	@FindBy(xpath = "//td[contains(@id, 'tabMisc:header:') AND @style = 'display: table-cell;']")
	@FindBy(xpath = "//td[contains(@id, 'tabMisc:header:inactive')]")
	private WebElement miscTabInactive;

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

	public void addSongToSchedule(int songIdx, int presentationIdx)
			throws YpException {
		validateSongIdx(songIdx);
		validatePresentationIdx(presentationIdx);
		dragAndDrop(songNames.get(songIdx),
				presentationNames.get(presentationIdx));
	}

	public void addSongToScheduleBeginning(int songIdx) throws YpException {
		validateSongIdx(songIdx);
		dragAndDrop(songNames.get(songIdx), scheduleName);
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
		validatePresentationIdx(presentationIdx);
		return presentationNames.get(presentationIdx).getText();
	}

	public Object getSongName(int songIdx) throws YpException {
		validateSongIdx(songIdx);
		return songNames.get(songIdx).getText();
	}

	public void movePresentationToBeginning(int dragIdx) throws YpException {
		validatePresentationIdx(dragIdx);
		dragAndDrop(presentationNames.get(dragIdx), scheduleName);
	}

	public void movePresentation(int dragIdx, int dropIdx) throws YpException {
		validatePresentationIdx(dragIdx);
		validatePresentationIdx(dropIdx);
		dragAndDrop(presentationNames.get(dragIdx),
				presentationNames.get(dropIdx));
	}

	private void dragAndDrop(WebElement drag, WebElement drop) {
		Action dragAndDrop = new Actions(driver).clickAndHold(drag)
				.moveToElement(drop).release(drop).build();
		dragAndDrop.perform();
	}

	public void setBgToSchedule(int bgIdx) throws YpException {
		validateBgIdx(bgIdx);
		dragAndDrop(bgImages.get(bgIdx), scheduleName);
	}

	public void setBgToPresentation(int bgIdx, int presentationIdx)
			throws YpException {
		validateBgIdx(bgIdx);
		validatePresentationIdx(presentationIdx);
		dragAndDrop(bgImages.get(bgIdx), presentationNames.get(presentationIdx));
	}

	public String getSlideBg(int slideIdx) throws YpException {
		// TODO more sophisticated computing could help, currently only slide
		// index
		// indexing all the slides on the page available

		validateSlideIdx(slideIdx);
		return slides.get(slideIdx).getCssValue("background-image");
	}

	public String getBgImgUrl(int bgIdx) throws YpException {
		validateBgIdx(bgIdx);
		return bgImages.get(bgIdx).getCssValue("background-image");
	}
	
	public String getMediaMiscUrl(int bgIdx) throws YpException {
		validateMediaMiscIdx(bgIdx);
		return mediaMiscs.get(bgIdx).getCssValue("background-image");
	}

	private void validateSlideIdx(int slideIdx) throws YpException {
		if (slides.size() <= slideIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
	}

	private void validateSongIdx(int songIdx) throws YpException {
		if (songNames.size() <= songIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
	}

	private void validatePresentationIdx(int presentationIdx)
			throws YpException {
		if (presentationNames.size() <= presentationIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
	}

	private void validateBgIdx(int bgIdx) throws YpException {
		if (bgImages.size() <= bgIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
	}

	public void setBgToSlide(int bgIdx, int slideIdx) throws YpException {
		validateBgIdx(bgIdx);
		validateSlideIdx(slideIdx);
		dragAndDrop(bgImages.get(bgIdx), slides.get(slideIdx));
	}

	public void clickEditSong(int songIdx) throws YpException {
		rightClickSong(songIdx);
		songEdits.get(songIdx).click();
	}

	private void rightClickSong(int songIdx) throws YpException {
		validateSongIdx(songIdx);
		// right click, see: http://code.google.com/p/selenium/issues/detail?id=161
		Actions builder = new Actions(driver);
		Action rClick = builder.contextClick(songNames.get(songIdx)).build();
		rClick.perform();

		Sleeper.sleepTightInSeconds(1);
	}

	public void clickDeleteSong(int songIdx) throws YpException {
		rightClickSong(songIdx);
		songDeletes.get(songIdx).click();
		
	}

	public int getSongCount() {
		return songNames.size();
	}

	public void openMiscUploadDialog() {
		// using menu is very error prone => hotkeys are better
		scheduleNameLabel.sendKeys(Keys.ALT, Keys.CONTROL, "u");
		this.waitAjaxDone();
	}

	public void addMediaMiscToSchedule(int mediaMiscIdx, int presentationIdx)
			throws YpException {
//		miscTabInactive.click();
		this.waitAjaxDone();
		
		validateMediaMiscIdx(mediaMiscIdx);
		validatePresentationIdx(presentationIdx);
		dragAndDrop(mediaMiscs.get(mediaMiscIdx),
				presentationNames.get(presentationIdx));
	}

	private void validateMediaMiscIdx(int mediaMiscIdx) throws YpException {
		if (mediaMiscs.size() <= mediaMiscIdx) {
			throw new YpException(YpError.NOT_SUPPORTED_IDX);
		}
		
	}

	public void addMediaMiscToScheduleBeginning(int mediaMiscIdx) throws YpException {
		miscTabInactive.click();
		this.waitAjaxDone();
		
		validateMediaMiscIdx(mediaMiscIdx);
		dragAndDrop(mediaMiscs.get(mediaMiscIdx), scheduleName);
	}

	public void clickDeletePresentation(int presentationIdx) throws YpException {
		rightClickPresentation(presentationIdx);
		presentationDeletes.get(presentationIdx).click();
	}
	
	private void rightClickPresentation(int presentationIdx) throws YpException {
		validatePresentationIdx(presentationIdx);
		// right click, see: http://code.google.com/p/selenium/issues/detail?id=161
		Actions builder = new Actions(driver);
		Action rClick = builder.contextClick(presentationNames.get(presentationIdx)).build();
		rClick.perform();

		Sleeper.sleepTightInSeconds(1);
	}

	public int getPresentationCount() {
		return presentationNames.size();
	}


}
