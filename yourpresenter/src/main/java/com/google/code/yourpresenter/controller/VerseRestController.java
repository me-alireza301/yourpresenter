package com.google.code.yourpresenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.yourpresenter.service.IVerseService;

@Controller
public class VerseRestController {
	@Autowired
	private IVerseService verseService;
	
	@RequestMapping(value="/verse/deleteall", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Integer getState() {
		return verseService.deleteAll();
	}
}
