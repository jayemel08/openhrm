package com.dhruvchaudhary.hrm.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class HolidayDTO {
	private int id;
	@NotEmpty
	private String date;
	@NotEmpty
	private String description;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
