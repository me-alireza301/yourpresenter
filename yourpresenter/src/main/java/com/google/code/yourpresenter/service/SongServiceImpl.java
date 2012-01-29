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
import org.springframework.util.StringUtils;

import com.google.code.yourpresenter.entity.Song;

@SuppressWarnings("serial")
@Service
@Repository
public class SongServiceImpl implements ISongService, Serializable {

	@Autowired
	private IVerseService verseService;
	
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
		if (null != songTr.getId()) {
			em.merge(songTr);
		} else {
			em.persist(songTr);
			// make sure identity field is generated prio to relation
			em.flush();
		}
	}

	@Transactional
	@Override
	public int delete(Song song) {
		return deleteById(song.getId());
	}

	@Transactional
	@Override
	public int deleteById(long songId) {
		verseService.deleteBySongId(songId);

		Query  query = em.createQuery("delete from Song s where s.id like :id");
		query.setParameter("id", songId);
		return query.executeUpdate();
	}

	private String getSearchPattern(String pattern) {
		if (StringUtils.hasText(pattern)) {
			return "%" + pattern.toLowerCase().replace('*', '%') + "%";
		} else {
			return "%";
		}
	}

	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Song s");
		return query.executeUpdate();
	}

	@Transactional(readOnly = true)
	@Override
	public String findNameById(Long id) {
		Query query = em
				.createQuery("SELECT s.name FROM Song s WHERE s.id = :id");
		query.setParameter("id", id);

		// for some reason following string doesn't work ok and returns 0
		// allways
		// => rather iterate over list
		// return query.getFirstResult();
		@SuppressWarnings("unchecked")
		List<String> positions = query.getResultList();
		if (!CollectionUtils.isEmpty(positions)) {
			return positions.iterator().next();
		}

		return null;
	}
	
}
