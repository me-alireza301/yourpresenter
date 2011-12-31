package com.google.code.yourpresenter.selenium;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.Sleeper;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.selenium.page.AddChangeSongDialog;
import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.page.PresenterPage;
import com.google.code.yourpresenter.selenium.restclient.PresentationRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.ScheduleRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SlideRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SongRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.VerseRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("test-application-config.xml")
public class PresenterPageIT {

	private static WebDriver driver;
	private static PresenterPage presenterPage;
	private static MainPage mainPage;
	private static AddChangeSongDialog addChangeSongDialog;

	public static final String SCHEDULE_NAME = "New schedule";
	public static final String PRESENTER_URL = "http://localhost:8081/yourpresenter/presenter/presenter.jsf";

	public static final String RESOURCE_PATH = "target/test-classes/com/google/code/yourpresenter/selenium/";

	@Autowired
	private ScheduleRestTemplate scheduleRestTemplate;
	@Autowired
	private SlideRestTemplate slideRestTemplate;
	@Autowired
	private PresentationRestTemplate presenationRestTemplate;
	@Autowired
	private VerseRestTemplate verseRestTemplate;
	@Autowired
	private SongRestTemplate songRestTemplate;

	@BeforeClass
	public static void setUpAll() throws Exception {
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
	public void setUp() throws Exception {
		mainPage = new MainPage(driver);
		presenterPage = new PresenterPage(driver);
		addChangeSongDialog = new AddChangeSongDialog(driver);
	}

	@AfterClass
	public static void cleanUpAll() {
		driver.quit();
	}

	@After
	public void cleanUp() {
		presenterPage.waitAjaxDone();
		slideRestTemplate.deleteAll();
		verseRestTemplate.deleteAll();
		presenationRestTemplate.deleteAll();
		songRestTemplate.deleteAll();
		scheduleRestTemplate.deleteAll();
	}

	@Test
	public void testAddChangeSongDialogCancel() {
		createSchedule(getScheduleName());

		Assert.assertFalse(addChangeSongDialog.isDisplayed());
		presenterPage.openAddSongDialog();
		addChangeSongDialog.clickCancelButton();
	}

	@Test
	public void testAddSongEmptyName() {
		createSchedule(getScheduleName());
		presenterPage.openAddSongDialog();
		addChangeSongDialog.clickOkButton();

		// check error message displayed
		Assert.assertEquals(
				"dialogSongEditForm:dialogSongEditName: Validation Error: Value is required.",
				addChangeSongDialog.getErrorsSumText());
	}

	@Test
	public void testAddSongTooLongTxt() throws IOException {
		createSchedule(getScheduleName());
		createSong("song_too_long_txt.txt", "cp1250");

		// check error message displayed
		Assert.assertEquals("size must be between 0 and 1000",
				addChangeSongDialog.getErrorsSumText());
	}

	// @Test
	public void testAddSongs() throws IOException, YpException {
		createSchedule(getScheduleName());
		Song song = createSong("song1_cp1250.txt", "cp1250");
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(0));

		driver.get(PRESENTER_URL);
		song = createSong("song2_cp1250.txt", "cp1250");
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(1));
	}

	// @Test
	public void testAddSongsToSchedule() throws IOException, YpException {
		testAddSongs();

		// workaround - only till drag&drop can be done without full refresh
		driver.get(PRESENTER_URL);
		presenterPage.addSongToScheduleBeginning(0);
		presenterPage.waitAjaxDone();
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(0));

		presenterPage.addSongToSchedule(1, 0);
		presenterPage.waitAjaxDone();
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(0));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(1));

		presenterPage.addSongToSchedule(1, 1);
		presenterPage.waitAjaxDone();
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(0));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(1));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(2));
	}

	@Test
	public void testPresentationReorder() throws IOException, YpException {
		testAddSongsToSchedule();

		presenterPage.movePresentationToBeginning(1);
		presenterPage.waitAjaxDone();
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(0));
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(1));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(2));

		presenterPage.movePresentation(0, 1);
		presenterPage.waitAjaxDone();
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(0));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(1));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(2));
	}

	private Song createSong(String fileName, String fileEncoding)
			throws IOException {
		presenterPage.openAddSongDialog();
		Song song = new SongPlainTxtUnmarshaller().unmarshall(new File(
				RESOURCE_PATH + fileName), fileEncoding);
		addChangeSongDialog.waitDialogDisplayed();
		addChangeSongDialog.setSongName(song.getName());
		addChangeSongDialog.setSongText(song.getText());
		addChangeSongDialog.clickOkButton();
		return song;
	}

	private void createSchedule(String scheduleName) {
		driver.get("http://localhost:8081/yourpresenter/main.jsf");

		mainPage.setScheduleName(scheduleName);
		mainPage.clickPresenterButton();

		// retry
		if (!("Schedule: " + scheduleName).equals(presenterPage
				.getScheduleNameText())) {
			Sleeper.sleepTightInSeconds(1);
			mainPage.clickPresenterButton();
		}

		// check schedule created
		Assert.assertEquals("Schedule: " + scheduleName,
				presenterPage.getScheduleNameText());
		// check redirect
		Assert.assertEquals(PRESENTER_URL, driver.getCurrentUrl());
	}

	protected String getScheduleName() {
		return SCHEDULE_NAME;
	}

	// private String getRandScheduleName(String scheduleName) {
	// String time = Long.toString(System.currentTimeMillis());
	// String randInt = Integer.toString(new Random().nextInt(1000));
	// scheduleName = new
	// StringBuilder(scheduleName).append(time).append(randInt).toString();
	// return scheduleName;
	// }
}
