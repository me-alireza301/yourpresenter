package com.google.code.yourpresenter.service;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;
import com.google.code.yourpresenter.entity.scheduled.Presentation;
import com.google.code.yourpresenter.entity.scheduled.Slide;

@SuppressWarnings("serial")
@Service("presentationService")
@Repository
public class PresentationServiceImpl implements IPresentationService, Serializable {

	private transient EntityManager em;
	
	private ISlideService slideService;

	public PresentationServiceImpl() {
	}

	@Autowired
	public PresentationServiceImpl(ISlideService slideService) {
		this.slideService = slideService;
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public Presentation findById(Long id) {
		return em.find(Presentation.class, id);
	}

	public Presentation createOrEdit(Long id) {
		if (null != id) {
			return findById(id);
		} else {
			return new Presentation();
		}
	}

	public void persist(Presentation presentation) {
		em.persist(presentation);
		em.flush();

		Song song = null;
		if (null != (song = presentation.getSong())) {
			for (Verse verse : song.getVerses()) {
				Slide slide = new Slide(verse, presentation);
				this.slideService.persist(slide);
			}
		}
	}
}
