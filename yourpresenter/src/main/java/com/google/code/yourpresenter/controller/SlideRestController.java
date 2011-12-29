package com.google.code.yourpresenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.yourpresenter.service.ISlideService;

@Controller
public class SlideRestController {

	@Autowired
	private ISlideService slideService;
	
	@RequestMapping(value="/slide/deleteall", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Integer getState() {
		return slideService.deleteAll();
	}
}
