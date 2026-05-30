package com.springAi.LernerManagementSystem.repository;

import com.springAi.LernerManagementSystem.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long>, JpaSpecificationExecutor<Learner> {
    public List<Learner> findByLearnerName(String learnerName);
    public List<Learner> findByLearnerEmail(String learnerEmail);
    public List<Learner> findByLearnerNameAndLearnerEmail(String learnerName, String learnerEmail);


    @Query("SELECT l FROM Learner l WHERE l.learnerName = :learnerName")
    public List<Learner> searchMeLearner(@Param("learnerName") String learnerName);

}
