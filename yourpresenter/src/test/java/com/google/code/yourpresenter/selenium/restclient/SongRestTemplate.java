package com.google.code.yourpresenter.selenium.restclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongRestTemplate {

	@Autowired
	RestTemplate restTemplate;
	
	public int deleteAll() {
		final String url = "http://localhost:8081/yourpresenter/mvc/song/deleteall";
		return restTemplate.getForObject(url, Integer.class);
	}

}
