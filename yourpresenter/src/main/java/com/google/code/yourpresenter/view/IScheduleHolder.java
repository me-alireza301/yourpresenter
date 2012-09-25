package com.google.code.yourpresenter.view;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Schedule;

public interface IScheduleHolder {

	public void setSchedule(Schedule schedule) throws YpException;
	
	public Schedule getSchedule();
}
