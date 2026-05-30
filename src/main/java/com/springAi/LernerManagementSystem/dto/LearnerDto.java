package com.springAi.LernerManagementSystem.dto;
//DTO decouples API contract from persistence model. Entity is managed by JPA/Hibernate and may contain internal fields,
// relationships, lazy-loaded associations, or sensitive data. DTO gives control over request/response shape,
// validation, versioning, and security.


//DTO protects sensitive fields from accidental exposure.
//DTO lets you add validation annotations like @Email, @NotBlank.
//DTO avoids lazy-loading serialization bugs from entity relationships.
//DTO helps API versioning.
//DTO makes controller/service tests cleaner.
//DTO prevents clients from setting fields they should not control, like id, createdAt, role, status.]


public record LearnerDto(
        Long learnerId,
        String learnerName,
        String learnerEmail,
        String learnerPhone
) {

}
