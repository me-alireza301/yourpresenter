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
import com.google.code.yourpresenter.view.IScheduleHolder;

@SuppressWarnings("serial")
@Service
public class StateDTOServiceImpl implements IStateDTOService, Serializable {

	@Autowired
	private ISlideService slideService;
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IStateChangedPropagator stateChangedPropagator;
	
	@CacheEvict(value = { "scheduleById", "stateDTOByScheduleId" }, key = "#root.args[0].id" , beforeInvocation = true)
	@Override
	public void stateChanged(Schedule schedule, IScheduleHolder scheduleHolder) throws YpException {
		stateChangedPropagator.stateChanged(schedule.getId(), findByScheduleId(scheduleHolder.getSchedule().getId()));
	}

	@Cacheable(value = { "stateDTOByScheduleId" })
	public StateDTO findByScheduleId(Long scheduleId) {
		Schedule schedule = scheduleService.findById(scheduleId);
		Slide slide = slideService.findActiveSlide(schedule.getId());
		return new StateDTO(slide, schedule);		
	}
}
