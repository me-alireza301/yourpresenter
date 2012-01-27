package com.google.code.yourpresenter.util;

import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;

public interface IPreferenceChangedListener {

	public void preferenceChanged(Preference preference) throws YpException;
}
