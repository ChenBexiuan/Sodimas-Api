package com.sodimacapp.demo.repository; 

import com.sodimacapp.demo.model.Application; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByCandidateId(Integer candidateId);
    List<Application> findByJobId(Integer jobId);
    List<Application> findByStatus(String status);
}