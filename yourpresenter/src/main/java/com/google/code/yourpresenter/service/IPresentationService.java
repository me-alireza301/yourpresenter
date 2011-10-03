package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Presentation;

public interface IPresentationService {

	public Presentation findById(Long id);
	
	public int findPositionById(Long id);
	
    public Presentation createOrEdit(Long id);
    
    public void persist(Presentation presentation);
    
	public void persistSlides(Presentation presentation);

	public void setBgImage(long presentationId, BgImage bgImage);

	public void setBgImage(Presentation presentation, BgImage bgImage);
}
