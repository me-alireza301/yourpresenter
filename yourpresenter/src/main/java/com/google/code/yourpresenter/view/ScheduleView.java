package com.google.code.yourpresenter.view;

import java.io.IOException;
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
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.IPresentationService;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;
import com.google.code.yourpresenter.service.IStateService;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;

@Component("scheduleView")
@Scope("session")
@SuppressWarnings("serial")
public class ScheduleView implements Serializable/*, IHasSchedule*/ {

	private static Logger logger = LoggerFactory.getLogger(ScheduleView.class);
	
	private String scheduleName;
	
	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IPresentationService presentationService;
	@Autowired
	private ISlideService slideService;
	@Autowired
	private IStateService stateService;
	
	public Schedule getSchedule() throws IOException {
		return scheduleService.loadAllSlidesEager(getSchedule(this.scheduleName));
	}
	
	private Schedule getSchedule(String scheduleName) {
		return this.scheduleService.findByName(scheduleName);
	}
	
	public String getScheduleName() {
		return getSchedule(this.scheduleName).getName();
	}
	
	public void setSchedule(Schedule schedule) throws YpException {
		this.scheduleName = schedule.getName();

		// make sure state change is propagated
		this.stateService.stateChanged(scheduleName);
	}

	public void dropped(DropEvent dropEvent) throws YpException, IOException {
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
		} else if (dragValue instanceof Presentation) {
				if (level.equals("schedule")) {
					// move presentation to the start (position 0)
					dropped((Presentation) dragValue, -1);
				} else if (level.equals("presentation")) {
					// move presentation after presentation (position id)
					dropped((Presentation) dragValue, id);
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
		
		// make sure state change is propagated
		this.stateService.stateChanged(scheduleName);
	}
	
	private void dropped(Presentation presentation, long presentationId) throws IOException {
		logger.debug("Moved presentation (presentation.id=", presentation.getId(), ") in (schedule=", this.scheduleName, 
				") after presentation (presentation.id=", presentationId, ")");
		this.scheduleService.movePresentation(this.getSchedule(), presentationId, presentation);
	}

	public void dropped(Song song, long presentationId) throws IOException {
		logger.debug("Added Song (song.id=", song.getId(), ") to schedule (schedule=", this.scheduleName, 
				") after presentation (presentation.id=", presentationId, ")");
		this.scheduleService.addPresentation(this.getSchedule(), presentationId, song);
	}
	
	public void droppedToSlide(BgImage bgImage, long slideId) {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to slide (slide.id=", slideId);
		this.slideService.setBgImage(slideId, bgImage);
	}
	
	public void droppedToPresentation(BgImage bgImage, long presentationId) {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to presentation (presentation.id=", presentationId);
		this.presentationService.setBgImage(presentationId, bgImage);
	}
	
	public void droppedToSchedule(BgImage bgImage) throws IOException {
		logger.debug("Assigned bgImage (bgImage.id=", bgImage.getId(), ") to schedule (schedule=", this.scheduleName);
		this.scheduleService.setBgImage(this.getSchedule(), bgImage);
	}
	
	public void activateSlide() throws NumberFormatException, YpException {
		@SuppressWarnings("rawtypes")
		Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String songId = (String) map.get("id");
		if (null != songId && !songId.isEmpty()) { 
			slideService.activateSlide(Long.valueOf(songId));
			
			// make sure state change is propagated
			this.stateService.stateChanged(scheduleName);
		} else {
			// for the case of drop of background on slide link is called as well 
			// (seems like activation, but no id is sent)
			// => for such a case do nothing
			
			// throw new YpException(YpError.SLIDE_ID_NOT_SET);
		}
	}

	public boolean getBlank() {
		return this.getSchedule(scheduleName).isBlank();
	}
	
	public void toggleBlank() throws YpException {
		scheduleService.toggleBlank(this.getSchedule(scheduleName));
		
		// make sure state change is propagated
		this.stateService.stateChanged(scheduleName);
	}
	
	public String getToggleBlankCssSuffix() {
		return (this.getSchedule(scheduleName).isBlank() ? "down" : "up" );
	}
	
	public boolean getClear() {
		return this.getSchedule(scheduleName).isClear();
	}
	
	public void toggleClear() throws YpException {
		scheduleService.toggleClear(this.getSchedule(scheduleName));
		
		// make sure state change is propagated
		this.stateService.stateChanged(scheduleName);
	}
	
	public String getToggleClearCssSuffix() {
		return (this.getSchedule(scheduleName).isClear() ? "down" : "up" );
	}
	
	public boolean getLive() {
		return this.getSchedule(scheduleName).isLive();
	}
	
	public void toggleLive() throws YpException {
		scheduleService.toggleLive(this.getSchedule(scheduleName));
		
		// make sure state change is propagated
		this.stateService.stateChanged(scheduleName);
	}
	
	public String getToggleLiveCssSuffix() {
		return (this.getSchedule(scheduleName).isLive() ? "down" : "up" );
	}
}
