package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.dto.LearnerDto;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.repository.LearnerRepository;
import com.springAi.LernerManagementSystem.repository.LearnerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer that contains business logic for Learner entities.
 *
 * {@code @Service} marks this as a Spring-managed bean. Spring detects it and
 * creates a singleton instance which is injected into controllers or other services.
 *
 * Internally the service typically participates in transactions (Spring can apply
 * transactional proxies) and delegates to Spring Data repositories which interact
 * with the EntityManager / Hibernate to perform persistence operations.
 */
@Service
public class LearnerService {

    @Autowired
    private LearnerRepository  _learnerRepository;

//Request JSON
//   ↓
//LearnerDto
//   ↓ convertToEntity
//Learner entity
//   ↓ repository.save()
//Database row
//   ↓ saved entity with generated ID
//LearnerDto response
//Internal Working?
//    When controller receives JSON, Spring uses Jackson to deserialize request body into your DTO.
//    Then service converts DTO into entity because JPA repository only understands entity classes.

    /**
     * Create and persist a new Learner.
     *
     * Internals:
     * - convertToEntity creates a new entity instance from the DTO.
     * - _learnerRepository.save(...) delegates to Spring Data which uses the EntityManager
     *   to persist or merge the entity. Hibernate assigns any generated id according to the
     *   entity's @GeneratedValue strategy. The returned entity is managed by the persistence
     *   context until the transaction commits.
     * - convertToDTO prepares a DTO that is safe to return to clients (avoid exposing JPA internals).
     *
     * @param learnerDto input DTO
     * @return saved LearnerDto with generated id
     */
    public LearnerDto createLearner(LearnerDto learnerDto) {
        Learner learner = convertToEntity(learnerDto);
        Learner savedLearner = _learnerRepository.save(learner);
        return convertToDTO(savedLearner);
    }

    /**
     * Retrieve all learners.
     *
     * Internals: this calls {@code JpaRepository.findAll()} which executes a simple SELECT query
     * to fetch all rows. Depending on size, this can be expensive and should be paginated in real apps.
     *
     * @return list of Learner entities
     */
    public List<Learner> getAllLearners() {
        return _learnerRepository.findAll();
    }

    /**
     * Find a learner by id. If not present, throws {@code LearnerNotFoundException}.
     *
     * Internals: {@code findById} returns an Optional because the record may not exist.
     * The service chooses to throw an unchecked exception which the controller maps to 404.
     *
     * @param learnerId id to look up
     * @return found Learner entity
     * @throws LearnerNotFoundException when the learner does not exist
     */
    public Learner findById(Long learnerId) throws LearnerNotFoundException {
        Optional<Learner> learnerOptional = _learnerRepository.findById(learnerId);
        if(learnerOptional.isEmpty()){
            throw new LearnerNotFoundException("Learner with id" + learnerId + " not found");
        }
        return learnerOptional.get();
    }

//    https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
//    JPA Query Methods
    /**
     * Find learners by exact name using Spring Data query derivation.
     * Internally Spring Data generates the query implementation at runtime by
     * analyzing the method name and creating a proxy that executes the corresponding
     * JPQL/SQL when called.
     *
     * @param learnerName exact name match
     * @return list of matching Learner entities
     */
    public List<Learner> findByName(String learnerName) {
        return _learnerRepository.findByLearnerName(learnerName);
    }

    /**
     * Find learners by exact email using Spring Data query derivation.
     *
     * @param learnerEmail email to match
     * @return list of matching Learner entities
     */
    public List<Learner> findByEmail(String learnerEmail) {
        return _learnerRepository.findByLearnerEmail(learnerEmail);
    }

    /**
     * Find learners matching both name and email using a derived query method.
     *
     * @param learnerName name to match
     * @param learnerEmail email to match
     * @return list of matching Learner entities
     */
    public List<Learner> findByNameandEmail(String learnerName, String learnerEmail) {
        return _learnerRepository.findByLearnerNameAndLearnerEmail(
                learnerName,
                learnerEmail);
    }

    /**
     * Search learners using dynamic criteria built from Specifications.
     *
     * Internals:
     * - Specifications are predicates that wrap the JPA Criteria API. They are evaluated
     *   when the repository performs the query. Until then they are simple, composable
     *   lambda objects.
     * - Combining specifications with {@code .and()} builds a single CriteriaQuery which
     *   Hibernate converts to SQL and executes.
     * - Results are mapped to DTOs to decouple the API model from persistence.
     *
     * @param learnerName optional name filter
     * @param learnerEmail optional email filter
     * @param learnerPhone optional phone filter
     * @return matching learners as DTOs
     */
    public List<LearnerDto> searchLearners(String learnerName, String learnerEmail, String learnerPhone) {
        Specification<Learner> spec = Specification
                .where(LearnerSpecification.hasName(learnerName))
                .and(LearnerSpecification.hasEmail(learnerEmail))
                .and(LearnerSpecification.hasPhone(learnerPhone));

        List<Learner> entities = _learnerRepository.findAll(spec);

        // Convert to DTOs and return
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Convert a Learner entity into a DTO suitable for API responses.
     * 
     * Why convert: Entities may contain persistence-related lazily loaded fields or
     * internal identifiers that you should not expose directly to clients. DTOs provide
     * a stable shape for your API contract.
     *
     * @param learner entity to convert
     * @return LearnerDto representing the entity
     */
    private LearnerDto convertToDTO(Learner learner) {
        return new LearnerDto(
            learner.getLearnerId(),
            learner.getLearnerName(),
            learner.getLearnerEmail(),
            learner.getLearnerPhone()
        );
    }

    /**
     * Convert a DTO into a Learner entity.
     *
     * Note: This creates a simple POJO that will be managed by the persistence provider when
     * passed to {@code save}. For updates you might want to load the existing entity from
     * the DB and apply changes to avoid unintentionally overwriting fields.
     *
     * @param learnerDto DTO to convert
     * @return new Learner entity
     */
    private Learner convertToEntity(LearnerDto learnerDto) {
        return new Learner(
                learnerDto.learnerId(),
                learnerDto.learnerName(),
                learnerDto.learnerEmail(),
                learnerDto.learnerPhone()
        );
    }

}
