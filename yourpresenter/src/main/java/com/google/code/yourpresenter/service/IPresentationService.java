package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.entity.scheduled.Presentation;

public interface IPresentationService {

	public Presentation findById(Long id);
	
    public Presentation createOrEdit(Long id);
    
    public void persist(Presentation presentation);
}
