package com.springAi.LernerManagementSystem.repository;

import com.springAi.LernerManagementSystem.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long> {

}
