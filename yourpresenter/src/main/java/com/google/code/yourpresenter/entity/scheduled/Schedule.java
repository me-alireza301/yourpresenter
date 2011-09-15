package com.google.code.yourpresenter.entity.scheduled;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

// TODO: Auto-generated Javadoc
/**
 * The Class Schedule.
 */
@SuppressWarnings("serial")
@Entity
public class Schedule implements Serializable {

	/** The id. */
	private Long id;

	/** The presentations. */
	private List<Presentation> presentations;

	/** The background. */
	private Background background;

	/** The name. */
	private String name;

	public Schedule() {
		super();
		this.name = "New Schedule"; 
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@NotNull
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the presentations.
	 * 
	 * @return the presentations
	 */
	// if having FetchType.EAGER => got exception: 
	// Caused by: org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags 
	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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
	public Background getBackground() {
		return background;
	}

	/**
	 * Sets the background.
	 * 
	 * @param background
	 *            the new background
	 */
	public void setBackground(Background background) {
		this.background = background;
	}
	
	public Presentation addPresentation(Presentation presentation) {
		if (null == this.presentations) {
			this.presentations = new ArrayList<Presentation>();
		}
		
		this.presentations.add(presentation);
		return presentation;
	}
}
