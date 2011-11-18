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

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Slide;

@SuppressWarnings("serial")
@Service("slideService")
@Repository
public class SlideServiceImpl implements ISlideService, Serializable {

	private transient EntityManager em;

	public SlideServiceImpl() {
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public Slide findById(Long id) {
		return em.find(Slide.class, id);
	}

	public void persist(Slide slide) {
		if (null != slide.getId()) {
			em.merge(slide);
		} else {
			em.persist(slide);
			// make sure identity field is generated prio to relation
			em.flush();
		}
	}

	@Transactional(readOnly = true)
	public Slide findActiveSlide(String scheduleName) {
		Query query = em.createQuery(
				"SELECT sl FROM Slide sl WHERE sl.active = true AND sl.presentation IN (SELECT p FROM Presentation p WHERE p.schedule IN (SELECT sch FROM Schedule sch WHERE sch.name = :name))");
		query.setParameter("name", scheduleName);
		@SuppressWarnings("unchecked")
		List<Slide> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			return oldSelections.iterator().next();
		}
		return null;
	}
	
	@Transactional
	public void activateSlide(Long id) throws YpException {
		// for examples see:
		// http://en.wikibooks.org/wiki/Java_Persistence/Querying
		Query query = em.createQuery(
				"SELECT sl FROM Slide sl WHERE sl.active = true AND sl.presentation IN (SELECT p FROM Presentation p WHERE p.schedule IN (SELECT sch FROM Schedule sch JOIN sch.presentations p WHERE p IN (SELECT p FROM Presentation p JOIN p.slides sl WHERE sl.id = :id)))");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Slide> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			Slide oldSelection = oldSelections.iterator().next();
			oldSelection.setActive(false);
			this.persist(oldSelection);
		}

		Slide slide = findById(id);
		slide.getPresentation().getSchedule();
		slide.setActive(true);
		this.persist(slide);
	}
	
	@Transactional
	@Override
	public void setBgImage(Long slideId, BgImage bgImage) {
		Slide slide = findById(slideId);
		
		// persist in DB only in case of changed bgImage
		if (bgImage.equals(slide.getBgImage())) {
			return;
		}
		
		slide.setBgImage(bgImage);
		this.persist(slide);
	}
}
