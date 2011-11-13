package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.Verse;

@SuppressWarnings("serial")
@Service("songService")
@Repository
public class SongServiceImpl implements ISongService, Serializable {

	private transient EntityManager em;

	public SongServiceImpl() {
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<Song> findByPattern(String pattern) {
		pattern = getSearchPattern(pattern);
		if (pattern != null) {
			return em.createQuery("select s from Song s ").getResultList();

			// return em
			// .createQuery(
			// "select s from Song s where s.noPunctuationText = :pattern order by s.name")
			// .setParameter("pattern", pattern).getResultList();
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public Song findById(Long id) {
		return em.find(Song.class, id);
	}

	@Transactional
	public void persist(Song songTr) {
		em.persist(songTr);
		em.flush();
		
		if (null != songTr.getVerses()) {
			// store also verses
			for (Verse verse : songTr.getVerses()) {
				em.persist(verse);
			}			
		}
	}

	@Transactional
	public void delete(Song song) {
		song = em.find(Song.class, song.getId());
		if (song != null) {
			em.remove(song);
		}
	}

	public Song createOrEditSong(Long id) {
		if (null != id) {
			return findById(id);
		} else {
			return new Song();
		}
	}

	private String getSearchPattern(String pattern) {
		if (StringUtils.hasText(pattern)) {
			return "%" + pattern.toLowerCase().replace('*', '%') + "%";
		} else {
			return "%";
		}
	}

}
