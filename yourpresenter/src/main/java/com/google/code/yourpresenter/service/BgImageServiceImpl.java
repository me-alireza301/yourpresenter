package com.google.code.yourpresenter.service;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.media.ThumbnailCreator;
import com.google.code.yourpresenter.util.FileUtil;

@SuppressWarnings("serial")
@Service
@Repository
public class BgImageServiceImpl implements IBgImageService, Serializable {

	private transient EntityManager em;

	@Autowired
	private IPreferenceService preferenceService;

	public BgImageServiceImpl() {
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true)
//	public List<BgImage> findAll() {
//		Query query = em.createQuery("SELECT b FROM BgImage b");
//		return (List<BgImage>) query.getResultList();
//	}

	@Transactional
	@CacheEvict (value = "bgImageFirstPositionCache", key = "#root.args[0].media.type.name")
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

//	@SuppressWarnings("unchecked")
//	@Override
//	@Transactional(readOnly = true)
//	public Collection<BgImage> findAllByType(String type) {
//		Query query = em
//				.createQuery("SELECT b FROM BgImage b WHERE b.media IN (SELECT m FROM Media m WHERE m.type = (SELECT mt FROM MediaType mt WHERE mt.name = :type))");
//		query.setParameter("type", type);
//		return (List<BgImage>) query.getResultList();
//	}
	
	@Transactional
	@Override
	public BgImage handleThumbnail(BgImage bgImage) throws YpException {
		String thumbDir = this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_DIR);
		thumbDir = FileUtil.replaceDirs(thumbDir);
		File dir = new File(thumbDir);

		// init thumbnail dir
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final String thumbExt = this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_EXT);

		final int thumbWidth = Integer.valueOf(this.preferenceService
				.findStringById(IConstants.MEDIA_THUMBNAIL_WIDTH));

		if (null == bgImage.getId()) {
			throw new YpException(YpError.THUMBNAIL_CREATION_FAILED,
					"BgImage has to be persisted prio to generating the thumbnail for it.");
		}

		final String thumbnailName = new StringBuilder(Long.toString(bgImage
				.getId())).append(".").append(thumbExt).toString();
		final File thumbnail = new File(thumbDir, thumbnailName);

		ThumbnailCreator.generateThumbnail(bgImage.getImage(), thumbnail,
				thumbWidth);

		// TODO change so that it can be served as static content possibly
		bgImage.setThumbnail(thumbnail.getAbsolutePath());
		persist(bgImage);

		return bgImage;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	@Cacheable( value = "bgImageFirstPositionCache" )
	public Collection<BgImage> findFirstBgImageByType(String type) {
		Query query = em
				.createQuery("SELECT bg FROM BgImage bg WHERE bg.media IN (SELECT m FROM Media m WHERE m.type = (SELECT mt FROM MediaType mt WHERE mt.name = :type)) AND bg.possition = 0");
		query.setParameter("type", type);
		return (List<BgImage>) query.getResultList();
	}
}
