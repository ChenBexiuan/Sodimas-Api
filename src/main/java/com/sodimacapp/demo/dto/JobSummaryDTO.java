package com.sodimacapp.demo.dto;

import lombok.Data;
// Este DTO representa un resumen del trabajo para ser anidado en otras respuestas.
@Data
public class JobSummaryDTO {
    private Integer id;
    private String title;
    private String department;
    private String location;
    private String type;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
    
	
}