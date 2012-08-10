package com.google.code.yourpresenter.selenium;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.media.PptJodConvImporterTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class PresenterPageIT extends AbstractIT {

	@Test
	public void testAddChangeSongDialogCancel() {
		createSchedule(getScheduleName());

		Assert.assertFalse(addChangeSongDialog.isDisplayed());
		presenterPage.openAddSongDialog();
		presenterPage.waitAjaxDone();
		addChangeSongDialog.clickCancelButton();
	}

	@Test
	public void testAddSongEmptyName() {
		createSchedule(getScheduleName());
		presenterPage.openAddSongDialog();
		addChangeSongDialog.waitDialogDisplayed();
		addChangeSongDialog.clickOkButton();
		presenterPage.waitAjaxDone();
		
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

	@Test
	public void testAddSongsToSchedule() throws IOException, YpException {
		createSongs();

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

	@Test
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



	@Test
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
		deleteSongDialog.clickActionButton();
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
				PptJodConvImporterTest.RESOURCE_PATH + "/appa.pdf") };
		uploadDialog.addImportFiles(files);
		uploadDialog.waitUploadDisplayed();
		uploadDialog.clickUploadButton();
		uploadDialog.waitImportEnabled();
		uploadDialog.clickImportButton();
		presenterPage.waitAjaxDone();

		// TODO workaround no rerendering on import complete yet
		driver.get(ITConstant.PRESENTER_URL);

		//
		// add media misc to schedule
		//
		presenterPage.addMediaMiscToScheduleBeginning(0);
		presenterPage.waitAjaxDone();

		// check that all slides in schedule have the same bg
		Assert.assertEquals(presenterPage.getMediaMiscUrl(0),
				presenterPage.getSlideBg(0));

		//
		// add media misc to schedule
		//
		presenterPage.addMediaMiscToSchedule(0, 0);
		presenterPage.waitAjaxDone();

		Assert.assertEquals(presenterPage.getMediaMiscUrl(0),
				presenterPage.getSlideBg(0));

		Assert.assertEquals(presenterPage.getMediaMiscUrl(0),
				presenterPage.getSlideBg(6));
	}
	
	@Test
	public void testPresentationDelete() throws IOException, YpException {
		testAddSongsToSchedule();

		checkPresentationCount(3);
		
		// delete presentation 1
		presenterPage.clickDeletePresentation(1);
		deletePresentationDialog.waitDialogDisplayed();
		deletePresentationDialog.clickActionButton();
		deletePresentationDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		
		checkPresentationCount(2);
		Assert.assertEquals(presenterPage.getSongName(0),
				presenterPage.getPresentationName(0));
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(1));
		
		// delete presentation 0
		presenterPage.clickDeletePresentation(0);
		deletePresentationDialog.waitDialogDisplayed();
		deletePresentationDialog.clickActionButton();
		deletePresentationDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		
		checkPresentationCount(1);
		Assert.assertEquals(presenterPage.getSongName(1),
				presenterPage.getPresentationName(0));
		
		// delete presentation 0
		presenterPage.clickDeletePresentation(0);
		deletePresentationDialog.waitDialogDisplayed();
		deletePresentationDialog.clickActionButton();
		deletePresentationDialog.waitDialogNotDisplayed();
		presenterPage.waitAjaxDone();
		
		checkPresentationCount(0);
	}
	
	public void checkPresentationCount(int count) {
		// don't wait too long for non-existing element
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		Assert.assertEquals(count, presenterPage.getPresentationCount());
		driver.manage().timeouts()
				.implicitlyWait(ITConstant.DRIVER_WAIT, TimeUnit.SECONDS);
	}
	
	@Test
	public void testScheduleDelete() throws IOException, YpException {

	}

}
