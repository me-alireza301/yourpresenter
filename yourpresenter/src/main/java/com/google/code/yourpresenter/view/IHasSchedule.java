package com.google.code.yourpresenter.view;

import java.io.IOException;

import com.google.code.yourpresenter.entity.Schedule;

public interface IHasSchedule {

	public Schedule getSchedule() throws IOException;
	public void setSchedule(Schedule schedule);

}
