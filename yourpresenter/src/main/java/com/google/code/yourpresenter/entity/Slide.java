package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class Slide.
 */
@SuppressWarnings("serial")
@Entity
@ToString(exclude = "presentation")
@EqualsAndHashCode(exclude = { "id", "presentation" })
@NoArgsConstructor
public class Slide implements Serializable {

	/** The id. */
	private Long id;

	/** The background. */
	private BgImage bgImage;

	private String text;

	/** The presentation. */
	@JsonIgnore
	private Presentation presentation;

	@JsonIgnore
	// in case of correct one: position => java.sql.SQLException: Table not
	// found in statement [select schedule0_.name ...
	// seems like position is reserved word => can't be used
	// see:
	// http://stackoverflow.com/questions/1442127/table-not-found-with-hibernate-and-hsqldb
	private int possition;

	/**
	 * Whether slide is the active one.
	 */
	@JsonIgnore
	private boolean active;

	public Slide(String text, Presentation presentation, int possition) {
		this.setText(text);
		this.setPresentation(presentation);
		this.setPossition(possition);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@Index(name = "SlideIdIdx")
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
	 * Gets the background.
	 * 
	 * @return the background
	 */
	@OneToOne
	@Index(name = "SlideBgImageIdx")
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

	/**
	 * Gets the presentation.
	 * 
	 * @return the presentation
	 */
	@ManyToOne(optional = false)
	@Index(name = "SlidePresentationIdx")
	public Presentation getPresentation() {
		return presentation;
	}

	/**
	 * Sets the presentation.
	 * 
	 * @param presentation
	 *            the new presentation
	 */
	public void setPresentation(Presentation presentation) {
		this.presentation = presentation;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean isActive) {
		this.active = isActive;
	}

	public void activate() {
		this.setActive(true);
	}

	@Transient
	@JsonIgnore
	public String getCssSuffix() {
		return (active ? "active" : "inactive");
	}

	/**
	 * @return the possition
	 */
	public int getPossition() {
		return possition;
	}

	/**
	 * @param possition
	 *            the possition to set
	 */
	public void setPossition(int possition) {
		this.possition = possition;
	}

	@Column(columnDefinition = "VARCHAR(1000)")
	@Size(max = 1000)
	@NotNull
	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (null == text) {
			text = "";
		}
		this.text = text;
	}
}
