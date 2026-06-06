package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.dto.LearnerDto;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.service.LearnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) -----------------> any where if use date formate can use this in controller

/**
 * REST controller that exposes CRUD and search endpoints for Learner resources.
 *
 * This class is detected by Spring's component scan because of the {@code @RestController}
 * annotation. Spring creates a singleton proxy instance and injects required dependencies
 * into the constructor at startup (constructor injection). Internally Spring resolves the
 * {@code LearnerService} bean from the ApplicationContext and supplies it here.
 */
@RestController
public class LearnerController {

//    @Autowired
//    private LearnerService _learnerService;
    private final LearnerService _learnerService;

    /**
     * Constructor used by Spring to perform dependency injection.
     * <p>
     * Internally Spring calls this constructor when creating the controller bean and
     * supplies the {@code LearnerService} instance from the ApplicationContext. This is
     * preferable to field injection because it makes the dependency explicit and easier
     * to test.
     *
     * @param learnerService service that contains business logic for Learner
     */
    public LearnerController(LearnerService learnerService) {
        this._learnerService = learnerService;
    }

    /**
     * Create a new Learner.
     *
     * How it works internally:
     * - Spring MVC maps this method to POST /learners and uses HttpMessageConverters (Jackson)
     *   to deserialize the JSON request body into {@code LearnerDto}.
     * - The controller delegates to the service layer which converts the DTO to an entity
     *   and saves it using Spring Data JPA. Hibernate associates the entity with a persistence
     *   context and, when saved, assigns a generated id.
     * - The saved entity is converted back to a DTO and returned. Spring MVC serializes the
     *   DTO to JSON for the HTTP response.
     *
     * @param learnerDto DTO representing the learner to create
     * @return the saved LearnerDto including generated id
     */
    @PostMapping("/learners")
    public LearnerDto createLearner(@RequestBody LearnerDto learnerDto ){
        return _learnerService.createLearner(learnerDto);
    }



    /**
     * Retrieve a learner by id.
     *
     * Details:
     * - Path variable is mapped by Spring MVC into the method parameter.
     * - The service layer retrieves the entity from the database; if not found a
     *   {@code LearnerNotFoundException} is thrown and handled below.
     * - The controller returns a {@code ResponseEntity} so we can control HTTP status codes.
     *
     * @param learnerId id of the learner to retrieve
     * @return 200 OK with Learner body if found
     */
    @GetMapping("/learners/{learnerId}")
    public ResponseEntity<Learner> getLearnerById(@PathVariable("learnerId") Long learnerId) throws LearnerNotFoundException {
        Learner learner =  _learnerService.findById(learnerId);
        return ResponseEntity.ok(learner);
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
    @ExceptionHandler(LearnerNotFoundException.class)
    public ResponseEntity<String> handleLearnerNotFoundException(LearnerNotFoundException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    /**
     * List learners. Supports optional query parameters to filter by name and/or email.
     *
     * How it works internally:
     * - Spring maps query parameters to method parameters. If both are provided we call
     *   a repository query method that uses derived query names and Spring Data JPA constructs
     *   the appropriate SQL at runtime.
     * - If no filters are provided, {@code findAll()} is used.
     *
     * @param learnerName optional learner name filter
     * @param learnerEmail optional learner email filter
     * @return list of matching Learner entities
     */
    @GetMapping("/learners")
    public List<Learner> getLearnerById(@RequestParam(value="learnerName",required = false) String learnerName,
                                        @RequestParam(value="learnerEmail",required = false) String learnerEmail)
    {
        if(learnerName != null && learnerEmail != null){
            return _learnerService.findByNameandEmail(learnerName,learnerEmail);
        }
        if(learnerName != null ){
            return _learnerService.findByName(learnerName);
        }
        if(learnerEmail != null ){
            return _learnerService.findByEmail(learnerEmail);
        }
        return _learnerService.getAllLearners();
    }

    /**
     * Search learners using dynamic criteria.
     *
     * Internals:
     * - This endpoint demonstrates Spring Data JPA Specifications which compose JPA
     *   Criteria API predicates at runtime. The service builds a Specification and passes
     *   it to the repository. Spring Data translates the Specification into a CriteriaQuery,
     *   which Hibernate converts to SQL and executes against the database.
     * - The result entities are converted to DTOs before returning to avoid exposing
     *   internal persistence details.
     *
     * @param learnerName optional name filter (supports partial match)
     * @param learnerEmail optional email filter (exact match)
     * @param learnerPhone optional phone filter (exact match)
     * @return 200 OK with list of matching LearnerDto objects
     */
    @GetMapping("/learners/search")
    public ResponseEntity<List<LearnerDto>> search(
            @RequestParam(required = false) String learnerName,
            @RequestParam(required = false) String learnerEmail,
            @RequestParam(required = false) String learnerPhone) {

        // 1. Controller calls the Service
        List<LearnerDto> results = _learnerService.searchLearners(learnerName, learnerEmail, learnerPhone);

        // 2. Controller wraps the result in an HTTP 200 OK response
        return ResponseEntity.ok(results);
    }

}
