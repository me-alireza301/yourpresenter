package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

@SuppressWarnings("serial")
@Entity
@ToString (exclude="bgImages")
@EqualsAndHashCode (exclude={"id", "bgImages"})
@NoArgsConstructor
public class Media implements Serializable {

	/** The id. */
	@Id
	@GeneratedValue
	@Index(name = "MediaIdIdx")
	private Long id;

	/** The name. */
	@NotNull
	private String name;

	private String original;

	// keep in mind to delete prphans:
	// http://javablog.co.uk/2009/12/27/onetomany-fixes-in-jpa-2/
//	@OneToMany(mappedBy = "media", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//	@OrderColumn(name = "possition")
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true) 
    @IndexColumn(name="bgimage_position", base=0)
    @JoinColumn(name="media_id", nullable=false)
	private List<BgImage> bgImages;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@Index(name = "MediaMediaTypeIdx")
	private MediaType type;
	
	/** last modified time in miliseconds */
	@JsonIgnore
	private long lastModifiedTime;

	/**
	 * @param name
	 * @param original
	 */
	public Media(String name, String original, MediaType type, long lastModifiedTime) {
		super();
		this.setName(name);
		this.setOriginal(original);
		this.setType(type);
		this.setLastModifiedTime(lastModifiedTime);
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the bgImages
	 */
	public List<BgImage> getBgImages() {
		return bgImages;
	}

	public void setBgImages(List<BgImage> bgImages) {
		this.bgImages = bgImages;
	}
	
	/**
	 * @param bgImages
	 *            the mediaImages to set
	 */
	public void addBgImage(BgImage bgImage) {
		if (null == bgImages) {
			bgImages = new ArrayList<BgImage>();
		}
		this.bgImages.add(bgImage);
	}
	
	/**
	 * @return the original
	 */
	public String getOriginal() {
		return original;
	}

	/**
	 * @param original
	 *            the original to set
	 */
	public void setOriginal(String original) {
		this.original = original;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
}
