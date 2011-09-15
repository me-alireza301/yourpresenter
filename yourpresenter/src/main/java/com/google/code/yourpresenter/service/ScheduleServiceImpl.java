package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.scheduled.Presentation;
import com.google.code.yourpresenter.entity.scheduled.Schedule;
import com.google.code.yourpresenter.entity.scheduled.Slide;
import com.google.code.yourpresenter.view.IHasSchedule;

@SuppressWarnings("serial")
@Service("scheduleService")
@Repository
public class ScheduleServiceImpl implements IScheduleService, Serializable {

	private EntityManager em;

	private IPresentationService presentationService;

	private ISongService songService;

	private ISlideService slideService;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Autowired
	public ScheduleServiceImpl(IPresentationService presentationService, ISongService songService, 
			ISlideService slideService) {
		this.presentationService = presentationService;
		this.songService = songService;
		this.slideService = slideService;
	}
	
	@Transactional(readOnly = true)
	public Schedule findScheduleById(Long id) {
		if (null == id) {
			return null;
		}
		return em.find(Schedule.class, id);
	}

	@Transactional
	public void persistSchedule(Schedule schedule) {
//		schedule = this.findScheduleById(schedule.getId());
		if (null != schedule.getId()) {
			em.merge(schedule);
		} else {
			em.persist(schedule);
			// make sure identity field is generated prio to relation 
			em.flush();
		}
	}

	@Transactional
	public void deleteSchedule(Schedule schedule) {
		schedule = em.find(Schedule.class, schedule.getId());
		if (schedule != null) {
			em.remove(schedule);
		}
	}

	public Schedule createOrEditSchedule(Long id) {
		if (null != id) {
			return findScheduleById(id);
		} else {
			return new Schedule();
		}
	}
	
	public Schedule createOrEditSchedule(Schedule schedule) {
		Long id = null;
		if (null != schedule) {
			id = schedule.getId();
		}
		return createOrEditSchedule(id);
	}

	@Transactional
	public void addPresentation(IHasSchedule callback, final Schedule scheduleTr, final Song songTr) {
		// to prevent:
		// Exception: failed to lazily initialize a collection of role: 
		// com.google.code.yourpresenter.entity.Song.verses, no session or session was closed
		Song song = songService.findSongById(songTr.getId());

		// to prevent: 
		// org.hibernate.PersistentObjectException: detached entity passed to persist: 
		// com.google.code.yourpresenter.entity.scheduled.Schedule
		Schedule schedule = createOrEditSchedule(scheduleTr);
		this.persistSchedule(schedule);
		
		Presentation presentation = presentationService.createOrEdit(null);
		presentation.setSchedule(schedule);
		presentation.setSong(song);
		this.presentationService.persist(presentation);
		
		// set changed state to view, to display after refresh
//		em.detach(schedule);
		callback.setSchedule(schedule);
	}
	
	@Transactional(readOnly = true)
	public Schedule loadAllSlidesEager(Schedule scheduleTr) {
		Schedule schedule = null;
		if (null != scheduleTr) {
			schedule = findScheduleById(scheduleTr.getId());	
		}
		
		if (null != schedule) {
				List<Presentation> presentations = schedule.getPresentations();
				presentations.toString();
				// the rest (slides are loaded via FetchType.EAGER on schedule.slides)
		}
		return schedule;
	}
}
