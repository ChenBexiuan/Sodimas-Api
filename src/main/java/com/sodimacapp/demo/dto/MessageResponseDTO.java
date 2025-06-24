package com.sodimacapp.demo.dto;

import com.sodimacapp.demo.model.ApplicationMessage;
import lombok.Data;
import java.time.LocalDateTime;

// Un DTO simple para representar un mensaje en la respuesta de la API.
@Data
public class MessageResponseDTO {
    private Integer id;
    private String text;
    private String sender;
    private LocalDateTime createdAt;

    public MessageResponseDTO(ApplicationMessage message) {
        this.id = message.getId();
        this.text = message.getText();
        this.sender = message.getSender();
        this.createdAt = message.getCreatedAt();
        
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}