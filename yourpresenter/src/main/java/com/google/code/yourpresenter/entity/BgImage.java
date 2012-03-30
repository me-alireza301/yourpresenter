package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class BgImage.
 */
@SuppressWarnings("serial")
@Entity
public class BgImage implements Serializable {

	/** The id. */
	private Long id;

	/** The image. */
	@JsonIgnore
	private String image;

	/** The thumbnail. */
	@JsonIgnore
	private String thumbnail;

	/** last modified time in miliseconds */
	@JsonIgnore
	private long lastModifiedTime;

	@JsonIgnore
	private boolean replaceable;

	private BgImageType type;

	public BgImage() {
	}

	public BgImage(String image, long lastModifiedTime, boolean replaceable,
			BgImageType type) {
		this.image = image;
		this.setLastModifiedTime(lastModifiedTime);
		this.setReplaceable(replaceable);
		this.setType(type);
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue
	@Index(name = "BgImageIdIdx")
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
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 * 
	 * @param image
	 *            the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the thumbnail.
	 * 
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Sets the thumbnail.
	 * 
	 * @param thumbnail
	 *            the new thumbnail
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	@Override
	public int hashCode() {
		// for updates find, these are the id's to be compared
		return new HashCodeBuilder().append(this.image)
				.append(this.lastModifiedTime).toHashCode();
	}

	/**
	 * @return the replaceable
	 */
	public boolean isReplaceable() {
		return replaceable;
	}

	/**
	 * @param replaceable
	 *            the replaceable to set
	 */
	public void setReplaceable(boolean replaceable) {
		this.replaceable = replaceable;
	}

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@Index(name = "BgImageBgImageTypeIdx")
	public BgImageType getType() {
		return type;
	}

	public void setType(BgImageType type) {
		this.type = type;
	}

}
