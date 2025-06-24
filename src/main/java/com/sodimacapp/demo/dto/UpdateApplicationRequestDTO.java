package com.sodimacapp.demo.dto;

import lombok.Data;

// Un DTO para recibir tanto el nuevo estado como el mensaje en una sola petición.
@Data
public class UpdateApplicationRequestDTO {
    private String status;
    private String message;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

  
    
}