package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.entity.Song;
import com.google.code.yourpresenter.entity.scheduled.Schedule;
import com.google.code.yourpresenter.view.IHasSchedule;

public interface IScheduleService {

    public Schedule findScheduleById(Long id);

    public void persistSchedule(Schedule schedule);

    public void deleteSchedule(Schedule schedule);
    
    public Schedule createOrEditSchedule(Long id);
    
    public Schedule createOrEditSchedule(Schedule schedule);
    
    public void addPresentation(IHasSchedule callback, Schedule schedule, Song song);
    
    public Schedule loadAllSlidesEager(Schedule schedule);
}
