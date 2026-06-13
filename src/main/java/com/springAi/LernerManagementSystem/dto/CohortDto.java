package com.springAi.LernerManagementSystem.dto;

import java.util.List;

/**
 * DTO representing a Cohort in API responses/requests.
 *
 * Includes learnerIds so the API can send/receive associations as simple id lists
 * while keeping the full Learner objects out of the Cohort JSON payload (avoids
 * circular nesting).
 */
public record CohortDto(
        Long cohortId,
        String cohortName,
        String cohortDescription,
        List<Long> learnerIds,
        Long courseId
) {

}
