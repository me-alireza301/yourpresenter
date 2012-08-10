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
import com.google.code.yourpresenter.selenium.restclient.ProjectorDataJSONRestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("./../test-application-config.xml")
public class StateDTONonExistingScheduleIT {

	@Autowired
	ProjectorDataJSONRestTemplate jSONRestTemplate;

	@Test
	public void testNonExistingSchedule() throws IOException, YpException {
		StateDTO stateDTO = jSONRestTemplate.getState("non_existing_schedule");
		Assert.assertNotNull(stateDTO);
		Assert.assertNull(stateDTO.getActualSlide());
		Assert.assertNull(stateDTO.getSchedule());
	}
}
