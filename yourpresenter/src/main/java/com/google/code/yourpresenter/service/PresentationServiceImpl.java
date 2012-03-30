package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.MediaMisc;
import com.google.code.yourpresenter.entity.MediaMiscImage;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Slide;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;
import com.google.code.yourpresenter.util.Logger;
import com.google.code.yourpresenter.util.LoggerFactory;

@SuppressWarnings("serial")
@Service
@Repository
public class PresentationServiceImpl implements IPresentationService,
		Serializable {

	private static Logger logger = LoggerFactory
			.getLogger(PresentationServiceImpl.class);

	private transient EntityManager em;

	@Autowired
	private ISlideService slideService;

	public PresentationServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public Presentation findById(Long id) {
		return em.find(Presentation.class, id);
	}

	public int findPositionById(Long id) {
		Query query = em
				.createQuery("SELECT p.possition FROM Presentation p WHERE p.id = :id");
		query.setParameter("id", id);

		// for some reason following string doesn't work ok and returns 0
		// allways
		// => rather iterate over list
		// return query.getFirstResult();
		@SuppressWarnings("unchecked")
		List<Integer> positions = query.getResultList();
		if (!CollectionUtils.isEmpty(positions)) {
			return positions.iterator().next();
		}
		return -1;
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
	public void persistSlides(Presentation presentation) {
		Song song = null;
		MediaMisc mediaMisc = null;
		if (null != (song = presentation.getSong())) {
			int i = 0;
			for (Verse verse : song.getVerses()) {
				Slide slide = new Slide(verse.getText(), presentation, i++);
				this.slideService.persist(slide);
				presentation.addSlide(slide);
			}
		} else if (null != (mediaMisc = presentation.getMediaMisc())) {
			int i = 0;
			for (MediaMiscImage mediaMiscImage : mediaMisc.getMediaMiscImages()) {
				// keep txt as empty string rather than null
				Slide slide = new Slide(null, presentation, i++);
				slide.setBgImage(mediaMiscImage.getBgImage());
				this.slideService.persist(slide);
				presentation.addSlide(slide);
			}
		}
	}

	@Transactional
	@Override
	public void setBgImage(long presentationId, BgImage bgImage) {
		Presentation presentation = findById(presentationId);
		this.setBgImage(presentation, bgImage);
	}

	@Transactional
	@Override
	public void setBgImage(Presentation presentation, BgImage bgImage) {
		presentation.setBgImage(bgImage);
		this.persist(presentation);

		for (Slide slide : presentation.getSlides()) {
			// persist in DB only in case of changed bgImage
			if (bgImage.equals(slide.getBgImage())) {
				continue;
			}

			// in case bgImage is fixed and can't be replaced
			if ((null != slide.getBgImage()) && !slide.getBgImage().isReplaceable()) {
				logger.debug(
						"Slide bgImage is not replacable => keeping untouched: ",
						slide);
				continue;
			}

			slide.setBgImage(bgImage);
			this.slideService.persist(slide);
		}
	}

	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Presentation p");
		return query.executeUpdate();
	}
}
