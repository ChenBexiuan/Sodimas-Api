package com.sodimacapp.demo.controller; // Controlador REST para gestionar las postulaciones

import com.sodimacapp.demo.dto.ApplicationResponseDTO; // DTO para respuestas de postulaciones
import com.sodimacapp.demo.dto.CreateApplicationDTO;
import com.sodimacapp.demo.dto.UpdateApplicationRequestDTO; // DTO para actualizar estado y mensaje
import com.sodimacapp.demo.model.Application; // Modelo de entidad Postulación
import com.sodimacapp.demo.service.ApplicationService; // Lógica de negocio relacionada a postulaciones
import com.sodimacapp.demo.service.FileStorageService; // Servicio para manejo de archivos

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController // Define un controlador tipo REST
@RequestMapping("/api/applications") // Ruta base para las operaciones de postulación
public class ApplicationController {

    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;

    public ApplicationController(ApplicationService applicationService, FileStorageService fileStorageService) {
        this.applicationService = applicationService;
        this.fileStorageService = fileStorageService;
    }

    // Crear nueva postulación con archivo adjunto (CV)
    @PostMapping(value = "/{jobId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Application> createApplication(
            @PathVariable Integer jobId,
            @RequestPart("application") CreateApplicationDTO applicationDTO,
            @RequestPart(value = "cvFile", required = false) MultipartFile cvFile,
            Authentication authentication) {

        String candidateEmail = authentication.getName(); // Obtener correo del usuario autenticado
        String cvFileName = null;

        // Guardar archivo si fue proporcionado
        if (cvFile != null && !cvFile.isEmpty()) {
            cvFileName = fileStorageService.storeFile(cvFile);
            applicationDTO.setCvFileName(cvFileName);
        }

        Application newApplication = applicationService.createApplication(applicationDTO, candidateEmail, jobId);

        return (newApplication != null)
                ? new ResponseEntity<>(newApplication, HttpStatus.CREATED)
                : ResponseEntity.badRequest().build();
    }

    // Obtener todas las postulaciones en formato DTO (solo RRHH o managers)
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public List<ApplicationResponseDTO> getAllApplications() {
        return applicationService.getAllApplicationsAsDTO();
    }

    // Obtener una postulación por su ID (solo RRHH o managers)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Application> getApplicationById(@PathVariable Integer id) {
        return applicationService.getApplicationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<Application> createApplication(@RequestBody CreateApplicationDTO applicationDTO,
                                                         @PathVariable Integer jobId,
                                                         Authentication authentication) {
        String candidateEmail = authentication.getName();
        
        // Llamamos al método del servicio que ahora también usa el DTO.
        Application newApplication = applicationService.createApplication(applicationDTO, candidateEmail, jobId);
        
        if (newApplication != null) {
            return new ResponseEntity<>(newApplication, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }


    // Actualizar estado y enviar notificación (solo RRHH o managers)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Application> updateApplicationAndNotify(@PathVariable Integer id, @RequestBody UpdateApplicationRequestDTO request) {
        Application updatedApplication = applicationService.updateStatusAndNotify(
            id,
            request.getStatus(),
            request.getMessage()
        );
        return (updatedApplication != null)
                ? ResponseEntity.ok(updatedApplication)
                : ResponseEntity.notFound().build();
    }

    // Eliminar una postulación por ID (solo RRHH o managers)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener postulaciones hechas por un candidato específico
    @GetMapping("/candidate/{candidateId}")
    public List<ApplicationResponseDTO> getApplicationsByCandidateId(@PathVariable Integer candidateId) {
        return applicationService.getApplicationsByCandidateIdAsDTO(candidateId);
    }

    // Obtener postulaciones asociadas a un trabajo específico (solo RRHH o managers)
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public List<Application> getApplicationsByJobId(@PathVariable Integer jobId) {
        return applicationService.getApplicationsByJobId(jobId);
    }
}

