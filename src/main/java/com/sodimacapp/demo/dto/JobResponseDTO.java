package com.sodimacapp.demo.dto;

import com.sodimacapp.demo.model.Job;
import lombok.Data;
import java.time.LocalDateTime;

// Este DTO representa cómo se enviará un 'Job' al frontend.
// Incluye la información del creador a través de UserSummaryDTO.
@Data
public class JobResponseDTO {
    private Integer id;
    private String title;
    private String department;
    private String location;
    private String type;
    private String salary;
    private String description;
    private String requirements;
    private String status;
    private LocalDateTime createdAt;
    private UserSummaryDTO createdBy;

   
    public JobResponseDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.department = job.getDepartment();
        this.location = job.getLocation();
        this.type = job.getType();
        this.salary = job.getSalary();
        this.description = job.getDescription();
        this.requirements = job.getRequirements();
        this.status = job.getStatus();
        this.createdAt = job.getCreatedAt();

        if (job.getCreatedBy() != null) {
            this.createdBy = new UserSummaryDTO();
            this.createdBy.setId(job.getCreatedBy().getId());
            this.createdBy.setUsername(job.getCreatedBy().getUsername());
            this.createdBy.setEmail(job.getCreatedBy().getEmail());
            this.createdBy.setRole(job.getCreatedBy().getRole());
        }
    }


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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UserSummaryDTO getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummaryDTO createdBy) {
		this.createdBy = createdBy;
	}
    
}