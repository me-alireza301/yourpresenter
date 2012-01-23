package com.google.code.yourpresenter.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.ajaxpush.IStateChangedPropagator;
import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;

@SuppressWarnings("serial")
@Service
public class StateServiceImpl implements IStateService, Serializable {

	@Autowired
	private ISlideService slideService;
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IStateChangedPropagator stateChangedPropagator;

	
	// there are specific needs for ehcache annotations, see:
	// http://code.google.com/p/ehcache-spring-annotations/wiki/FrequentlyAskedQuestions
	// 3. Where does @Cacheable go in my source?
	@TriggersRemove(cacheName={"scheduleCache", "stateCache"}, 
	        keyGenerator = @KeyGenerator (
	                name = "HashCodeCacheKeyGenerator",
	                properties = @Property( name="includeMethod", value="false" )
	            )
	        )
	@Override
	public void stateChanged(String scheduleName) throws YpException {
		stateChangedPropagator.stateChanged(scheduleName, getState(scheduleName));
	}

	@Cacheable(cacheName="stateCache",
			keyGenerator = @KeyGenerator (
	                name = "HashCodeCacheKeyGenerator",
	                properties = @Property( name="includeMethod", value="false" )
				)
			)
	public StateDTO getState(String scheduleName) {
		Slide slide = slideService.findActiveSlide(scheduleName);
		Schedule schedule = scheduleService.findByName(scheduleName);
		return new StateDTO(slide, schedule);		
	}
}
