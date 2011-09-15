package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.scheduled.Presentation;
import com.google.code.yourpresenter.entity.scheduled.Schedule;
import com.google.code.yourpresenter.entity.scheduled.Slide;

@SuppressWarnings("serial")
@Service("slideService")
@Repository
public class SlideServiceImpl implements ISlideService, Serializable {

	private transient EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public Slide findById(Long id) {
		return em.find(Slide.class, id);
	}

	public void persist(Slide slide) {
		em.persist(slide);
	}

	@Transactional
	public void activateSlide(Long id) throws YpException {
		// for examples see:
		// http://en.wikibooks.org/wiki/Java_Persistence/Querying
		String queryString = "SELECT sl FROM Slide sl WHERE sl.active = true AND sl.presentation IN (SELECT p FROM Presentation p WHERE p.schedule IN (SELECT sch FROM Schedule sch JOIN sch.presentations p WHERE p IN (SELECT p FROM Presentation p JOIN p.slides sl WHERE sl.id = :id)))";
		Query query = em.createQuery(queryString);
		query.setParameter("id", id);
		List<Slide> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			Slide oldSelection = oldSelections.iterator().next();
			oldSelection.setActive(false);
			merge(oldSelection);
		}

		Slide slide = findById(id);
		slide.getPresentation().getSchedule();
		slide.setActive(true);
		merge(slide);
	}
	
	public void merge(Slide slide) {
		em.merge(slide);
	}
}
