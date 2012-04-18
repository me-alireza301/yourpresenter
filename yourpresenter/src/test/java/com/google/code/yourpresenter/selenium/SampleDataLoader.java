package com.google.code.yourpresenter.selenium;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.Sleeper;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.selenium.page.AddChangeSongDialog;
import com.google.code.yourpresenter.selenium.page.MainPage;
import com.google.code.yourpresenter.selenium.page.PresenterPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class SampleDataLoader {

	private static WebDriver driver;
	private static PresenterPage presenterPage;
	private static MainPage mainPage;
	private static AddChangeSongDialog addChangeSongDialog;

	@BeforeClass
	public static void setUpAll() throws Exception {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(ITConstant.DRIVER_WAIT, TimeUnit.SECONDS);
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

	@Test
	public void load() throws InitializationError, IOException, YpException {
		createSchedule(new PresenterPageIT()
				.getScheduleName());
		Song song = createSong("song1_cp1250.txt", "cp1250");
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(1));

		driver.get(PresenterPageIT.PRESENTER_URL);
		song = createSong("song2_cp1250.txt", "cp1250");
		addChangeSongDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		Assert.assertEquals(song.getName(), presenterPage.getSongName(2));
	}
	
	private void createSchedule(String scheduleName) {
		driver.get("http://localhost:8081/yourpresenter/main.jsf");
		
		mainPage.choosePresenterRole();
		mainPage.setScheduleName(scheduleName);
		mainPage.clickOkButton();

		// retry
		Sleeper.sleepTightInSeconds(1);
		if(!driver.getCurrentUrl().equals(PresenterPageIT.PRESENTER_URL)) {
			mainPage.clickOkButton();
		}

		// check schedule created
		Assert.assertEquals("Schedule: " + scheduleName,
				presenterPage.getScheduleNameText());
		// check redirect
		Assert.assertEquals(PresenterPageIT.PRESENTER_URL, driver.getCurrentUrl());
	}

	private Song createSong(String fileName, String fileEncoding) throws IOException {
		presenterPage.openAddSongDialog();
		Song song = new SongPlainTxtUnmarshaller().unmarshall(new File(PresenterPageIT.RESOURCE_PATH + fileName), fileEncoding);
		addChangeSongDialog.waitDialogDisplayed();
		addChangeSongDialog.setSongName(song.getName());
		addChangeSongDialog.setSongText(song.getText());
		addChangeSongDialog.clickOkButton();
		return song;
	}

}
