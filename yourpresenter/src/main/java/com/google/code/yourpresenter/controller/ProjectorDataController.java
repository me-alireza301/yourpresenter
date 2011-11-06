package com.google.code.yourpresenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;

// for json spring integration info see:
// http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/
@Controller
public class ProjectorDataController {

	@Autowired
	private ISlideService slideService;
	
	@Autowired
	private IScheduleService scheduleService;

	@RequestMapping(value="/state/{name}", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody StateDTO getState(@PathVariable("name") String scheduleName) {
		Slide slide = this.slideService.findActiveSlide(scheduleName);
		Schedule schedule = this.scheduleService.findByName(scheduleName);
		// to prevent error: 
		// failed to lazily initialize a collection of role: com.google.code.yourpresenter.entity.Schedule.presentations, no session or session was closed
//		schedule = this.scheduleService.loadAllSlidesEager(schedule);
		return new StateDTO(slide, schedule);
	}
}
