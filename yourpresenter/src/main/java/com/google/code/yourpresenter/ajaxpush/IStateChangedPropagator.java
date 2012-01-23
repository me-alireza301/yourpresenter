package com.google.code.yourpresenter.ajaxpush;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;

public interface IStateChangedPropagator {

	public void stateChanged(String scheduleName, StateDTO stateDTO) throws YpException;
}
