package com.sodimacapp.demo.dto;

import lombok.Data;

// Este DTO define los datos que el frontend enviará para crear una postulación.
@Data
public class CreateApplicationDTO {
    private String coverLetter;
    private String experience;
    private String skills;
    private String cvFileName; // El nombre del archivo del CV
	public String getCoverLetter() {
		return coverLetter;
	}
	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	public String getCvFileName() {
		return cvFileName;
	}
	public void setCvFileName(String cvFileName) {
		this.cvFileName = cvFileName;
	}

    // Getters y Setters son generados por Lombok o puedes añadirlos manualmente.
    
}
