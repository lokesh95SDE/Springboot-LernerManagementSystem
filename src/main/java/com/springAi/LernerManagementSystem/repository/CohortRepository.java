package com.springAi.LernerManagementSystem.repository;

import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface CohortRepository extends JpaRepository<Cohort,Long> {
}
