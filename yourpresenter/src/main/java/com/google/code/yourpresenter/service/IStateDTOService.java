package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.view.IScheduleHolder;

public interface IStateDTOService {

	public void stateChanged(Schedule schedule, IScheduleHolder scheduleHolder) throws YpException;
	public StateDTO findByScheduleId(Long scheduleId);
}
