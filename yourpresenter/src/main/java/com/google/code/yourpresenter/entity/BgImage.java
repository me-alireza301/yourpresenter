package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * The Class BgImage.
 */
@SuppressWarnings("serial")
@Entity
public class BgImage implements Serializable {

	/** The id. */
	private Long id;

	/** The image. */
	private String image;

	/** The thumbnail. */
//	@JsonIgnore
	private String thumbnail;

	/** last modified time in miliseconds */
	private long lastModifiedTime;

	public BgImage() {
	}
			
	public BgImage(String image, long lastModifiedTime) {
		this.image = image;
		this.lastModifiedTime = lastModifiedTime;
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
	
}
