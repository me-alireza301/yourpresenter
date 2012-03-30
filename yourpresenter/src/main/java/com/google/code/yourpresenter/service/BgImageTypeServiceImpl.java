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

import com.google.code.yourpresenter.entity.BgImageType;

@SuppressWarnings("serial")
@Service
@Repository
public class BgImageTypeServiceImpl implements IBgImageTypeService,
		Serializable {

	private transient EntityManager em;

	public BgImageTypeServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<BgImageType> findAll() {
		Query query = em.createQuery("SELECT b FROM BgImageType b");
		return query.getResultList();
	}

	@Override
	@Transactional
	public void persist(BgImageType bgImageType) {
		em.persist(bgImageType);
	}

	@Override
	@Transactional(readOnly = true)
	public BgImageType findByName(String name) {
		Query query = em
				.createQuery("SELECT bt FROM BgImageType bt WHERE bt.name = :name)");
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		List<BgImageType> oldSelections = query.getResultList();
		if (!CollectionUtils.isEmpty(oldSelections)) {
			return oldSelections.iterator().next();
		}
		return null;
	}

}
