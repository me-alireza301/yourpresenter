package com.google.code.yourpresenter.ajaxpush;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.dto.StateDTO;

public interface IStateChangedPropagator {

	public void stateChanged(Long scheduleId, StateDTO stateDTO) throws YpException;
}
