package com.google.code.yourpresenter.selenium;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.Sleeper;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.selenium.page.AddChangeSongDialog;
import com.google.code.yourpresenter.selenium.page.DeletePresentationDialog;
import com.google.code.yourpresenter.selenium.page.DeleteScheduleDialog;
import com.google.code.yourpresenter.selenium.page.DeleteSongDialog;
import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.page.PresenterPage;
import com.google.code.yourpresenter.selenium.page.UploadDialog;
import com.google.code.yourpresenter.selenium.restclient.PresentationRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.ScheduleRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SlideRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SongRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.VerseRestTemplate;

public abstract class AbstractIT {

	// TODO for paralelization this should be refactored to some IT specific one
	public static final String SCHEDULE_NAME = "New schedule";

	protected static WebDriver driver;
	protected PresenterPage presenterPage;
	protected MainPage mainPage;
	protected AddChangeSongDialog addChangeSongDialog;
	protected DeleteSongDialog deleteSongDialog;
	protected UploadDialog uploadDialog;
	protected DeleteScheduleDialog deleteScheduleDialog;
	protected DeletePresentationDialog deletePresentationDialog;

	@Autowired
	protected ScheduleRestTemplate scheduleRestTemplate;
	@Autowired
	protected SlideRestTemplate slideRestTemplate;
	@Autowired
	protected PresentationRestTemplate presenationRestTemplate;
	@Autowired
	protected VerseRestTemplate verseRestTemplate;
	@Autowired
	protected SongRestTemplate songRestTemplate;

	@BeforeClass
	public static void beforeClass() throws Exception {
		// for drag and drop to be working on linux:
		// Enabling features that are disabled by default in Firefox
		// see: http://code.google.com/p/selenium/wiki/TipsAndTricks
		FirefoxProfile profile = new FirefoxProfile();
		profile.setEnableNativeEvents(true);
		driver = new FirefoxDriver(profile);
		driver.manage().timeouts()
				.implicitlyWait(ITConstant.DRIVER_WAIT, TimeUnit.SECONDS);
	}

	@Before
	public void before() throws Exception {
		// make sure that in case there is something left from the last failed
		// tests,
		// it'll be cleaned up
		cleanUp();

		mainPage = new MainPage(driver);
		presenterPage = new PresenterPage(driver);
		addChangeSongDialog = new AddChangeSongDialog(driver);
		deleteSongDialog = new DeleteSongDialog(driver);
		deleteScheduleDialog = new DeleteScheduleDialog(driver);
		deletePresentationDialog = new DeletePresentationDialog(driver);
		uploadDialog = new UploadDialog(driver);
	}

	@AfterClass
	public static void afterAll() {
		driver.quit();
	}

	@After
	public void after() {
		presenterPage.waitAjaxDone();
		cleanUp();
	}

	public void cleanUp() {
		slideRestTemplate.deleteAll();
		verseRestTemplate.deleteAll();
		presenationRestTemplate.deleteAll();
		songRestTemplate.deleteAll();
		scheduleRestTemplate.deleteAll();
	}

	protected Song createSong(String fileName, String fileEncoding)
			throws IOException {
		presenterPage.openAddSongDialog();
		Song song = loadSong(fileName, fileEncoding);
		addChangeSongDialog.waitDialogDisplayed();
		addChangeSongDialog.setSongName(song.getName());
		addChangeSongDialog.setSongText(song.getText());
		addChangeSongDialog.clickOkButton();
		return song;
	}

	protected Song loadSong(String fileName) throws IOException {
		return loadSong(fileName, null);
	}

	protected Song loadSong(String fileName, String fileEncoding)
			throws IOException {
		Song song = new SongPlainTxtUnmarshaller().unmarshall(new File(
				ITConstant.RESOURCE_PATH + fileName), fileEncoding);
		return song;
	}

	protected void createSchedule(String scheduleName) {
		driver.get(ITConstant.MAIN_URL);

		mainPage.choosePresenterRole();
		mainPage.setScheduleName(scheduleName);
		mainPage.clickOkButton();

		// retry
		Sleeper.sleepTightInSeconds(1);
		if (!driver.getCurrentUrl().equals(ITConstant.PRESENTER_URL)) {
			mainPage.clickOkButton();
		}

		// check schedule created
		Assert.assertEquals("Schedule: " + scheduleName,
				presenterPage.getScheduleNameText());
		// check redirect
		Assert.assertEquals(ITConstant.PRESENTER_URL, driver.getCurrentUrl());
	}

	public void createSongs() throws IOException, YpException {
		createSchedule(getScheduleName());
		Song song = createSong("song1.txt", null);
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(0));

		song = createSong("song2.txt", null);
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(1));
	}

	protected String getScheduleName() {
		return SCHEDULE_NAME;
	}
}
