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
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.google.code.yourpresenter.IConstants;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Preference;
import com.google.code.yourpresenter.service.IPreferenceService;

@Component("preferencesView")
@Scope(WebApplicationContext.SCOPE_SESSION)
@SuppressWarnings("serial")
public class PreferencesView implements Serializable {

	@Autowired
	private IPreferenceService preferenceService;

	private String mediaAcceptedExts;
	private String mediaDirs;

	private boolean saveDisabled = true;


	public String getViewFontMaxsizeProjector() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_FONT_MAXSIZE_PROJECTOR);
	}

	public String getViewFontMaxsizePresenter() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_FONT_MAXSIZE_PRESENTER);
	}

	/**
	 * @return the mediaAcceptedExts
	 * @throws YpException
	 */
	public String getMediaAcceptedExts() throws YpException {
		String[] exts = this.preferenceService
				.findStringArrayById(IConstants.MEDIA_ACCEPTED_EXTS);
		setMediaAcceptedExts(StringUtils.join(exts, ","));
		return mediaAcceptedExts;
	}

	/**
	 * @param mediaAcceptedExts
	 *            the mediaAceptedExts to set
	 */
	public void setMediaAcceptedExts(String mediaAcceptedExts) {
		// // null safe equals
		// if (StringUtils.equals(mediaAcceptedExts, this.mediaAcceptedExts)) {
		// return;
		// }
		//
		// saveDisabled = false;
		this.mediaAcceptedExts = mediaAcceptedExts;
	}

	/**
	 * @return the mediaDirs
	 * @throws YpException
	 */
	public String getMediaDirs() throws YpException {
		String[] dirs = this.preferenceService
				.findStringArrayById(IConstants.MEDIA_DIRS);
		setMediaDirs(StringUtils.join(dirs, ","));
		return mediaDirs;
	}

	/**
	 * @param mediaDirs
	 *            the mediaDirs to set
	 */
	public void setMediaDirs(String mediaDirs) {
		this.mediaDirs = mediaDirs;
	}

	public void save(ActionEvent event) throws YpException {
		saveDisabled = true;
		
		Collection<Preference> preferences = new ArrayList<Preference>();
		preferences.add(new Preference(IConstants.MEDIA_DIRS, mediaDirs));
		preferences.add(new Preference(IConstants.MEDIA_ACCEPTED_EXTS,
				mediaAcceptedExts));
		this.preferenceService.update(preferences);
	}

	public boolean isSaveDisabled() {
		return saveDisabled;
	}

	// parameter is mandatory for listener,
	// see:
	// https://issues.jboss.org/browse/RF-11125?page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel&focusedCommentId=12620545#comment-12620545
	public void valueChanged(AjaxBehaviorEvent event) throws YpException {
		saveDisabled = false;
	}
}
