package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Slide;

public interface ISlideService {

	public Slide findById(Long id);
    
    public void persist(Slide slide);
    
    public void activateSlide(Long id) throws YpException;

	public void setBgImage(Long slideId, BgImage bgImage);
	
	public Slide findActiveSlide(String scheduleName);

	public int deleteAll();
}
