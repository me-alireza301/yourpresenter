package com.google.code.yourpresenter.service;

import java.io.File;
import java.util.List;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Media;

public interface IMediaService {

	public void persist(Media mediaMisc);
	
	public Media findById(Long id);

	public void startImport(List<File> files) throws YpException;

}
