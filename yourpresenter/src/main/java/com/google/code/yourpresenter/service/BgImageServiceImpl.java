package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.entity.BgImage;

@SuppressWarnings("serial")
@Service("bgImageService")
@Repository
public class BgImageServiceImpl implements IBgImageService, Serializable {

	private transient EntityManager em;
	
	public BgImageServiceImpl() {
	}
	
	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Collection<BgImage> findAll() {
		Query query = em.createQuery("SELECT b FROM BgImage b");
	    return (Collection<BgImage>) query.getResultList();
	}
	
	public void persist(BgImage bgImage) {
		if (null != bgImage.getId()) {
			em.merge(bgImage);
		} else {
			em.persist(bgImage);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public BgImage findById(Long id) {
		return em.find(BgImage.class, id);
	}
}
