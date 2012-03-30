package com.google.code.yourpresenter.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.MediaMisc;
import com.google.code.yourpresenter.util.IMediaImportProgressListener;

public interface IMediaMiscService {

	public void persist(MediaMisc mediaMisc);
	
	public Collection<MediaMisc> findAll();
	
	public void startImport(List<File> files, IMediaImportProgressListener listener) throws YpException;

	public MediaMisc findById(Long id);
	
}
