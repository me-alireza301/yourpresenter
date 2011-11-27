package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class Schedule.
 */
@SuppressWarnings("serial")
@Entity
public class Schedule implements Serializable {

	/** The presentations. */
	@JsonIgnore
	private List<Presentation> presentations;

	/** The background. */
	@JsonIgnore
	private BgImage bgImage;

	/** The name. */
	private String name;
	
	private boolean blank;
	private boolean clear;
	private boolean live;

	public Schedule() {
		this(null);
	}
	
	public Schedule(String name) {
		super();
		this.blank = false;
		this.clear = false;
		this.live = false;
		
		this.name = name;	
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Id
	@NotNull
	@Index(name="ScheduleNameIdx")
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the presentations.
	 * 
	 * @return the presentations
	 */
	// if having FetchType.EAGER => got exception: 
	// Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags 
	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderColumn(name = "possition")
	public List<Presentation> getPresentations() {
		return presentations;
	}

	/**
	 * Sets the presentations.
	 * 
	 * @param presentations
	 *            the new presentations
	 */
	public void setPresentations(List<Presentation> presentations) {
		this.presentations = presentations;
	}

	/**
	 * Gets the background.
	 * 
	 * @return the background
	 */
	@OneToOne
	@Index(name="ScheduleBgImageIdx")
	public BgImage getBgImage() {
		return bgImage;
	}

	/**
	 * Sets the background.
	 * 
	 * @param background
	 *            the new background
	 */
	public void setBgImage(BgImage background) {
		this.bgImage = background;
	}
	
	public Presentation addPresentation(Presentation presentation) {
		if (null == this.presentations) {
			this.presentations = new ArrayList<Presentation>();
		}
		
		this.presentations.add(presentation);
		return presentation;
	}
	
	public boolean isBlank() {
		return blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
}
