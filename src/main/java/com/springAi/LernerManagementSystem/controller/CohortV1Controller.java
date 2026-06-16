package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.dto.CohortDto;
import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.exceptions.CohortNotFoundException;
import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.service.CohortService;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cohorts")
public class CohortV1Controller {

    private final CohortService cohortService;

    public CohortV1Controller(CohortService cohortService) {
        this.cohortService = cohortService;
    }

    @GetMapping
    public Page<Cohort> getPageinatedAndSortedCohort(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "cohortId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) throws CohortNotFoundException {
        return cohortService.fetchPageinatedAndSortedCohort(pageSize, pageNumber, sortBy, sortDir);
    }

//    @GetMapping("/v1/cohorts")
//    public ResponseEntity<Page<CohortDto>> listCohortsPaginated(
//            @RequestParam(defaultValue = "0") int pageNumber,
//            @RequestParam(defaultValue = "10") int pageSize,
//            @RequestParam(defaultValue = "cohortId") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDir) {
//
//        // Call service with the actual pageNumber, pageSize, sortBy, sortDir
//        Page<Cohort> page = cohortService.fetchPageinatedAndSortedCohort(pageSize, pageNumber, sortBy, sortDir);
//
//        // Map Page<Cohort> to Page<CohortDto>
//        Page<CohortDto> dtoPage = page.map(cohort -> convertEntityToDto(cohort));
//
//        return ResponseEntity.ok(dtoPage);
//    }

}

//    @PostMapping
//    public Cohort createCohort(@RequestBody Cohort Cohort) throws CohortNotFoundException {
//        return cohortService.createCohortWithoutCourse(Cohort);
//    }
//
//    @PostMapping("/with-course")
//    public CohortDto createCohort(@RequestBody CohortDto cohortDto) throws CohortNotFoundException {
//        return cohortService.createCohort(cohortDto);
//    }
//
//    // Accept a JSON array of learners in the request body and assign them to the cohort
//    @PostMapping("/{cohortId}/learners")
//    public CohortDto createAndAssignLearnerToCohort(@PathVariable("cohortId") Long cohortId,
//                                                    @RequestBody List<Learner> learnersList) throws CohortNotFoundException, LearnerNotFoundException {
//        return cohortService.createAndAssignLearnerToCohort(cohortId, learnersList);
//    }
//
//    @PostMapping("/assignLearnerToCohort")
//    public CohortDto assignLearnerToCohortPathParam(@RequestParam(required=false) Long cohortId,
//                                           @RequestParam(required=false) Long learnerId) throws CohortNotFoundException, LearnerNotFoundException, BadRequestException {
//        if (cohortId == null || learnerId == null) {
//            throw new BadRequestException("cohortId and learnerId are required");
//        }
//        return cohortService.assignLearnerToCohort(cohortId, learnerId);
//    }
//

//
//    @GetMapping("/details")
//    public List<CohortDto> getAllCohortsList() throws CohortNotFoundException {
//        return cohortService.getAllCohortsWithLearnerId();
//    }
//
//    /**
//     * Exception handler for {@code LearnerNotFoundException}.
//     * <p>
//     * Spring detects this method as an exception handler and will call it when the
//     * corresponding exception is thrown from any handler method in this controller. This
//     * allows mapping domain exceptions to appropriate HTTP status codes.
//     *
//     * @param e the exception thrown
//     * @return ResponseEntity with 404 status and error message
//     */
//    @ExceptionHandler(CohortNotFoundException.class)
//    public ResponseEntity<String> handleCohortNotFoundException(CohortNotFoundException e){
//        return ResponseEntity.status(404).body(e.getMessage());
//    }
//
//    @ExceptionHandler(LearnerNotFoundException.class)
//    public ResponseEntity<String> handleLearnerNotFoundException(LearnerNotFoundException e){
//        return ResponseEntity.status(404).body(e.getMessage());
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleBadRequestException(IllegalArgumentException e){
//        return ResponseEntity.badRequest().body(e.getMessage());
//    }

