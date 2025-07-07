package com.sodimacapp.demo.controller; // Paquete donde se encuentra el controlador de trabajos

import com.sodimacapp.demo.dto.CreateJobDTO; // Importación del DTO para la creación de trabajos
import com.sodimacapp.demo.dto.JobResponseDTO; // Importación del DTO para la respuesta de trabajos
import com.sodimacapp.demo.model.Job; // Importación del modelo de trabajo
import com.sodimacapp.demo.service.JobService; // Importación del servicio de trabajos
import org.springframework.http.HttpStatus; // Importación para los códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importación para la entidad de respuesta HTTP
import org.springframework.security.access.prepost.PreAuthorize; // Importación para la autorización basada en roles
import org.springframework.security.core.Authentication; // Importación para la autenticación del usuario
import org.springframework.web.bind.annotation.*; // Importación para las anotaciones de controladores
import java.util.List; // Importación para la lista de trabajos
import java.util.Map; // Importación para el payload de actualización de estado

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/jobs") // Mapeo de la ruta base para los trabajos
public class JobController {

    private final JobService jobService; // Servicio para manejar la lógica de trabajos

    // Constructor que inyecta el servicio de trabajos
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Endpoint para obtener todos los trabajos como DTOs
    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobsWithCreator();
    }

    // Endpoint para obtener un trabajo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Integer id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // --- ENDPOINT DE CREACIÓN MODIFICADO CON DTO ---
    // Ahora recibe un DTO en lugar de la entidad completa para mayor seguridad.
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')") 
    public ResponseEntity<Job> createJob(@RequestBody CreateJobDTO jobDTO, Authentication authentication) {
        String creatorEmail = authentication.getName();
        // Obtenemos el rol desde el objeto Authentication
        String creatorRole = authentication.getAuthorities().iterator().next().getAuthority();
        
        Job newJob = jobService.createJob(jobDTO, creatorEmail, creatorRole);
        
        if (newJob != null) {
            return new ResponseEntity<>(newJob, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
    
    // --- ENDPOINT DE ACTUALIZACIÓN MODIFICADO CON DTO ---
    // También utiliza un DTO para la actualización, evitando que se modifiquen campos no deseados.
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody CreateJobDTO jobDetails) {
        Job updatedJob = jobService.updateJob(id, jobDetails);
        if (updatedJob != null) {
            return ResponseEntity.ok(updatedJob);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Endpoint específico para actualizar SOLO el estado de un trabajo
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<Job> updateJobStatus(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Job updatedJob = jobService.updateJobStatus(id, newStatus);
        if (updatedJob != null) {
            return ResponseEntity.ok(updatedJob);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar un trabajo por su ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para obtener trabajos por su estado
    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(@PathVariable String status) {
        return jobService.getJobsByStatus(status);
    }
}
