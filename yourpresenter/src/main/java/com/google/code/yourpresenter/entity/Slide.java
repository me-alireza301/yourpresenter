package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	@Id
	@Index(name = "SlideIdIdx")
	@GeneratedValue
	private Long id;

	/** The background. */
	@OneToOne
	@Index(name = "SlideBgImageIdx")
	private BgImage bgImage;

	@Column(columnDefinition = "VARCHAR(1000)")
	@Size(max = 1000)
	@NotNull
	private String text;

	/** The presentation. */
	@JsonIgnore
//	@ManyToOne(optional = false)
//	@Index(name = "SlidePresentationIdx")
//	@ManyToOne(optional = false)
//	@Index(name = "SlidePresentationIdx")
//	@JoinColumn(name="PRESENTATION_ID", nullable = false, updatable = false, insertable = false)
//	@JoinColumn(name="PRESENTATION_ID")
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "presentation_id", insertable = false, updatable = false, nullable = true, unique = false)
	private Presentation presentation;

	/**
	 * Whether slide is the active one.
	 */
	@JsonIgnore
	private boolean active;

	public Slide(String text, Presentation presentation/*, int possition*/) {
		this.setText(text);
		this.setPresentation(presentation);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
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
