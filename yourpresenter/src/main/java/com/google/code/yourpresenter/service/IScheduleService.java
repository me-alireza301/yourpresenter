package com.google.code.yourpresenter.service;

import java.util.List;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Media;
import com.google.code.yourpresenter.entity.Presentation;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;

public interface IScheduleService {

	public Schedule findById(Long id);
	
	public Schedule findByName(String name);

    public Schedule persist(Schedule schedule);

    public void delete(Schedule schedule);
    
    public void addPresentation(Schedule schedule, long presentationId, Song song, Media mediaMisc) throws YpException;

    public void movePresentation(Schedule schedule, long presentationId,
			Presentation presentation) throws YpException;
    
	public void setBgImage(Schedule schedule, BgImage bgImage);

	public List<Schedule> findAll();
	
	public List<String> findScheduleNamesByName(String name);
	
	public void toggleBlank(Schedule schedule);
	public void toggleClear(Schedule schedule);
	public void toggleLive(Schedule schedule);

	public int deleteAll();

	public void deletePresentation(Schedule schedule, long presentationId) throws YpException;

}
