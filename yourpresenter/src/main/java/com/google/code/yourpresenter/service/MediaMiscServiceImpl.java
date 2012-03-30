package com.google.code.yourpresenter.service;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaMisc;
import com.google.code.yourpresenter.media.IMediaImporter;
import com.google.code.yourpresenter.util.IMediaImportProgressListener;

@SuppressWarnings("serial")
@Service
@Repository
public class MediaMiscServiceImpl implements IMediaMiscService, Serializable {

	@Autowired
	private List<IMediaImporter> mediaImporters;

	private transient EntityManager em;

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	@Transactional
	@Override
	public void persist(MediaMisc mediaMisc) {
		if (null != mediaMisc.getId()) {
			em.merge(mediaMisc);
		} else {
			em.persist(mediaMisc);
			// make sure identity field is generated prio to relation
			// em.flush();
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Collection<MediaMisc> findAll() {
		Query query = em.createQuery("SELECT m FROM MediaMisc m");
		return (Collection<MediaMisc>) query
				.getResultList();
	}

//	@Async
	@Override
	@Transactional
	public void startImport(List<File> files,
			IMediaImportProgressListener listener) throws YpException {

		long increment = 100L / files.size();
		long overallProgress = 0L;

		for (File file : files) {
			listener.fireImportProgress(file, 0L, 0);

			for (IMediaImporter mediaImporter : mediaImporters) {
				if (mediaImporter.supportsMediaType(file.getAbsolutePath())) {
					MediaMisc mediaMisc = new MediaMisc(file.getName(),
							file.getAbsolutePath());
					mediaMisc = mediaImporter.importMedia(mediaMisc,
							file.getParentFile());
					// store imported stuff in DB
					this.persist(mediaMisc);
				}
			}
			overallProgress += increment;

			listener.fireImportProgress(file, 100L, overallProgress);
		}

		listener.fireImportProgress(null, 100L, 100L);
	}

	@Override
	@Transactional(readOnly = true)
	public MediaMisc findById(Long id) {
		return em.find(MediaMisc.class, id);
	}

}
