package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;

@SuppressWarnings("serial")
@Service
@Repository
public class ScheduleServiceImpl implements IScheduleService, Serializable {

	private EntityManager em;

	@Autowired
	private IPresentationService presentationService;
	@Autowired
	private ISongService songService;

	public ScheduleServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Schedule> findAll() {
		Query query = em.createQuery("SELECT s FROM Schedule s");
		return query.getResultList();
	}

	@Cacheable(cacheName="scheduleCache", 
	        keyGenerator = @KeyGenerator (
	                name = "HashCodeCacheKeyGenerator",
	                properties = @Property( name="includeMethod", value="false" )
	            )
	        )
	@Transactional(readOnly = true)
	public Schedule findByName(String name) {
		if (null == name) {
			return null;
		}
		return em.find(Schedule.class, name);
	}

	@Transactional
	public void persist(Schedule schedule) {
		// schedule = this.findScheduleById(schedule.getId());
		if (null != schedule.getName()) {
			em.merge(schedule);
		} else {
			em.persist(schedule);
			// make sure identity field is generated prio to relation
			em.flush();
		}
	}

	@Transactional
	public void delete(Schedule schedule) {
		schedule = em.find(Schedule.class, schedule.getName());
		if (schedule != null) {
			em.remove(schedule);
		}
	}

	@Transactional
	@Override
	public void addPresentation(final Schedule schedule,
			long presentationId, final Song songTr) {
		// to prevent:
		// Exception: failed to lazily initialize a collection of role:
		// com.google.code.yourpresenter.entity.Song.verses, no session or
		// session was closed
		Song song = songService.findById(songTr.getId());
		
		// make sure schedule is not detached object => do the merge
		this.persist(schedule);
		
		int position = shiftPresentation(presentationId, schedule, true);

		Presentation presentation = presentationService.createOrEdit(null);
		presentation.setPossition(position);
		presentation.setSchedule(schedule);
		presentation.setSong(song);
		this.presentationService.persist(presentation);
		// make sure that also slides relevant for song are to be persisted
		this.presentationService.persistSlides(presentation);

		BgImage bgImage = schedule.getBgImage();
		if (null != bgImage) {
			presentation = this.presentationService.findById(presentation
					.getId());
			this.presentationService.setBgImage(presentation, bgImage);
		}
	}

	private int shiftPresentation(long presentationId, Schedule schedule, boolean forward) {
		List<Presentation> presentations = schedule.getPresentations();

		// if new schedule
		if (null == presentations) {
			return 0;
		}

		int position = 0;
		if (-1 != presentationId) {
			position = presentationService.findPositionById(presentationId);
			position++;
		}

		int maxIdx = presentations.size();
		for (int idx = 0; idx < maxIdx; idx++) {
			Presentation toShiftPres = presentations.get(idx);
			// skip all the presentations not to be affected by shift (before target position)
			if (toShiftPres.getPossition() < position) {
				continue;
			}
			
			if (forward) {
				toShiftPres.incrementPossition();
			} else {
				toShiftPres.decrementPossition();	
			}
			this.presentationService.persist(toShiftPres);	
		}
		return position;
	}

	@Transactional(readOnly = true)
	public Schedule loadAllSlidesEager(Schedule scheduleTr) {
		Schedule schedule = null;
		if (null != scheduleTr) {
			schedule = findByName(scheduleTr.getName());
		}

		if (null != schedule) {
			List<Presentation> presentations = schedule.getPresentations();
			presentations.toString();
			// the rest (slides are loaded via FetchType.EAGER on
			// schedule.slides)
		}
		return schedule;
	}

	@Transactional
	@Override
	public void setBgImage(Schedule schedule, BgImage bgImage) {
		schedule.setBgImage(bgImage);
		this.persist(schedule);

		List<Presentation> presentations = schedule.getPresentations();
		if (!CollectionUtils.isEmpty(presentations)) {
			for (Presentation presentation : presentations) {
				this.presentationService.setBgImage(presentation, bgImage);
			}
		}
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

	@Transactional
	@Override
	public void toggleBlank(Schedule schedule) {
		schedule.setBlank(!schedule.isBlank());
		persist(schedule);
	}

	@Transactional
	@Override
	public void toggleClear(Schedule schedule) {
		schedule.setClear(!schedule.isClear());
		persist(schedule);
	}

	@Transactional
	@Override
	public void toggleLive(Schedule schedule) {
		schedule.setLive(!schedule.isLive());
		persist(schedule);
	}

	@Transactional
	@Override
	public void movePresentation(Schedule schedule, long presentationId,
			Presentation presentationTr) {
		// to prevent:
		// Exception: failed to lazily initialize a collection of role:
		// com.google.code.yourpresenter.entity.Song.verses, no session or
		// session was closed
		Presentation presentation = presentationService.findById(presentationTr.getId());
		
		// TODO tune performance detect cases where no update necessary and skip DB actions
		// nothing to be done
//		if (presentation.getPossition() == 1 && presentationId == -1) {
//			
//		}
		
		// make sure schedule is not detached object => do the merge
		this.persist(schedule);
		
		// move all the presentations after this one in schedule backward
		shiftPresentation(presentation.getId(), schedule, false);
		
		// move all the presentations before this one in schedule forward
		int newPosition = shiftPresentation(presentationId, schedule, true);

		presentation.setPossition(newPosition);
		this.presentationService.persist(presentation);
	}

	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Schedule s");
		return query.executeUpdate();
	}

}
