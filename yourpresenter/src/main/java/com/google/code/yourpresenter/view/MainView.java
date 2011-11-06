package com.google.code.yourpresenter.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.code.yourpresenter.entity.Schedule;
import com.google.code.yourpresenter.service.IScheduleService;

@Component("mainView")
@Scope("session")
@SuppressWarnings("serial")
public class MainView implements Serializable {

	private UIComponent submitButton;
	
	@Autowired
	private IScheduleService scheduleService;

	@Autowired
	private ScheduleView scheduleView;

	@Size(min=2, max=30)
	private String scheduleName;

	public List<String> allScheduleNames(String name) {
		List<String> persistedSchedules = scheduleService.findScheduleNamesByName(name);
		return persistedSchedules;
	}
	
//	public List<String> getAllScheduleNames() {
//		List<String> schedules = new ArrayList<String>();
//
//		schedules.add("schedule new 1");
//		schedules.add("schedule new 2");
//		schedules.add("schedule new 3");
//		schedules.add("schedule new 4");
//		schedules.add("schedule new 5");
//		
//		List<String> persistedSchedules = scheduleService
//				.findAllScheduleNames();
//		return persistedSchedules;
//		if (null != persistedSchedules) {
//			if (!persistedSchedules.contains(Schedule.EMPTY)) {
//				schedules.add(Schedule.EMPTY);
//			}
//			schedules.addAll(persistedSchedules);
//		}
//
//		return schedules;
		// to prevent 'null converter' problem,
		// solution found on:
		// http://balusc.blogspot.com/2007/09/objects-in-hselectonemenu.html
		// fill data for combo/list box
		// List<SelectItem> selectItems = new ArrayList<SelectItem>();
		// for (ScheduleNameDTO schedule : schedules) {
		// selectItems.add(new SelectItem(schedule, schedule.getName()));
		// }
		// return selectItems;
//	}

	public String submitSchedule() throws IOException {
		Schedule schedule = scheduleService.findByName(this.scheduleName);
		
		// if new schedule to be created
		if (null == schedule) {
			schedule = new Schedule(this.scheduleName);
			scheduleService.persist(schedule);
		}
		this.scheduleView.setSchedule(schedule);
		
		// redirect to the next page via outcome param value
		@SuppressWarnings("rawtypes")
		Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		return (String) map.get("outcome");
	}

	public String chooseSchedule() throws IOException {
		Schedule schedule = scheduleService.findByName(this.scheduleName);
		
		// if new schedule to be created => error
		if (null == schedule) {
			// add validation error message
			// see: http://stackoverflow.com/questions/315804/how-to-display-my-applications-errors-in-jsf
			// and: http://stackoverflow.com/questions/1140426/add-message-from-methode-in-jsf
			final FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(this.submitButton.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, "New schedule can be created by presenter only!", null));
		    return null;
		} else {
			this.scheduleView.setSchedule(schedule);
		
			// redirect to the next page via outcome param value
			@SuppressWarnings("rawtypes")
			Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			return (String) map.get("outcome");
		}
	}
	
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public UIComponent getSubmitButton() {
		return submitButton;
	}

	public void setSubmitButton(UIComponent submitButton) {
		this.submitButton = submitButton;
	}
}
