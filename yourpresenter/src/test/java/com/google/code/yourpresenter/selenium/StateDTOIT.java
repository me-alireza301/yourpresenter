package com.google.code.yourpresenter.selenium;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.selenium.restclient.ProjectorDataJSONRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class StateDTOIT extends AbstractIT {

	@Autowired
	ProjectorDataJSONRestTemplate jSONRestTemplate;

	@Test
	public void testToggleButtons() throws IOException, YpException {
		Song song = loadSong("song1.txt");

		// add song
		createSongs();
		presenterPage.addSongToScheduleBeginning(0);
		presenterPage.waitAjaxDone();

		checkNoActiveSlide(false, false, false);
		// check buttons
		Assert.assertFalse(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// click live button
		presenterPage.clickLiveButton();
		presenterPage.waitAjaxDone();

		checkNoActiveSlide(true, false, false);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// activate 1.st slide
		presenterPage.clickSlide(0);
		presenterPage.waitAjaxDone();

		checkNoBgImage(song, true, false, false);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// set background
		presenterPage.setBgToSchedule(0);
		presenterPage.waitAjaxDone();

		checkWithBgImage(song, true, false, false);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// click clear button
		presenterPage.clickClearButton();
		presenterPage.waitAjaxDone();

		checkWithBgImage(song, true, false, true);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertTrue(presenterPage.isClearClicked());

		// unclick clear button
		presenterPage.clickClearButton();
		presenterPage.waitAjaxDone();

		checkWithBgImage(song, true, false, false);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertFalse(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// click blank button
		presenterPage.clickBlankButton();
		presenterPage.waitAjaxDone();

		checkWithBgImage(song, true, true, false);
		// check buttons
		Assert.assertTrue(presenterPage.isLiveClicked());
		Assert.assertTrue(presenterPage.isBlankClicked());
		Assert.assertFalse(presenterPage.isClearClicked());

		// unclick blank button
		presenterPage.clickBlankButton();
		presenterPage.waitAjaxDone();

		checkWithBgImage(song, true, false, false);

	}

	private void checkStateButtons(StateDTO stateDTO, boolean live,
			boolean blank, boolean clear) {
		// check buttons
		Assert.assertEquals(presenterPage.isLiveClicked(), live);
		Assert.assertEquals(presenterPage.isBlankClicked(), blank);
		Assert.assertEquals(presenterPage.isClearClicked(), clear);

		// check state
		Assert.assertEquals(stateDTO.getSchedule().isBlank(), blank);
		Assert.assertEquals(stateDTO.getSchedule().isLive(), live);
		Assert.assertEquals(stateDTO.getSchedule().isClear(), clear);
	}

	private void checkNoBgImage(Song song, boolean live, boolean blank,
			boolean clear) {
		// check state
		// OK let's assume we have one schedule only => 1
		StateDTO stateDTO = jSONRestTemplate.getState(1L);
		Assert.assertNotNull(stateDTO);
		Assert.assertNotNull(stateDTO.getActualSlide());
		Assert.assertEquals(stateDTO.getActualSlide().getText(), song.getVerses().get(0).getText());
		Assert.assertNull(stateDTO.getActualSlide().getBgImage());
		Assert.assertNotNull(stateDTO.getSchedule());
		Assert.assertEquals(getScheduleName(), stateDTO.getSchedule().getName());
		Assert.assertNull(stateDTO.getSchedule().getBgImage());
		Assert.assertNull(stateDTO.getSchedule().getPresentations());

		checkStateButtons(stateDTO, live, blank, clear);
	}

	private void checkNoActiveSlide(boolean live, boolean blank, boolean clear) {
		// check state
		// OK let's assume we have one schedule only => 1
		StateDTO stateDTO = jSONRestTemplate.getState(1L);
		Assert.assertNotNull(stateDTO);
		Assert.assertNull(stateDTO.getActualSlide());
		Assert.assertNotNull(stateDTO.getSchedule());
		Assert.assertEquals(getScheduleName(), stateDTO.getSchedule().getName());
		Assert.assertNull(stateDTO.getSchedule().getBgImage());
		Assert.assertNull(stateDTO.getSchedule().getPresentations());

		checkStateButtons(stateDTO, live, blank, clear);
	}

	private void checkWithBgImage(Song song, boolean live, boolean blank, boolean clear) {
		// check state
		// OK let's assume we have one schedule only => 1
		StateDTO stateDTO = jSONRestTemplate.getState(1L);
		Assert.assertNotNull(stateDTO);
		Assert.assertNotNull(stateDTO.getActualSlide());
		Assert.assertEquals(stateDTO.getActualSlide().getText(), song.getVerses().get(0).getText());
		Assert.assertNotNull(stateDTO.getActualSlide().getBgImage());
		Assert.assertEquals(stateDTO.getActualSlide().getBgImage().getId(),
				Long.valueOf(1));
		Assert.assertNotNull(stateDTO.getSchedule());
		Assert.assertEquals(getScheduleName(), stateDTO.getSchedule().getName());
		Assert.assertNull(stateDTO.getSchedule().getBgImage());
		Assert.assertNull(stateDTO.getSchedule().getPresentations());

		checkStateButtons(stateDTO, live, blank, clear);
	}

}
