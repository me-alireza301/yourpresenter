package com.google.code.yourpresenter.view;

import java.io.IOException;
import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.richfaces.event.DropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.service.IPresentationService;
import com.google.code.yourpresenter.service.IScheduleService;
import com.google.code.yourpresenter.service.ISlideService;
import com.google.code.yourpresenter.service.IStateDTOService;

@Component("scheduleView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
@Slf4j
public class ScheduleView implements Serializable, IScheduleHolder {

	private Schedule schedule;
	private Long presentationIdToDelete = null;
	private Long activeSlideId = null;

	@Autowired
	private IScheduleService scheduleService;
	@Autowired
	private IPresentationService presentationService;
	@Autowired
	private ISlideService slideService;
	@Autowired
	private IStateDTOService stateService;

	public Schedule getSchedule() {
		if (null != schedule) {
			return scheduleService.findById(schedule.getId());	
		} else {
			return null;
		}
	}

	public Long getScheduleId() {
		return getSchedule().getId();
	}

	public void setSchedule(Schedule schedule) throws YpException {
		this.schedule = schedule;
	}

	public void dropped(DropEvent dropEvent) throws YpException, IOException {
		// TODO extract to new class DnDDispatcher
		Object dragValue = dropEvent.getDragValue();
		String dropValue = (String) dropEvent.getDropValue();

		if (StringUtils.isEmpty(dropValue)) {
			throw new YpException(YpError.EMPTY_DROP_VALUE);
		}

		if ((null == dropValue) || (!dropValue.contains("_"))) {
			throw new YpException(YpError.EMPTY_DROP_VALUE);
		}

		String[] split = dropValue.split("_");
		if (1 == split.length) {
			split = new String[] { split[0], "0" };
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
		} else if (dragValue instanceof Media) {
			if (level.equals("schedule")) {
				// add MediaMisc at start (position 0)
				dropped((Media) dragValue, -1);
			} else if (level.equals("presentation")) {
				// add MediaMisc after presentation (position id)
				dropped((Media) dragValue, id);
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
			log.error("drop of not supported element type detected: {}",
					dragValue);
		}

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	private void dropped(Media mediaMisc, long presentationId)
			throws IOException, YpException {
		log.debug(
				"Added MediaMisc (mediaMisc.id={}) to schedule (schedule={}) after presentation (presentation.id={})",
				new Object[] { mediaMisc.getId(), this.getSchedule().getId(),
						presentationId });
		this.scheduleService.addPresentation(this.getSchedule(),
				presentationId, null, mediaMisc);
	}

	private void dropped(Presentation presentation, long presentationId)
			throws IOException, YpException {
		log.debug(
				"Moved presentation (presentation.id={}) in (schedule={}) after presentation (presentation.id=)",
				new Object[] { presentation.getId(), this.getSchedule().getId(),
						presentationId });
		this.scheduleService.movePresentation(this.getSchedule(),
				presentationId, presentation);
	}

	public void dropped(Song song, long presentationId) throws IOException, YpException {
		log.debug(
				"Added Song (song.id={}) to schedule (schedule={}) after presentation (presentation.id={})",
				new Object[] { song.getId(), this.getSchedule().getId(), presentationId });
		this.scheduleService.addPresentation(this.getSchedule(),
				presentationId, song, null);
	}

	public void droppedToSlide(BgImage bgImage, long slideId) {
		log.debug("Assigned bgImage (bgImage.id={}) to slide (slide.id={}",
				bgImage.getId(), slideId);
		this.slideService.setBgImage(slideId, bgImage);
	}

	public void droppedToPresentation(BgImage bgImage, long presentationId) {
		log.debug(
				"Assigned bgImage (bgImage.id={}) to presentation (presentation.id={}",
				bgImage.getId(), presentationId);
		this.presentationService.setBgImage(presentationId, bgImage);
	}

	public void droppedToSchedule(BgImage bgImage) throws IOException {
		log.debug("Assigned bgImage (bgImage.id={}) to schedule (schedule={}",
				bgImage.getId(), this.getSchedule().getId());
		this.scheduleService.setBgImage(this.getSchedule(), bgImage);
	}

	/**
	 * Called whenever slide is being activated.
	 * 
	 * @throws NumberFormatException
	 * @throws YpException
	 */
	public void activateSlide() throws NumberFormatException, YpException {
	if (null == getActiveSlideId()) {
		throw new YpException(YpError.SLIDE_ID_NOT_SET);
	}
	log.debug(
			"Slide has been activated (slide.id={} in schedule.id={}).",
			new Object[] { getActiveSlideId(), this.getSchedule().getId() });
	
	slideService.activateSlide(Long.valueOf(getActiveSlideId()));

	// make sure state change is propagated
	this.stateService.stateChanged(this.getSchedule(), this);
	}

	public boolean getBlank() {
		return this.getSchedule().isBlank();
	}

	public void toggleBlank() throws YpException {
		scheduleService.toggleBlank(this.getSchedule());

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	public String getToggleBlankCssSuffix() {
		return (this.getSchedule().isBlank() ? "down" : "up");
	}

	public boolean getClear() {
		return this.getSchedule().isClear();
	}

	public void toggleClear() throws YpException {
		scheduleService.toggleClear(this.getSchedule());

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	public String getToggleClearCssSuffix() {
		return (this.getSchedule().isClear() ? "down" : "up");
	}

	public boolean getLive() {
		return this.getSchedule().isLive();
	}

	public void toggleLive() throws YpException {
		scheduleService.toggleLive(this.getSchedule());

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	public String getToggleLiveCssSuffix() {
		return (this.getSchedule().isLive() ? "down" : "up");
	}

	public void delete() throws YpException {
		if (null == this.getSchedule()) {
			throw new YpException(YpError.SCHEDULE_DELETE_FAILED,
					"Schedule to be deleted is null!");
		}
		scheduleService.delete(this.getSchedule());

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	public void deletePresentation() throws YpException {
		Presentation p = this.getPresentationToDelete();
		if (null == p) {
			throw new YpException(YpError.PRESENTATION_DELETE_FAILED,
					"Presentation to be deleted is null!");
		}

		this.scheduleService.deletePresentation(this.getSchedule(),
				getPresentationIdToDelete());

		// make sure we clear the internal var holding to be deleted
		// presentation
		this.presentationIdToDelete = null;

		// make sure state change is propagated
		this.stateService.stateChanged(this.getSchedule(), this);
	}

	public Presentation getPresentationToDelete() {
		if (null != presentationIdToDelete) {
			return this.presentationService
					.findById(this.presentationIdToDelete);
		}
		return null;
	}

	public Long getPresentationIdToDelete() {
		return presentationIdToDelete;
	}

	public void setPresentationIdToDelete(Long presentationIdToDelete) {
		this.presentationIdToDelete = presentationIdToDelete;
	}

	/**
	 * @return the activeSlideId
	 */
	public Long getActiveSlideId() {
		return activeSlideId;
	}

	/**
	 * @param activeSlideId the activeSlideId to set
	 */
	public void setActiveSlideId(Long activeSlideId) {
		this.activeSlideId = activeSlideId;
	}
}
