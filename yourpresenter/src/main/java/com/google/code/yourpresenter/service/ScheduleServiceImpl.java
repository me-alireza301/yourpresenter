package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;

@SuppressWarnings("serial")
@Service
@Repository
public class ScheduleServiceImpl implements IScheduleService, Serializable {

	private EntityManager em;

	@Autowired
	private IPresentationService presentationService;
	@Autowired
	private ISongService songService;

	@Autowired
	private IMediaService mediaMiscService;

	public ScheduleServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Schedule> findAll() {
		Query query = em.createQuery("SELECT s FROM Schedule s");
		return query.getResultList();
	}

	@Cacheable(value = { "scheduleById" })
	@Override
	public Schedule findById(Long id) {
		return em.find(Schedule.class, id);
	}
	
	@Transactional(readOnly = true)
	public Schedule findByName(String name) {
		Query query = em
				.createQuery("SELECT s FROM Schedule s WHERE s.name = :name)");
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<Schedule> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			return oldSelections.iterator().next();
		}
		return null;
	}

	// as it's called only inernally => @CacheEvict present on all the methods
	// calling this one
//	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	public Schedule persist(Schedule schedule) {
		if (null != schedule.getId()) {
			em.merge(schedule);
		} else {
			em.persist(schedule);
		}
		return schedule;
		
	}

	@CacheEvict(value = "scheduleById", key = "#root.args[0].id" )
	@Transactional
	@Override
	public void delete(Schedule schedule) {
		schedule = em.find(Schedule.class, schedule.getName());
		if (schedule != null) {
			em.remove(schedule);
		}
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void addPresentation(final Schedule schedule, long presentationId,
			final Song songTr, final Media mediaMiscTr) throws YpException {

		// make sure schedule is not detached object => do the merge
		this.persist(schedule);

		Presentation presentation = presentationService.createOrEdit(null);
		presentation.setSchedule(schedule);

		if (null != songTr) {
			Song song = songService.findById(songTr.getId());
			presentation.setName(song.getName());
			presentation.setSong(song);
		} else if (null != mediaMiscTr) {
			Media mediaMisc = mediaMiscService.findById(mediaMiscTr.getId());
			presentation.setName(mediaMisc.getName());
			presentation.setMedia(mediaMisc);
		}

		// make sure that also slides relevant for song are to be persisted
		presentation = this.presentationService.addSlides(presentation);

		BgImage bgImage = schedule.getBgImage();
		if (null != bgImage) {
			presentation.setBgImage(bgImage);
		}
		
		List<Presentation> presentations = schedule.getPresentations();
		int toIdx = getPresentationIdx(presentations, presentationId);

		// add to required idx, or if last idx => add to the end
		if (toIdx < presentations.size()) {
			presentations.add(++toIdx, presentation);	
		} else {
			presentations.add(presentation);	
		}
			
		this.persist(schedule);
	}

	private int getPresentationIdx(List<Presentation> presentations, long presentationId) throws YpException {
		// if new schedule
		if (null == presentations || -1 == presentationId) {
			return -1;
		}

		int idx = 0;
		for (Presentation presentation : presentations) {
			if (presentationId == presentation.getId()) {
				return idx;
			}
			idx++;
		}
		
		throw new YpException(YpError.PRESENTAION_ID_INVALID);
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void setBgImage(Schedule schedule, BgImage bgImage) {
		schedule.setBgImage(bgImage);

		List<Presentation> presentations = schedule.getPresentations();
		if (!CollectionUtils.isEmpty(presentations)) {
			for (Presentation presentation : presentations) {
				presentation.setBgImage(bgImage);
			}
		}
		
		this.persist(schedule);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<String> findScheduleNamesByName(String name) {
		// JPA supports constructor calls, see:
		// http://www.objectdb.com/java/jpa/query/jpql/select
		// Query query =
		// em.createQuery("SELECT NEW com.google.code.yourpresenter.dto.ScheduleNameDTO(s.id, s.name) FROM Schedule s");

		Query query = null;
		if ((null == name) || StringUtils.isEmpty(name)) {
			query = em.createQuery("SELECT s.name FROM Schedule s");
		} else {
			query = em
					.createQuery("SELECT s.name FROM Schedule s WHERE s.name = :name");
			query.setParameter("name", name);
		}
		return (List<String>) query.getResultList();
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void toggleBlank(Schedule schedule) {
		schedule.setBlank(!schedule.isBlank());
		persist(schedule);
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void toggleClear(Schedule schedule) {
		schedule.setClear(!schedule.isClear());
		persist(schedule);
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void toggleLive(Schedule schedule) {
		schedule.setLive(!schedule.isLive());
		persist(schedule);
	}

	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void movePresentation(Schedule schedule, long presentationId,
			Presentation presentationTr) throws YpException {

		// make sure schedule is not detached object => load it
		schedule = this.findById(schedule.getId());
				
		List<Presentation> presentations = schedule.getPresentations();
		int fromIdx = getPresentationIdx(presentations, presentationTr.getId());
		int toIdx = getPresentationIdx(presentations, presentationId);

		// if nothing to be done
		 if (1 == presentations.size()) {
			 return;
		 }
		
		// add to required idx, or if last idx => add to the end
		if (toIdx < presentations.size()) {
			presentations.add(++toIdx, presentations.get(fromIdx));	
		} else {
			presentations.add(presentations.get(fromIdx));	
		}
		
		// make sure we remove correct element, if we moved to the start
		if (toIdx < fromIdx) {
			fromIdx++;
		}
		
		// then remove from the original one
		presentations.remove(fromIdx);
		
		this.persist(schedule);
	}
	
	@CacheEvict (value = "scheduleById", key = "#root.args[0].id")
	@Transactional
	@Override
	public void deletePresentation(Schedule schedule, long presentationId) throws YpException {
		List<Presentation> presentations = schedule.getPresentations();
		presentations.remove(getPresentationIdx(presentations, presentationId));
		this.persist(schedule);
	}

	@CacheEvict (value = "scheduleById", allEntries = true)
	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Schedule s");
		return query.executeUpdate();
	}

}
