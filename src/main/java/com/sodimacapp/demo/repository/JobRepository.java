package com.sodimacapp.demo.repository; 

import com.sodimacapp.demo.model.Job; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByStatus(String status);
    List<Job> findByDepartment(String department);
    List<Job> findByLocation(String location);
}