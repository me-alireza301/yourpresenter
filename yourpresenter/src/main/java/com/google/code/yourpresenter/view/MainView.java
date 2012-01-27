package com.google.code.yourpresenter.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.YpError;
import com.google.code.yourpresenter.YpException;
import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.service.IScheduleService;

@Component("mainView")
@Scope("session")
@SuppressWarnings("serial")
public class MainView implements Serializable {

//	private UIComponent submitButton;

	@Autowired
	private IScheduleService scheduleService;

	@Autowired
	private ScheduleView scheduleView;

	@Size(min = 2, max = 30)
	private String scheduleName;

	private Role role;

	private boolean scheduleChoose = false;
	private boolean scheduleCreate = false;

	private static Map<String, Role> roles;

	static {
		roles = new LinkedHashMap<String, Role>();
		for (Role role : Role.values()) {
			roles.put(role.getTxt(), role); // label, value
		}
	}

	public List<String> filterScheduleNames(String name) {
		return scheduleService.findScheduleNamesByName(name);
	}

	public Map<String, Role> getRoles() {
		return roles;
	}

	public String submitSchedule() throws YpException {
		switch (getRole()) {
		case PROJECTOR:
		case SPEAKER:
		case MUSICIAN:
		case ADMIN:
			break;
		case PRESENTER:
			createOrChooseSchedule();
			break;
		default:
			throw new YpException(YpError.ROLE_NOT_SUPPORTED);
		}

		return getRole().getUrl();
	}

	public void createOrChooseSchedule() throws YpException {
		Schedule schedule = scheduleService.findByName(this.scheduleName);

		// if new schedule to be created
		if (null == schedule) {
			schedule = new Schedule(this.scheduleName);
			scheduleService.persist(schedule);
		}
		this.scheduleView.setSchedule(schedule);
	}

	// parameter is mandatory for listener,
	// see:
	// https://issues.jboss.org/browse/RF-11125?page=com.atlassian.jira.plugin.system.issuetabpanels:comment-tabpanel&focusedCommentId=12620545#comment-12620545
	public void roleChanged(AjaxBehaviorEvent event) throws YpException {
		switch (getRole()) {
		case PRESENTER:
			this.scheduleCreate = true;
			this.scheduleChoose = false;
			break;
		case PROJECTOR:
		case SPEAKER:
		case MUSICIAN:
			this.scheduleCreate = false;
			this.scheduleChoose = true;
			break;
		case ADMIN:
			this.scheduleCreate = false;
			this.scheduleChoose = false;
			break;
		default:
			throw new YpException(YpError.ROLE_NOT_SUPPORTED);
		}
	}

//	public void validateSchedule(ActionEvent event) {
//		Schedule schedule = scheduleService.findByName(this.scheduleName);
//
//		// if new schedule to be created => error
//		if (null == schedule) {
//			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
//					"Choose existing schedule (New one can be created by presenter only!)",
//					null));
//		}
//	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

//	public UIComponent getSubmitButton() {
//		return submitButton;
//	}
//
//	public void setSubmitButton(UIComponent submitButton) {
//		this.submitButton = submitButton;
//	}

	/**
	 * @return the scheduleCreate
	 */
	public boolean isScheduleCreate() {
		return scheduleCreate;
	}

	/**
	 * @return the scheduleChoose
	 */
	public boolean isScheduleChoose() {
		return scheduleChoose;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return the allScheduleNames
	 */
	public Map<String, String> getAllScheduleNames() {
		Map<String, String> map = new HashMap<String, String>();
		List<String> schedules = scheduleService.findScheduleNamesByName(null);
		for (String schedule : schedules) {
			map.put(schedule, schedule);
		}
		return map;
	}

	/**
	 * @return true if role has NOT been chosen, otherwise returns false.
	 */
	public boolean isRoleNotChosen() {
		return null == role;
	}
}
