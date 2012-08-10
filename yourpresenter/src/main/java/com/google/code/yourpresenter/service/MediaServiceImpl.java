package com.google.code.yourpresenter.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.media.IMediaImporter;

@SuppressWarnings("serial")
@Service
@Repository
@Slf4j
public class MediaServiceImpl implements IMediaService, Serializable {

	@Autowired
	private List<IMediaImporter> mediaImporters;

	@Autowired
	private IBgImageService bgImageService;

	private transient EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional
	@Override
	public void persist(Media media) {
		if (null != media.getId()) {
			em.merge(media);
		} else {
			em.persist(media);
			// make sure identity field is generated prio to relation
			// em.flush();
		}
	}

	@Override
	@Transactional
	public void startImport(List<File> files/*
											 * , IMediaImportProgressListener
											 * listener
											 */) throws YpException {

		// long increment = 100L / files.size();
		// long overallProgress = 0L;

		for (File file : files) {
			// listener.fireImportProgress(file, 0L, 0);

			for (IMediaImporter mediaImporter : mediaImporters) {
				if (mediaImporter.supportsMediaType(file.getAbsolutePath())) {

					Media media = new Media(file.getName(),
							file.getAbsolutePath(),
							mediaImporter.getMediaType(), file.lastModified());

					// TODO will it kill performance/DB?
					if (this.existsByLastModifiedTimeAndOriginal(media)) {
						log.debug("skipping already imported media: {}", file.getAbsolutePath());
						continue;
					}

					// store media in DB
					this.persist(media);
					
					File[] bgFiles = mediaImporter.importMedia(media);

					int position = 0;
					for (File f : bgFiles) {
						BgImage bgImage = new BgImage(f.getAbsolutePath(), media, position++);

						bgImageService.persist(bgImage);
						// generate and store the thumbnail
						bgImageService.handleThumbnail(bgImage);
					}
				}
			}
			// overallProgress += increment;

			// listener.fireImportProgress(file, 100L, overallProgress);
		}

		// listener.fireImportProgress(null, 100L, 100L);
	}

	@Override
	@Transactional(readOnly = true)
	public Media findById(Long id) {
		return em.find(Media.class, id);
	}

	@Transactional(readOnly = true)
	private boolean existsByLastModifiedTimeAndOriginal(Media media) {
		Query query = em
				.createQuery("SELECT m FROM Media m WHERE m.lastModifiedTime = :lastModifiedTime AND m.original = :original");
		query.setParameter("lastModifiedTime", media.getLastModifiedTime());
		query.setParameter("original", media.getOriginal());
		@SuppressWarnings("unchecked")
		List<Media> result = (List<Media>) query.getResultList();
		return (null != result) && (result.size() > 0);
	}

}
