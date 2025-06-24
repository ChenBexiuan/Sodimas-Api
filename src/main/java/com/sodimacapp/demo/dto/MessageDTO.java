package com.sodimacapp.demo.dto;

import lombok.Data;

// Un DTO para recibir el contenido del mensaje desde el frontend.
@Data
public class MessageDTO {
    private String text;
    private String sender; 
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

   
    
}