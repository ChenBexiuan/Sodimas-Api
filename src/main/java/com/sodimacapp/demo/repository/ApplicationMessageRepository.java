package com.sodimacapp.demo.repository; 
import com.sodimacapp.demo.model.ApplicationMessage; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationMessageRepository extends JpaRepository<ApplicationMessage, Integer> {
    List<ApplicationMessage> findByApplicationId(Integer applicationId);
}