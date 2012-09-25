package com.google.code.yourpresenter.service;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Slide;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;

@SuppressWarnings("serial")
@Service
@Repository
public class PresentationServiceImpl implements IPresentationService,
		Serializable {

	private transient EntityManager em;

	@Autowired
	private ISlideService slideService;

	public PresentationServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
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
		if (null != presentation.getId()) {
			em.merge(presentation);
		} else {
			em.persist(presentation);
			// make sure identity field is generated prio to relation
			em.flush();
		}
	}

	@Transactional
	@Override
	public Presentation addSlides(Presentation presentation) {
		Song song = null;
		Media media = null;
		if (null != (song = presentation.getSong())) {
			for (Verse verse : song.getVerses()) {
				Slide slide = new Slide(verse.getText(), presentation);
				presentation.addSlide(slide);
			}
		} else if (null != (media = presentation.getMedia())) {
			for (BgImage bgImage : media.getBgImages()) {
				// keep txt as empty string rather than null
				Slide slide = new Slide(null, presentation);
				slide.setBgImage(bgImage);
				presentation.addSlide(slide);
			}
		}
		return presentation;
	}

	@Transactional
	@Override
	public void setBgImage(long presentationId, BgImage bgImage) {
		this.setBgImage(findById(presentationId), bgImage);
	}

	@Transactional
	@Override
	public void setBgImage(Presentation presentation, BgImage bgImage) {
		presentation.setBgImage(bgImage);
		for (Slide slide : presentation.getSlides()) {
			this.slideService.setBgImage(slide, bgImage);
		}
		
		this.persist(presentation);
	}

	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Presentation p");
		return query.executeUpdate();
	}
}
