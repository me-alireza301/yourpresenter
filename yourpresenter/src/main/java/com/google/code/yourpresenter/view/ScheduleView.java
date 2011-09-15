package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.richfaces.component.UICommandLink;
import org.richfaces.event.DropEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.scheduled.Schedule;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;

@Component("scheduleView")
@Scope("session")
@SuppressWarnings("serial")
public class ScheduleView implements Serializable, IHasSchedule {

	Logger logger = LoggerFactory.getLogger(ScheduleView.class);

	private Schedule schedule;
	
	private IScheduleService scheduleService;
	
	private ISlideService slideService;
	
	private Long activeSlideId = -1L;
	
	@Autowired
	public ScheduleView(IScheduleService scheduleService, ISlideService slideService) {
		this.scheduleService = scheduleService;
		this.slideService = slideService;
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
	
	public Long getActiveSlideId() {
		if (this.activeSlideId == -1) {
			// TODO
		}
		return activeSlideId;
	}

	public void setActiveSlideId(Long activeSlideId) throws YpException {
		this.activeSlideId = activeSlideId;
		this.slideService.activateSlide(activeSlideId);
	}
	
	public void activateSlide() throws NumberFormatException, YpException {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String songId = (String) map.get("id");
		if (null != songId && !songId.isEmpty()) { 
			slideService.activateSlide(Long.valueOf(songId));
		} else {
			throw new YpException(YpError.SLIDE_ID_NOT_SET);
		}
		
	}
}

