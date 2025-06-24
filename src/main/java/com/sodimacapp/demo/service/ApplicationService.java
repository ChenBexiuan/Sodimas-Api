package com.sodimacapp.demo.service;

import com.sodimacapp.demo.dto.ApplicationResponseDTO;
import com.sodimacapp.demo.dto.CreateApplicationDTO;
import com.sodimacapp.demo.model.Application;
import com.sodimacapp.demo.model.ApplicationMessage;
import com.sodimacapp.demo.model.Job;
import com.sodimacapp.demo.model.User;
import com.sodimacapp.demo.repository.ApplicationMessageRepository;
import com.sodimacapp.demo.repository.ApplicationRepository;
import com.sodimacapp.demo.repository.JobRepository;
import com.sodimacapp.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que maneja toda la lógica de negocio relacionada con las postulaciones.
 */
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationMessageRepository messageRepository;

    /**
     * Constructor con inyección de dependencias.
     */
    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            ApplicationMessageRepository messageRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.messageRepository = messageRepository;
    }

    // ====================== MÉTODOS CON DTOs PARA EL FRONTEND ======================

    /**
     * Retorna todas las postulaciones con información detallada en formato DTO.
     */
    public List<ApplicationResponseDTO> getAllApplicationsAsDTO() {
        return applicationRepository.findAll().stream()
                .map(ApplicationResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las postulaciones realizadas por un candidato específico (por ID).
     */
    public List<ApplicationResponseDTO> getApplicationsByCandidateIdAsDTO(Integer candidateId) {
        return applicationRepository.findByCandidateId(candidateId).stream()
                .map(ApplicationResponseDTO::new)
                .collect(Collectors.toList());
    }

    // ====================== MÉTODO TRANSACCIONAL DE ESTADO + MENSAJE ======================

    /**
     * Actualiza el estado de una postulación y guarda un mensaje asociado. Todo ocurre en una transacción.
     */
    @Transactional
    public Application updateStatusAndNotify(Integer applicationId, String newStatus, String messageText) {
        return applicationRepository.findById(applicationId).map(application -> {
            application.setStatus(newStatus);
            Application savedApplication = applicationRepository.save(application);

            // Si se proporciona un mensaje, lo guardamos también
            if (messageText != null && !messageText.isEmpty()) {
                ApplicationMessage message = new ApplicationMessage();
                message.setApplication(savedApplication);
                message.setText(messageText);
                message.setSender("recruiter"); // Asumimos que siempre lo envía un reclutador
                messageRepository.save(message);
            }

            return savedApplication;
        }).orElse(null);
    }

    // ====================== CRUD BÁSICO DE POSTULACIONES ======================

    /**
     * Obtiene todas las postulaciones registradas en el sistema.
     */
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * Obtiene una postulación específica por su ID.
     */
    public Optional<Application> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    /**
     * Crea una nueva postulación vinculando el usuario (candidato) y la oferta laboral.
     */
    public Application createApplication(CreateApplicationDTO dto, String candidateEmail, Integer jobId) {
        Optional<User> candidate = userRepository.findByEmail(candidateEmail);
        Optional<Job> job = jobRepository.findById(jobId);

        if (candidate.isPresent() && job.isPresent()) {
            Application application = new Application(); // Creamos una nueva entidad vacía
            
            // Mapeamos los datos del DTO a la nueva entidad
            application.setCoverLetter(dto.getCoverLetter());
            application.setExperience(dto.getExperience());
            application.setSkills(dto.getSkills());
            application.setCvFileName(dto.getCvFileName());
            
            // Asignamos las relaciones
            application.setCandidate(candidate.get());
            application.setJob(job.get());
            
            // El estado 'submitted' se asignará por el valor por defecto de la base de datos.
            
            return applicationRepository.save(application);
        }
        return null;
    }


    /**
     * Actualiza los campos editables de una postulación (carta, experiencia, skills, etc).
     */
    public Application updateApplication(Integer id, Application applicationDetails) {
        return applicationRepository.findById(id).map(application -> {
            application.setStatus(applicationDetails.getStatus());
            application.setCoverLetter(applicationDetails.getCoverLetter());
            application.setExperience(applicationDetails.getExperience());
            application.setSkills(applicationDetails.getSkills());
            application.setCvFileName(applicationDetails.getCvFileName());
            return applicationRepository.save(application);
        }).orElse(null);
    }

    /**
     * Cambia únicamente el estado de una postulación.
     */
    public Application updateApplicationStatus(Integer applicationId, String newStatus) {
        return applicationRepository.findById(applicationId).map(application -> {
            application.setStatus(newStatus);
            return applicationRepository.save(application);
        }).orElse(null);
    }

    /**
     * Elimina una postulación por su ID.
     */
    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }

    // ====================== FILTRADO POR RELACIONES ======================

    /**
     * Retorna todas las postulaciones hechas por un candidato específico.
     */
    public List<Application> getApplicationsByCandidateId(Integer candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }

    /**
     * Retorna todas las postulaciones realizadas para una oferta laboral específica.
     */
    public List<Application> getApplicationsByJobId(Integer jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}

