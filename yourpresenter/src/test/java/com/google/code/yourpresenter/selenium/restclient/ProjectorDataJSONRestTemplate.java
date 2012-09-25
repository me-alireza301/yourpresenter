package com.google.code.yourpresenter.selenium.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.code.yourpresenter.dto.StateDTO;

@Service
public class ProjectorDataJSONRestTemplate {

	@Autowired
	RestTemplate restTemplate;

	public StateDTO getState(Long scheduleId) {
		final String url = "http://localhost:8081/yourpresenter/mvc/state/{scheduleId}";
		return restTemplate.getForObject(url, StateDTO.class, scheduleId);
	}
}
