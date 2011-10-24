package com.google.code.yourpresenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.yourpresenter.dto.ScheduleDTO;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;

@Controller
public class ProjectorDataController {

	@Autowired
	private ISlideService slideService;
	
	@Autowired
	private IScheduleService scheduleService;

	@RequestMapping(value="/data", method=RequestMethod.GET)
	public @ResponseBody ScheduleDTO getAvailability(@RequestParam String scheduleName) {
		Slide slide = this.slideService.findActiveSlide(scheduleName);
		Schedule schedule = this.scheduleService.findByName(scheduleName);
		
		return new ScheduleDTO(slide, schedule);
	}
}
