package com.google.code.yourpresenter.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.ajaxpush.IStateChangedPropagator;
import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;

@SuppressWarnings("serial")
@Service
public class StateServiceImpl implements IStateService, Serializable {

	@Autowired
	private ISlideService slideService;
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IStateChangedPropagator stateChangedPropagator;
	
	@CacheEvict(value = { "scheduleCache", "stateCache" }, beforeInvocation = true)
	@Override
	public void stateChanged(String scheduleName) throws YpException {
		stateChangedPropagator.stateChanged(scheduleName, getState(scheduleName));
	}

	@Cacheable(value = { "stateCache" })
	public StateDTO getState(String scheduleName) {
		Slide slide = slideService.findActiveSlide(scheduleName);
		Schedule schedule = scheduleService.findByName(scheduleName);
		return new StateDTO(slide, schedule);		
	}
}
