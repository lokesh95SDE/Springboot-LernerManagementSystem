package com.springAi.LernerManagementSystem.dto;
//DTO decouples API contract from persistence model. Entity is managed by JPA/Hibernate and may contain internal fields,
// relationships, lazy-loaded associations, or sensitive data. DTO gives control over request/response shape,
// validation, versioning, and security.
//
//
//DTO protects sensitive fields from accidental exposure.
//DTO lets you add validation annotations like @Email, @NotBlank.
//DTO avoids lazy-loading serialization bugs from entity relationships.
//DTO helps API versioning.
//DTO makes controller/service tests cleaner.
//DTO prevents clients from setting fields they should not control, like id, createdAt, role, status.]
//
//1. The Mental Model: DTO vs. Specification
//To understand the difference, think of your application like a Warehouse (Database). Your Entity (Learner) is the actual physical product sitting on the warehouse shelf.
//
//The Specification is the Search Warrant: It is the set of instructions you give to the warehouse worker (Hibernate) telling them exactly which products to go find on the shelves. It is used to build dynamic, programmatic database queries at runtime.[cite_end]
//
//Example: "Go find me all learners where City = 'Chennai' AND Date = 'Today'."
//
//The DTO is the Shipping Box: Once the worker finds the product (Entity), you don't mail the raw product with its internal warehouse barcodes to the customer. You pack it into a clean, safe shipping box (the DTO) that only contains what the customer is allowed to see. It separates the database layer from the presentation layer.[cite_end]
//
//In short: * Specifications pull data IN from the database.
//
//DTOs push data OUT to the API client (or accept incoming JSON).

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Data Transfer Object for Learner API requests and responses.
 *
 * Records are immutable holders for values and provide a concise way to declare
 * a simple DTO. The controller receives JSON and Jackson maps fields to the record's
 * components. Since records are value-based, they are a good fit for stateless API models.
 *
 * Components:
 * @param learnerId unique identifier (may be null for create requests)
 * @param learnerName learner's name
 * @param learnerEmail learner's email
 * @param learnerPhone learner's phone number
 */
public class LearnerDto {
    private Long learnerId;
    @NotNull
    private String learnerName;
    @Email
    private String learnerEmail;
    @NotNull
    private String learnerPhone;
    private List<CohortDto> cohortDtos;

    public LearnerDto() {
    }

    public LearnerDto(Long learnerId, String learnerName, String learnerEmail, String learnerPhone, List<CohortDto> cohortDtos) {
        this.learnerId = learnerId;
        this.learnerName = learnerName;
        this.learnerEmail = learnerEmail;
        this.learnerPhone = learnerPhone;
        this.cohortDtos = cohortDtos;
    }
    public List<CohortDto> getCohortDtos() {
        return cohortDtos;
    }
    public void setCohortDtos(List<CohortDto> cohortDtos) {
        this.cohortDtos = cohortDtos;
    }

    public Long getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(Long learnerId) {
        this.learnerId = learnerId;
    }

    public String getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(String learnerName) {
        this.learnerName = learnerName;
    }

    public String getLearnerEmail() {
        return learnerEmail;
    }

    public void setLearnerEmail(String learnerEmail) {
        this.learnerEmail = learnerEmail;
    }

    public String getLearnerPhone() {
        return learnerPhone;
    }

    public void setLearnerPhone(String learnerPhone) {
        this.learnerPhone = learnerPhone;
    }

}
