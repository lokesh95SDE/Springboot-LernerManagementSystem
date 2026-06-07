package com.springAi.LernerManagementSystem.entity;

import jakarta.persistence.JoinTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

import java.util.List;

@Entity
public class Cohort {
    /**
     * Use field access consistently. Placing @Id on the field ensures JPA inspects
     * annotations on fields rather than getters. Mixing access types can cause
     * relationship annotations on fields to be ignored which leads Hibernate to try
     * to map entity types as basic JDBC types (the root cause of the JdbcType error).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cohortId;

    private String cohortName;

    private String cohortDescription;

    /**
     * Many-to-many association to Learner. Define a join table explicitly so Hibernate
     * knows how to map the relationship to relational tables.
     */
    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "cohort_learner",
//            joinColumns = @JoinColumn(name = "cohort_id"),
//            inverseJoinColumns = @JoinColumn(name = "learner_id")
//    )
    private List<Learner> learners;

    // No-argument constructor required by JPA
    public Cohort() {
    }

    public Cohort(Long cohortId, String cohortName, String cohortDescription, List<Learner> learners) {
        this.cohortId = cohortId;
        this.cohortName = cohortName;
        this.cohortDescription = cohortDescription;
        this.learners = learners;
    }

    public Long getCohortId() {
        return cohortId;
    }

    public void setCohortId(Long cohortId) {
        this.cohortId = cohortId;
    }

    public String getCohortName() {
        return cohortName;
    }

    public void setCohortName(String cohortName) {
        this.cohortName = cohortName;
    }

    public String getCohortDescription() {
        return cohortDescription;
    }

    public void setCohortDescription(String cohortDescription) {
        this.cohortDescription = cohortDescription;
    }

    public List<Learner> getLearners() {
        return learners;
    }

    public void setLearners(List<Learner> learners) {
        this.learners = learners;
    }



}
