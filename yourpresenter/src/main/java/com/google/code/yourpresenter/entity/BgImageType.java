package com.google.code.yourpresenter.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
public class BgImageType implements Serializable {

	/** The id. */
	private Long id;

	private String name;

	@JsonIgnore
	private List<BgImage> bgImages;

	/**
	 * @param id
	 * @param name
	 */
	public BgImageType(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public BgImageType() {
	}

	@Id
	@Index(name = "BgImageTypeIdIdx")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	public List<BgImage> getBgImages() {
		return bgImages;
	}

	public void setBgImages(List<BgImage> bgImages) {
		this.bgImages = bgImages;
	}
}
