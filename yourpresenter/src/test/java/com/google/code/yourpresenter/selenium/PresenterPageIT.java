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
import com.google.code.yourpresenter.media.PptJodConvImporterTest;
import com.google.code.yourpresenter.selenium.page.AddChangeSongDialog;
import com.google.code.yourpresenter.selenium.page.DeleteSongDialog;
import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.page.PresenterPage;
import com.google.code.yourpresenter.selenium.page.UploadDialog;
import com.google.code.yourpresenter.selenium.restclient.PresentationRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.ScheduleRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SlideRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.SongRestTemplate;
import com.google.code.yourpresenter.selenium.restclient.VerseRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class PresenterPageIT {

	private static WebDriver driver;
	private static PresenterPage presenterPage;
	private static MainPage mainPage;
	private static AddChangeSongDialog addChangeSongDialog;
	private DeleteSongDialog deleteSongDialog;
	private UploadDialog uploadDialog;

	public static final String SCHEDULE_NAME = "New schedule";
	public static final String MAIN_URL = "http://localhost:8081/yourpresenter/";
	public static final String PRESENTER_URL = MAIN_URL
			+ "presenter/presenter.jsf";
	public static final String ADMIN_URL = MAIN_URL + "admin/admin.jsf";

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
		// DesiredCapabilities caps = DesiredCapabilities.firefox();
		// LoggingPreferences logs = new LoggingPreferences();
		// logs.enable(LogType.DRIVER, Level.ALL);
		// caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
		// driver = new FirefoxDriver(caps);

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
		deleteSongDialog = new DeleteSongDialog(driver);
		uploadDialog = new UploadDialog(driver);
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

	// @Test
	public void testAddChangeSongDialogCancel() {
		createSchedule(getScheduleName());

		Assert.assertFalse(addChangeSongDialog.isDisplayed());
		presenterPage.openAddSongDialog();
		addChangeSongDialog.clickCancelButton();
	}

	// @Test
	public void testAddSongEmptyName() {
		createSchedule(getScheduleName());
		presenterPage.openAddSongDialog();
		addChangeSongDialog.clickOkButton();

		// check error message displayed
		Assert.assertEquals(
				"dialogSongEditForm:dialogSongEditName: Validation Error: Value is required.",
				addChangeSongDialog.getErrorsSumText());
	}

//	@Test
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
		// TODO - currently workaround applied in application itself
		// driver.get(PRESENTER_URL);
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

	// @Test
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

	// @Test
	public void testBackground() throws IOException, YpException {
		testAddSongsToSchedule();

		//
		// check bg set for all schedule
		//
		presenterPage.setBgToSchedule(0);
		presenterPage.waitAjaxDone();

		// check that all slides in schedule have the same bg
		for (int i = 0; i < 12; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(0),
					presenterPage.getSlideBg(i));
		}

		//
		// check bg set for 1 presentation
		//
		presenterPage.setBgToPresentation(1, 0);
		presenterPage.waitAjaxDone();

		// check that all slides in schedule have the same bg
		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(1),
					presenterPage.getSlideBg(i));
		}

		// check that the rest is not affected
		for (int i = 4; i < 12; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(0),
					presenterPage.getSlideBg(i));
		}

		//
		// check bg set for 1 slide
		//
		presenterPage.setBgToSlide(2, 0);
		presenterPage.waitAjaxDone();

		// check that particular slide has the right bg
		for (int i = 0; i < 1; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(2),
					presenterPage.getSlideBg(i));
		}

		// check that the rest is not affected
		for (int i = 1; i < 4; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(1),
					presenterPage.getSlideBg(i));
		}

		for (int i = 4; i < 12; i++) {
			Assert.assertEquals(presenterPage.getBgImgUrl(0),
					presenterPage.getSlideBg(i));
		}
	}

	// private void setUpBg() {
	// openAdminPage();
	//
	// adminPage.setMediaDirs("");
	// }
	//
	// private void openAdminPage() {
	// driver.get(MAIN_URL);
	//
	// mainPage.chooseAdminRole();
	// mainPage.clickOkButton();
	//
	// Assert.assertEquals(ADMIN_URL, driver.getCurrentUrl());
	// }

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
		driver.get(MAIN_URL);

		mainPage.choosePresenterRole();
		mainPage.setScheduleName(scheduleName);
		mainPage.clickOkButton();

		// retry
		Sleeper.sleepTightInSeconds(1);
		if (!driver.getCurrentUrl().equals(PRESENTER_URL)) {
			mainPage.clickOkButton();
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

//	@Test
	public void testUpdateDeleteSong() throws IOException, YpException {
		// add schedule
		createSchedule(getScheduleName());

		// add song to schedule
		Song song = createSong("song3.txt", "UTF-8");
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(0));

		// change song name
		song.setName("song3 updated");
		presenterPage.clickEditSong(0);
		addChangeSongDialog.waitDialogDisplayed();
		addChangeSongDialog.setSongName(song.getName());
		addChangeSongDialog.clickOkButton();
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(0));

		// delete song
		presenterPage.clickDeleteSong(0);
		deleteSongDialog.waitDialogDisplayed();
		deleteSongDialog.clickDeleteButton();
		deleteSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();

		// don't wait too long for non-existing element
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Assert.assertEquals(0, presenterPage.getSongCount());
		driver.manage().timeouts()
				.implicitlyWait(ITConstant.DRIVER_WAIT, TimeUnit.SECONDS);

	}

	@Test
	public void testImportMediaMisc() throws IOException, YpException {
		// add schedule
		createSchedule(getScheduleName());
		
		// test import
		presenterPage.openMiscUploadDialog();
		File[] files = new File[] { new File(
				PptJodConvImporterTest.RESOURCE_PATH
						+ "/appa.pdf") };
		uploadDialog.addImportFiles(files);
		uploadDialog.waitUploadDisplayed();
		uploadDialog.clickUploadButton();
		uploadDialog.waitImportEnabled();
		uploadDialog.clickImportButton();
		presenterPage.waitAjaxDone();
		
		// TODO workaround no rerendering on import complete yet
		driver.get(PRESENTER_URL);
		
		//
		// check bg set for all schedule
		//
		presenterPage.addMediaMiscToScheduleBeginning(0);
		presenterPage.waitAjaxDone();

		// check that all slides in schedule have the same bg
//		for (int i = 0; i < 12; i++) {
//			Assert.assertEquals(presenterPage.getBgImgUrl(0),
//					presenterPage.getSlideBg(i));
//		}

		//
		// check bg set for 1 presentation
		//
		presenterPage.addMediaMiscToSchedule(0, 0);
		presenterPage.waitAjaxDone();
	}
	
}
