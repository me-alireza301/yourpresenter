package com.google.code.yourpresenter.service;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@Service
@Repository
public class VerseServiceImpl implements IVerseService, Serializable {

	private transient EntityManager em;
	
	public VerseServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Transactional
	@Override
	public int deleteAll() {
		Query query = em.createQuery("DELETE FROM Verse v");
		return query.executeUpdate();
	}
	
	@Transactional( propagation=Propagation.REQUIRED )
	@Override
	public int deleteBySongId(long songId) {
		Query query = em.createQuery("delete from Verse v where v.song IN (SELECT s FROM Song s where s.id like :id)");
		query.setParameter("id", songId);
		return query.executeUpdate();
	}
	
}
