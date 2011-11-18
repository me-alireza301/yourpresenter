package com.google.code.yourpresenter.service;

import java.util.List;

import com.google.code.yourpresenter.entity.BgImage;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.view.IHasSchedule;

public interface IScheduleService {

    public Schedule findByName(String name);

    public void persist(Schedule schedule);

    public void delete(Schedule schedule);
    
    public void addPresentation(Schedule schedule, long presentationId, Song song);
    
    public Schedule loadAllSlidesEager(Schedule schedule);

	public void setBgImage(Schedule schedule, BgImage bgImage);

	public List<Schedule> findAll();
	
	public List<String> findScheduleNamesByName(String name);
	
	public void toggleBlank(Schedule schedule);
	public void toggleClear(Schedule schedule);
	public void toggleLive(Schedule schedule);
}
