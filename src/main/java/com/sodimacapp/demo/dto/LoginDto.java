package com.sodimacapp.demo.dto;

import lombok.Data;
/**
 * DTO usado para autenticación de usuarios.
 */
@Data
public class LoginDto {
    private String email;
    private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
}