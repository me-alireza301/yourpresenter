/*
 * Copyright 2009 Prime Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.service.IPreferenceService;

@Component("preferencesView")
@Scope("session")
@SuppressWarnings("serial")
public class PreferencesView implements Serializable {

	/** The theme. */
	private String theme = "aristo"; //default
	
	@Autowired
	private IPreferenceService preferenceService;
	
	private Collection<Preference> allPreferences;

	/**
	 * Gets the theme.
	 * 
	 * @return the theme
	 */
	public String getTheme() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.containsKey("theme")) {
			theme = params.get("theme");
		}
		return theme;
	}

	/**
	 * Sets the theme.
	 * 
	 * @param theme
	 *            the new theme
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}
	
	public String getViewFontMaxsizeProjector() throws YpException {
		return this.preferenceService.findStringById(IConstants.VIEW_FONT_MAXSIZE_PROJECTOR);
	}
	
	public String getViewFontMaxsizePresenter() throws YpException {
		return this.preferenceService.findStringById(IConstants.VIEW_FONT_MAXSIZE_PRESENTER);
	}

	public Collection<Preference> getAllPreferences() throws YpException {
		return preferenceService.findAll();
	}
}
