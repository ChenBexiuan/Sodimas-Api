package com.sodimacapp.demo.service;

import com.sodimacapp.demo.model.Application;
import com.sodimacapp.demo.model.ApplicationMessage;
import com.sodimacapp.demo.repository.ApplicationMessageRepository;
import com.sodimacapp.demo.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona los mensajes relacionados a las postulaciones.
 */
@Service
public class ApplicationMessageService {

    private final ApplicationMessageRepository messageRepository;
    private final ApplicationRepository applicationRepository;

    public ApplicationMessageService(ApplicationMessageRepository messageRepository, ApplicationRepository applicationRepository) {
        this.messageRepository = messageRepository;
        this.applicationRepository = applicationRepository;
    }

    /**
     * Obtiene todos los mensajes registrados.
     */
    public List<ApplicationMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Obtiene un mensaje específico por su ID.
     */
    public Optional<ApplicationMessage> getMessageById(Integer id) {
        return messageRepository.findById(id);
    }

    /**
     * Crea un nuevo mensaje asociado a una postulación existente.
     * 
     * @param applicationId ID de la postulación
     * @param text Texto del mensaje
     * @param sender Rol o nombre del remitente (por ejemplo: recruiter)
     * @return ApplicationMessage creado o null si la postulación no existe
     */
    public ApplicationMessage createMessage(Integer applicationId, String text, String sender) {
        return applicationRepository.findById(applicationId)
                .map(application -> {
                    ApplicationMessage newMessage = new ApplicationMessage();
                    newMessage.setApplication(application);
                    newMessage.setText(text);
                    newMessage.setSender(sender);
                    return messageRepository.save(newMessage);
                })
                .orElse(null);
    }

    /**
     * Elimina un mensaje por su ID.
     */
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
    }

    /**
     * Obtiene todos los mensajes relacionados a una postulación específica.
     */
    public List<ApplicationMessage> getMessagesByApplicationId(Integer applicationId) {
        return messageRepository.findByApplicationId(applicationId);
    }
}
