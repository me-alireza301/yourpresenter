package com.google.code.yourpresenter.dto;

import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Slide;

public class ScheduleDTO {
	private Slide actualSlide;
	private Schedule schedule;
	
	public ScheduleDTO(Slide actualSlide, Schedule schedule) {
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
