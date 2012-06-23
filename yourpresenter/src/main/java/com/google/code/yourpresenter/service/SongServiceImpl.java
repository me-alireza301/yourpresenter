package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.entity.Song;

@SuppressWarnings("serial")
@Service
@Repository
public class SongServiceImpl implements ISongService, Serializable {

	private transient EntityManager em;

	public SongServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Cacheable("allSongsCache")
	public List<Song> findAll() {
		Query query = em.createQuery("SELECT s FROM Song s");
		return query.getResultList();
	}
	
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	public List<Song> findByPattern(String pattern) {
//		pattern = getSearchPattern(pattern);
//		if (pattern != null) {
//			return em.createQuery("select s from Song s ").getResultList();
//
//			// return em
//			// .createQuery(
//			// "select s from Song s where s.noPunctuationText = :pattern order by s.name")
//			// .setParameter("pattern", pattern).getResultList();
//		} else {
//			return null;
//		}
//	}

	@Transactional(readOnly = true)
	public Song findById(Long id) {
		return em.find(Song.class, id);
	}

	@Transactional
	@CacheEvict(value = "allSongsCache", allEntries=true )
	public void persist(Song song) {
		if (null != song.getId()) {
			em.merge(song);
		} else {
			em.persist(song);
		}
	}

	@Transactional
	@Override
	@CacheEvict(value = "allSongsCache", allEntries=true )
	public void delete(Song song) {
		deleteById(song.getId());
	}

	@Transactional
	@Override
	@CacheEvict(value = "allSongsCache", allEntries=true )
	public void deleteById(long songId) {
		Song song = em.find(Song.class, songId);
		if (song != null) {
			em.remove(song);
		}
	}

//	private String getSearchPattern(String pattern) {
//		if (StringUtils.hasText(pattern)) {
//			return "%" + pattern.toLowerCase().replace('*', '%') + "%";
//		} else {
//			return "%";
//		}
//	}

	@Transactional
	@Override
	@CacheEvict(value = "allSongsCache", allEntries=true )
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
	
	@Override
	@Transactional
	@CacheEvict(value = "allSongsCache", allEntries=true )
	public void update(Song song) {
		this.persist(song);
	}
}
