package com.springAi.LernerManagementSystem.repository;

import com.springAi.LernerManagementSystem.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for Learner entities.
 *
 * Internals:
 * - Spring creates a runtime proxy implementation for this interface and provides
 *   implementations for CRUD methods declared in {@code JpaRepository}.
 * - Methods whose names follow the query derivation rules (like findByLearnerName)
 *   are analyzed by Spring Data to build appropriate JPQL/SQL queries at runtime.
 * - {@code JpaSpecificationExecutor} allows executing dynamic queries built with
 *   Specifications (JPA Criteria API).
 */
@Repository
public interface LearnerRepository extends JpaRepository<Learner,Long>, JpaSpecificationExecutor<Learner> {
    /**
     * Derived query to find learners by exact name.
     * @param learnerName name to match
     * @return matching learners
     */
    public List<Learner> findByLearnerName(String learnerName);

    /**
     * Derived query to find learners by exact email.
     * @param learnerEmail email to match
     * @return matching learners
     */
    public List<Learner> findByLearnerEmail(String learnerEmail);

    /**
     * Derived query that matches both name and email.
     * @param learnerName name to match
     * @param learnerEmail email to match
     * @return matching learners
     */
    public List<Learner> findByLearnerNameAndLearnerEmail(String learnerName, String learnerEmail);


    /**
     * Example of a custom JPQL query using {@code @Query}. Spring Data will parse
     * the JPQL and execute it when this method is called.
     * @param learnerName name to search
     * @return matching learners
     */
    @Query("SELECT l FROM Learner l WHERE l.learnerName = :learnerName")
    public List<Learner> searchMeLearner(@Param("learnerName") String learnerName);

}
