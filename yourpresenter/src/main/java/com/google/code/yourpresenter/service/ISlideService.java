package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.scheduled.Slide;

public interface ISlideService {

	public Slide findById(Long id);
    
    public void persist(Slide slide);
    
    public void activateSlide(Long id) throws YpException;
}
