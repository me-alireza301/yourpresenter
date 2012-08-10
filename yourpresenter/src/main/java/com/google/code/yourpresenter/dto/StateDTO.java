package com.google.code.yourpresenter.dto;

import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;

public class StateDTO {
	private Slide actualSlide;
	private Schedule schedule;
	
	public StateDTO() {
	}
	
	public StateDTO(Slide actualSlide, Schedule schedule) {
		super();
		this.actualSlide = actualSlide;
		this.schedule = schedule;
	}
	
	public Slide getActualSlide() {
		return actualSlide;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	
}
