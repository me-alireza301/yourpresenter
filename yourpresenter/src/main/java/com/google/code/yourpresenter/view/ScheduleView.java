package com.google.code.yourpresenter.view;

import java.io.Serializable;

import org.richfaces.event.DropEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.scheduled.Schedule;
import com.google.code.yourpresenter.service.IScheduleService;

@Component("scheduleView")
@Scope("session")
@SuppressWarnings("serial")
public class ScheduleView implements Serializable, IHasSchedule {

	Logger logger = LoggerFactory.getLogger(ScheduleView.class);

	private Schedule schedule;
	
	private IScheduleService scheduleService;
	
	@Autowired
	public ScheduleView(IScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	public Schedule getSchedule() {
		return scheduleService.loadAllSlidesEager(this.schedule);
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public void add(DropEvent dropEvent) {
//		String sourceId = dropEvent.getDragSource().getId();
		Object dragValue = dropEvent.getDragValue();
		if (dragValue instanceof Song) {
			logger.debug("Song added to schedule: ", (Song) dragValue);
			add((Song) dragValue);
		} else {
			logger.warn("drop of not supported element type detected: ", dragValue);
		}
	}
	
	public void add(Song song) {
		this.scheduleService.addPresentation(this, this.schedule, song);
	}
}
