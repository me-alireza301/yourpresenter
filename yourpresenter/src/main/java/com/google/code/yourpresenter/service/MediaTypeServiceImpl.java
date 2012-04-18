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

import com.google.code.yourpresenter.entity.MediaType;

@SuppressWarnings("serial")
@Service
@Repository
public class MediaTypeServiceImpl implements IMediaTypeService,
		Serializable {

	private transient EntityManager em;

	public MediaTypeServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<MediaType> findAll() {
		Query query = em.createQuery("SELECT mt FROM MediaType mt");
		return query.getResultList();
	}

	@Override
	@Transactional
	public void persist(MediaType mediaType) {
		em.persist(mediaType);
	}

	@Override
	@Transactional(readOnly = true)
	public MediaType findByName(String name) {
		Query query = em
				.createQuery("SELECT mt FROM MediaType mt WHERE mt.name = :name)");
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<MediaType> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			return oldSelections.iterator().next();
		}
		return null;
	}

}
