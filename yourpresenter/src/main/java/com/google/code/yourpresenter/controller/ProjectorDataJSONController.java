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
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;

// for json spring integration info see:
// http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/
@Controller
public class ProjectorDataJSONController {

	private static Logger logger = LoggerFactory.getLogger(ProjectorDataJSONController.class);
	
	@Autowired
	private ISlideService slideService;
	
	@Autowired
	private IScheduleService scheduleService;

	@Cacheable(cacheName="stateCache",
			keyGenerator = @KeyGenerator (
	                name = "HashCodeCacheKeyGenerator",
	                properties = @Property( name="includeMethod", value="false" )
				)
			)
	@RequestMapping(value="/state/{name}", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody StateDTO getState(@PathVariable("name") String scheduleName) {
		logger.debug("called getState for: " + scheduleName);
		Slide slide = this.slideService.findActiveSlide(scheduleName);
		Schedule schedule = this.scheduleService.findByName(scheduleName);
		// to prevent error: 
		// failed to lazily initialize a collection of role: com.google.code.yourpresenter.entity.Schedule.presentations, no session or session was closed
//		schedule = this.scheduleService.loadAllSlidesEager(schedule);
		return new StateDTO(slide, schedule);
	}
}
