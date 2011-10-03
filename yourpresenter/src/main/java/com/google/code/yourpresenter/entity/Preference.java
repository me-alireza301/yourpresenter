package com.google.code.yourpresenter.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

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

	public Preference(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Id
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

}	