package com.sodimacapp.demo.controller;

import com.sodimacapp.demo.dto.MessageDTO;
import com.sodimacapp.demo.model.ApplicationMessage;
import com.sodimacapp.demo.service.ApplicationMessageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controlador REST para manejar mensajes asociados a postulaciones
@RequestMapping("/api/messages") // Ruta base para los endpoints de mensajes
public class ApplicationMessageController {

    private final ApplicationMessageService messageService;

    public ApplicationMessageController(ApplicationMessageService messageService) {
        this.messageService = messageService;
    }

    // Obtener todos los mensajes del sistema
    @GetMapping
    public List<ApplicationMessage> getAllMessages() {
        return messageService.getAllMessages();
    }

    // Obtener un mensaje por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationMessage> getMessageById(@PathVariable Integer id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo mensaje asociado a una postulación
    @PostMapping("/{applicationId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')") // Solo usuarios con rol autorizado pueden enviar mensajes
    public ResponseEntity<ApplicationMessage> createMessage(@PathVariable Integer applicationId,
                                                            @RequestBody MessageDTO messageDTO) {
        String sender = (messageDTO.getSender() != null) ? messageDTO.getSender() : "recruiter";
        ApplicationMessage newMessage = messageService.createMessage(applicationId, messageDTO.getText(), sender);

        return (newMessage != null)
                ? new ResponseEntity<>(newMessage, HttpStatus.CREATED)
                : ResponseEntity.badRequest().build();
    }

    // Eliminar un mensaje por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los mensajes de una aplicación específica
    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')") // Acceso restringido a RRHH o Managers
    public List<ApplicationMessage> getMessagesByApplicationId(@PathVariable Integer applicationId) {
        return messageService.getMessagesByApplicationId(applicationId);
    }
}

