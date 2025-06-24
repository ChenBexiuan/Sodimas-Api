package com.sodimacapp.demo.dto;

import com.sodimacapp.demo.model.Application;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Este DTO define la estructura exacta de una postulación que se enviará al frontend.
@Data
public class ApplicationResponseDTO {
    private Integer id;
    private String status;
    private String coverLetter;
    private String experience;
    private String skills;
    private String cvFileName;
    private LocalDateTime appliedAt;
    private UserSummaryDTO candidate;
    private JobSummaryDTO job;
    private List<MessageResponseDTO> messages; // Campo añadido para los mensajes

    // Constructor que mapea la entidad Application completa a este DTO.
    public ApplicationResponseDTO(Application application) {
        this.id = application.getId();
        this.status = application.getStatus();
        this.coverLetter = application.getCoverLetter();
        this.experience = application.getExperience();
        this.skills = application.getSkills();
        this.cvFileName = application.getCvFileName();
        this.appliedAt = application.getAppliedAt();

        if (application.getCandidate() != null) {
            this.candidate = new UserSummaryDTO();
            this.candidate.setId(application.getCandidate().getId());
            this.candidate.setUsername(application.getCandidate().getUsername());
            this.candidate.setEmail(application.getCandidate().getEmail());
            this.candidate.setRole(application.getCandidate().getRole());
        }

        if (application.getJob() != null) {
            this.job = new JobSummaryDTO();
            this.job.setId(application.getJob().getId());
            this.job.setTitle(application.getJob().getTitle());
            this.job.setDepartment(application.getJob().getDepartment());
            this.job.setLocation(application.getJob().getLocation());
            this.job.setType(application.getJob().getType());
        }
        
        // --- LÓGICA AÑADIDA PARA MAPEAR LOS MENSAJES ---
        if (application.getMessages() != null) {
            this.messages = application.getMessages().stream()
                    .map(MessageResponseDTO::new) // Mapea cada ApplicationMessage a su DTO
                    .collect(Collectors.toList());
        }
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public LocalDateTime getAppliedAt() {
		return appliedAt;
	}

	public void setAppliedAt(LocalDateTime appliedAt) {
		this.appliedAt = appliedAt;
	}

	public UserSummaryDTO getCandidate() {
		return candidate;
	}

	public void setCandidate(UserSummaryDTO candidate) {
		this.candidate = candidate;
	}

	public JobSummaryDTO getJob() {
		return job;
	}

	public void setJob(JobSummaryDTO job) {
		this.job = job;
	}

	public List<MessageResponseDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<MessageResponseDTO> messages) {
		this.messages = messages;
	}

    
}
