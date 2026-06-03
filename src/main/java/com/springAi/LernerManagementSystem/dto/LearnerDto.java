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

public record LearnerDto(
        Long learnerId,
        String learnerName,
        String learnerEmail,
        String learnerPhone
) {

}
