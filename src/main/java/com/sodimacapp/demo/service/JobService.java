package com.sodimacapp.demo.service;

import com.sodimacapp.demo.dto.CreateJobDTO; // Importamos el nuevo DTO
import com.sodimacapp.demo.dto.JobResponseDTO;
import com.sodimacapp.demo.model.Job;
import com.sodimacapp.demo.model.User;
import com.sodimacapp.demo.repository.JobRepository;
import com.sodimacapp.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio encargado de la lógica de negocio relacionada con los trabajos/ofertas laborales.
 */
@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    /**
     * Constructor con inyección de dependencias de los repositorios necesarios.
     */
    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    /**
     * Devuelve una lista de trabajos en formato DTO, incluyendo información del creador.
     */
    public List<JobResponseDTO> getAllJobsWithCreator() {
        return jobRepository.findAll().stream()
                .map(JobResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve todos los trabajos completos (entidades Job).
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    /**
     * Busca un trabajo específico por su ID.
     */
    public Optional<Job> getJobById(Integer id) {
        return jobRepository.findById(id);
    }

    /**
     * Crea una nueva oferta laboral a partir de un DTO.
     * Asigna el creador y el estado del trabajo basado en el rol.
     */
    public Job createJob(CreateJobDTO dto, String creatorEmail, String creatorRole) {
        Optional<User> creator = userRepository.findByEmail(creatorEmail);
        
        if (creator.isPresent()) {
            Job job = new Job();
            
            // Mapeamos los datos del DTO a la nueva entidad
            job.setTitle(dto.getTitle());
            job.setDepartment(dto.getDepartment());
            job.setLocation(dto.getLocation());
            job.setType(dto.getType());
            job.setSalary(dto.getSalary());
            job.setDescription(dto.getDescription());
            job.setRequirements(dto.getRequirements());
            
            job.setCreatedBy(creator.get());

            // Asignamos el estado basado en el rol del creador
            if ("ROLE_HR".equals(creatorRole)) {
                job.setStatus("approved");
            } else {
                job.setStatus("pending");
            }
            
            return jobRepository.save(job);
        }
        return null;
    }

    /**
     * Actualiza los datos de una oferta laboral existente a partir de un DTO.
     */
    public Job updateJob(Integer id, CreateJobDTO jobDetails) {
        return jobRepository.findById(id).map(existingJob -> {
            existingJob.setTitle(jobDetails.getTitle());
            existingJob.setDepartment(jobDetails.getDepartment());
            existingJob.setLocation(jobDetails.getLocation());
            existingJob.setType(jobDetails.getType());
            existingJob.setSalary(jobDetails.getSalary());
            existingJob.setDescription(jobDetails.getDescription());
            existingJob.setRequirements(jobDetails.getRequirements());
            return jobRepository.save(existingJob);
        }).orElse(null);
    }

    /**
     * Elimina una oferta laboral por su ID.
     */
    public void deleteJob(Integer id) {
        jobRepository.deleteById(id);
    }

    /**
     * Obtiene todos los trabajos que tienen un estado específico.
     */
    public List<Job> getJobsByStatus(String status) {
        return jobRepository.findByStatus(status);
    }
}
