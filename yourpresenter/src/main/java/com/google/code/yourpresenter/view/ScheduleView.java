package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.DropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.IPresentationService;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;

@Component("scheduleView")
@Scope("session")
@SuppressWarnings("serial")
public class ScheduleView implements Serializable, IHasSchedule {

	private static Logger logger = LoggerFactory.getLogger(ScheduleView.class);

	private Schedule schedule;
	
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IPresentationService presentationService;
	@Autowired
	private ISlideService slideService;
	
	public Schedule getSchedule() {
		return scheduleService.loadAllSlidesEager(this.schedule);
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public void dropped(DropEvent dropEvent) throws YpException {
		// TODO extract to new class DnDDispatcher
		Object dragValue = dropEvent.getDragValue();
		String dropValue = (String) dropEvent.getDropValue();
		
		if (StringUtils.isEmpty(dropValue)) {
			throw new YpException(YpError.EMPTY_DROP_VALUE);
		}
		
		if ((null == dropValue) || (!dropValue.contains("_"))){
			throw new YpException(YpError.EMPTY_DROP_VALUE);
		}
		
		String[] split = dropValue.split("_");
		if (1 == split.length) {
			split = new String[] { split[0], "0"};
		}
		
		long id = Long.valueOf(split[1]);
		String level = split[0];
			
		if (dragValue instanceof Song) {
			if (level.equals("schedule")) {
				// add song at start (position 0)
				dropped((Song) dragValue, -1);
			} else if (level.equals("presentation")) {
				// add song after presentation (position id)
				dropped((Song) dragValue, id);
			}
		} else if (dragValue instanceof BgImage) {
			if (level.equals("schedule")) {
				droppedToSchedule((BgImage) dragValue);
			} else if (level.equals("presentation")) {
				droppedToPresentation((BgImage) dragValue, id);
			} else if (level.equals("slide")) {
				droppedToSlide((BgImage) dragValue, id);
			}
		} else {
			logger.error("drop of not supported element type detected: ", dragValue);
		}
	}
	
	public void dropped(Song song, long presentationId) {
		logger.debug("Added Song (song.id=", song.getId(), ") to schedule (schedule=", this.schedule, 
				") after presentation (presentation.id=", presentationId, ")");
		this.scheduleService.addPresentation(this, this.schedule, presentationId, song);
	}
	
	public void droppedToSlide(BgImage bgImage, long slideId) {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to slide (slide.id=", slideId);
		this.slideService.setBgImage(slideId, bgImage);
	}
	
	public void droppedToPresentation(BgImage bgImage, long presentationId) {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to presentation (presentation.id=", presentationId);
		this.presentationService.setBgImage(presentationId, bgImage);
	}
	
	public void droppedToSchedule(BgImage bgImage) {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to schedule (schedule=", this.schedule);
		this.scheduleService.setBgImage(this.schedule, bgImage);
	}
	
	public void activateSlide() throws NumberFormatException, YpException {
		FacesContext context = FacesContext.getCurrentInstance();
		@SuppressWarnings("rawtypes")
		Map map = context.getExternalContext().getRequestParameterMap();
		String songId = (String) map.get("id");
		if (null != songId && !songId.isEmpty()) { 
			slideService.activateSlide(Long.valueOf(songId));
		} else {
			throw new YpException(YpError.SLIDE_ID_NOT_SET);
		}
		
	}
}
