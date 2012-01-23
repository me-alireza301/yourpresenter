package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Index;

@SuppressWarnings("serial")
@Entity
public class Preference implements Serializable {
	
	/** The name. */
	private String name;

	/**
	 * The comma separated values.
	 * (can't be called values as it causes problems in JPA table creation)
	 */
	private String value;
	
	private String type;

	public Preference(String name, String value, String type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}

	@Id
	@Index(name="PreferenceNameIdx")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}	
