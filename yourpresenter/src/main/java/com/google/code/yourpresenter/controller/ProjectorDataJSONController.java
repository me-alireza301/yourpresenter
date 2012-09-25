package com.google.code.yourpresenter.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.service.IStateDTOService;

// for json spring integration info see:
// http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/
@Controller
@Slf4j
public class ProjectorDataJSONController {

	@Autowired
	private IStateDTOService stateService;

	@RequestMapping(value="/state/{name}", method = { RequestMethod.GET, RequestMethod.POST }/*, consumes="application/json", produces="application/json"*/)
	public @ResponseBody StateDTO getState(@PathVariable("id") Long scheduleId) {
		log.debug("called getState for: {}", scheduleId);
		return stateService.findByScheduleId(scheduleId);
	}
}
