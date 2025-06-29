package com.sodimacapp.demo.dto;

import lombok.Data;

// Un DTO simple para enviar solo la información necesaria del creador del trabajo.
@Data
public class UserSummaryDTO {
    private Integer id;
    private String username;
    private String email;
    private String role;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

    // Getters y Setters...
    
}