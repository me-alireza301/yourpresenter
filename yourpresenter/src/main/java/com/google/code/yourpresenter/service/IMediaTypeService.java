package com.google.code.yourpresenter.service;

import java.util.List;

import com.google.code.yourpresenter.entity.MediaType;

public interface IMediaTypeService {

	public List<MediaType> findAll();
	
	public MediaType findByName(String name);
	
	public void persist(MediaType mediaType);
}
