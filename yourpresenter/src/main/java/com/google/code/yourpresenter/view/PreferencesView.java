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

	private int width;

	@Autowired
	private IPreferenceService preferenceService;

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

	public String getViewNotifyNonblocking() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_NONBLOCKING);
	}

	public String getViewNotifyNonblockingOpacity() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_NONBLOCKINGOPACITY);
	}

	public String getViewNotifyShowShadow() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_SHOWSHADOW);
	}

	public String getViewNotifyShowCloseButton() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_SHOWCLOSEBUTTON);
	}

	public String getViewNotifyStayTime() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_STAYTIME);
	}

	public String getViewNotifySticky() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_NOTIFY_STICKY);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getViewSlideHeight() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_SLIDE_HEIGHT);
	}

	public String getViewSlideWidth() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_SLIDE_WIDTH);
	}

	public String getViewMediaHeight() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_MEDIA_HEIGHT);
	}

	public String getViewMediaWidth() throws YpException {
		return this.preferenceService
				.findStringById(IConstants.VIEW_MEDIA_WIDTH);
	}

	public int getSlideColumnCnt() throws YpException {
		return (this.width - 230)
				/ (Integer.parseInt(getViewSlideWidth()) + 10);
	}

	public int getMediaColumnCnt() throws YpException {
		return (this.width - 20) / (Integer.parseInt(getViewMediaWidth()) + 10);
	}
}
