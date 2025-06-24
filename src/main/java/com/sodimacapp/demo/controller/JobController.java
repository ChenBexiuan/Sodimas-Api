package com.sodimacapp.demo.controller; // Paquete donde se encuentra el controlador de trabajos

import com.sodimacapp.demo.dto.CreateJobDTO;
import com.sodimacapp.demo.dto.JobResponseDTO; // Importación del DTO para la respuesta de trabajos
import com.sodimacapp.demo.model.Job; // Importación del modelo de trabajo
import com.sodimacapp.demo.service.JobService; // Importación del servicio de trabajos
import org.springframework.http.HttpStatus; // Importación para los códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Importación para la entidad de respuesta HTTP
import org.springframework.security.access.prepost.PreAuthorize; // Importación para la autorización basada en roles
import org.springframework.security.core.Authentication; // Importación para la autenticación del usuario
import org.springframework.web.bind.annotation.*; // Importación para las anotaciones de controladores
import java.util.List; // Importación para la lista de trabajos

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/jobs") // Mapeo de la ruta base para los trabajos
public class JobController {

    private final JobService jobService; // Servicio para manejar la lógica de trabajos

    // Constructor que inyecta el servicio de trabajos
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Endpoint para obtener todos los trabajos
    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobsWithCreator(); // Retorna la lista de trabajos con su creador
    }

    // Endpoint para obtener un trabajo por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Integer id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok) // Retorna el trabajo si se encuentra
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 si no se encuentra
    }
    
    // Endpoint para crear un nuevo trabajo
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')") 
    public ResponseEntity<Job> createJob(@RequestBody CreateJobDTO jobDTO, Authentication authentication) {
        String creatorEmail = authentication.getName();
        
        // Llamamos al servicio que ahora también usa el DTO.
        Job newJob = jobService.createJob(jobDTO, creatorEmail, authentication.getAuthorities().iterator().next().getAuthority());
        
        if (newJob != null) {
            return new ResponseEntity<>(newJob, HttpStatus.CREATED);
        }
        return ResponseEntity.badRequest().build();
    }
    // Endpoint para actualizar un trabajo existente por su ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')")
    public ResponseEntity<Job> updateJob(@PathVariable Integer id, @RequestBody CreateJobDTO jobDetails) {
        Job updatedJob = jobService.updateJob(id, jobDetails);
        if (updatedJob != null) {
            return ResponseEntity.ok(updatedJob);
        }
        return ResponseEntity.notFound().build();
    }


    // Endpoint para eliminar un trabajo por su ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('HR')") // Solo usuarios autorizados pueden eliminar trabajos
    public ResponseEntity<Void> deleteJob(@PathVariable Integer id) {
        jobService.deleteJob(id); // Elimina el trabajo
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Endpoint para obtener trabajos por su estado
    @GetMapping("/status/{status}")
    public List<Job> getJobsByStatus(@PathVariable String status) {
        return jobService.getJobsByStatus(status); // Retorna la lista de trabajos con el estado especificado
    }
}
