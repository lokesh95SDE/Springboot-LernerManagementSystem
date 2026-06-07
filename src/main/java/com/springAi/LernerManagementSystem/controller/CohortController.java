package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.dto.CohortDto;
import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.exceptions.CohortNotFoundException;
import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.service.CohortService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CohortController {

    @Autowired
    private CohortService cohortService;

    @PostMapping("/cohorts")
    public CohortDto createCohort(@RequestBody CohortDto cohortDto) throws CohortNotFoundException {
        return cohortService.createCohort(cohortDto);
    }

    @PostMapping("/assignLearnerToCohort")
    public CohortDto assignLearnerToCohort(@RequestParam(required=false) Long cohortId,
                                           @RequestParam(required=false) Long learnerId) throws CohortNotFoundException, LearnerNotFoundException, BadRequestException {
        if (cohortId == null || learnerId == null) {
            throw new BadRequestException("cohortId and learnerId are required");
        }
        return cohortService.assignLearnerToCohort(cohortId, learnerId);
    }

    @GetMapping("/cohorts")
    public List<CohortDto> getAllCohorts() throws CohortNotFoundException {
        return cohortService.getAllCohortsWithLearnerId();
    }

    @GetMapping("/cohortsList")
    public List<Cohort> getAllCohortsList() throws CohortNotFoundException {
        return cohortService.getAllCohortsWithLeanerDetails();
    }

    /**
     * Exception handler for {@code LearnerNotFoundException}.
     * <p>
     * Spring detects this method as an exception handler and will call it when the
     * corresponding exception is thrown from any handler method in this controller. This
     * allows mapping domain exceptions to appropriate HTTP status codes.
     *
     * @param e the exception thrown
     * @return ResponseEntity with 404 status and error message
     */
    @ExceptionHandler(CohortNotFoundException.class)
    public ResponseEntity<String> handleCohortNotFoundException(CohortNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }
}
