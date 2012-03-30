package com.google.code.yourpresenter.service;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.util.IPreferenceChangedListener;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.googlecode.ehcache.annotations.Property;
import com.googlecode.ehcache.annotations.TriggersRemove;

@SuppressWarnings("serial")
@Service
@Repository
public class PreferenceServiceImpl implements IPreferenceService, Serializable {

	private Multimap<String, IPreferenceChangedListener> listeners = HashMultimap
			.create();

	private transient EntityManager em;

	public PreferenceServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional(readOnly = true)
	@Cacheable(cacheName = "preferenceCache", keyGenerator = @KeyGenerator(name = "HashCodeCacheKeyGenerator", properties = @Property(name = "includeMethod", value = "false")))
	@Override
	public String findStringById(String name) throws YpException {
		Query query = em
				.createQuery("SELECT p.value FROM Preference p WHERE p.name = :name");
		query.setParameter("name", name);
		@SuppressWarnings("unchecked")
		Collection<String> values = (Collection<String>) query.getResultList();
		if (CollectionUtils.isEmpty(values)) {
			throw new YpException(YpError.PREFERENCE_NOT_SET, name);
		}

		return values.iterator().next();
	}

	@Transactional(readOnly = true)
	@Cacheable(cacheName = "preferenceCache", keyGenerator = @KeyGenerator(name = "HashCodeCacheKeyGenerator", properties = @Property(name = "includeMethod", value = "false")))
	@Override
	public String[] findStringArrayById(String name) throws YpException {
		return findStringById(name).split(",");
	}

	// make sure that cache is updated (old element removed)
	// for details, see:
	// http://code.google.com/p/ehcache-spring-annotations/wiki/UsingTriggersRemove
	@TriggersRemove(cacheName = "preferenceCache", keyGenerator = @KeyGenerator(name = "HashCodeCacheKeyGenerator", properties = @Property(name = "includeMethod", value = "false")))
	public void persist(Preference preference) {
		em.persist(preference);
	}

	@Transactional
	@Override
	@TriggersRemove(cacheName = "preferenceCache", removeAll = true)
	public void update(Collection<Preference> preferences) throws YpException {
		for (Preference preference : preferences) {
			em.merge(preference);
			firePreferenceChangedListeners(preference);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Collection<Preference> findAll() throws YpException {
		Query query = em.createQuery("SELECT p FROM Preference p");
		@SuppressWarnings("unchecked")
		Collection<Preference> preferences = (Collection<Preference>) query
				.getResultList();
		if (CollectionUtils.isEmpty(preferences)) {
			throw new YpException(YpError.PREFERENCES_NOT_SET);
		}

		return preferences;
	}

	/**
	 * Notifies all the listeners registered for preference change.
	 * 
	 * @param preference
	 * @throws YpException
	 */
	private void firePreferenceChangedListeners(Preference preference)
			throws YpException {
		Collection<IPreferenceChangedListener> toNotify = listeners
				.get(preference.getName());
		for (IPreferenceChangedListener listener : toNotify) {
			listener.preferenceChanged(preference);
		}
	}

	@Override
	public void unregisterPreferenceChangedListener(
			IPreferenceChangedListener listener) throws YpException {
		// TODO implement
		throw new YpException(YpError.METHOD_NOT_IMPLEMENTED);
	}

	@Override
	public void registerPreferenceChangedListener(
			IPreferenceChangedListener listener, String... preferences)
			throws YpException {
		if (null == preferences) {
			throw new YpException(YpError.PREFERENCE_NOT_SPECIFIED);
		}
		
		for (String preference : preferences) {
			listeners.put(preference, listener);
		}
	}
}
