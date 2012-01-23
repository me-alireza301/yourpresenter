package com.google.code.yourpresenter.service;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;

public interface IStateService {

	public void stateChanged(String scheduleName) throws YpException;
	
	public StateDTO getState(String scheduleName);
}
