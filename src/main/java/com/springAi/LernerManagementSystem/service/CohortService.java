package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.dto.CohortDto;
import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.entity.Course;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.exceptions.CohortNotFoundException;
import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.repository.CohortRepository;
import com.springAi.LernerManagementSystem.repository.CourseRepository;
import com.springAi.LernerManagementSystem.repository.LearnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CohortService {

    private final CohortRepository cohortRepository;
    private final LearnerRepository learnerRepository;
    private final CourseRepository courseRepository;

    public CohortService(CohortRepository cohortRepository, LearnerRepository learnerRepository, CourseRepository courseRepository) {
        this.cohortRepository = cohortRepository;
        this.learnerRepository = learnerRepository;
        this.courseRepository = courseRepository;
    }

    public CohortDto createCohort(CohortDto cohortDto) throws CohortNotFoundException {
        Cohort cohort = convertToEntity(cohortDto);
        Cohort savedCohort = cohortRepository.save(cohort);
        return convertEntityToDto(savedCohort);
    }

    public Cohort createCohortWithoutCourse(Cohort cohort) throws CohortNotFoundException {
        Cohort savedCohort = cohortRepository.save(cohort);
        return savedCohort;
    }

    private Cohort convertToEntity(CohortDto cohortDto) {
        List<Learner> learners = findLearnersByIds(cohortDto.learnerIds());
        Course course = findCourseById(cohortDto.courseId());
        return new Cohort(null, cohortDto.cohortName(), cohortDto.cohortDescription(), learners, course);
    }

    private CohortDto convertEntityToDto(Cohort cohort) {
        List<Long> learnerIds;
        if (cohort.getLearners() == null || cohort.getLearners().isEmpty()) {
            learnerIds = List.of();
        } else {
            learnerIds = cohort.getLearners()
                    .stream()
                    .map(Learner::getLearnerId)
                    .toList();
        }

        Long courseId = cohort.getCourse() == null ? null : cohort.getCourse().getCourseId();
        return new CohortDto(cohort.getCohortId(), cohort.getCohortName(), cohort.getCohortDescription(), learnerIds, courseId);
    }

    public CohortDto assignLearnerToCohort(Long cohortId, Long learnerId) throws CohortNotFoundException, LearnerNotFoundException {
        Optional<Cohort> cohortOpt = cohortRepository.findById(cohortId);
        if (cohortOpt.isEmpty()) {
            throw new CohortNotFoundException("No learners found " + cohortId + " in this cohort");
        }
        Optional<Learner> learnerOpt = learnerRepository.findById(learnerId);
        if (learnerOpt.isEmpty()) {
            throw new LearnerNotFoundException("Learner with id " + learnerId + " not found");
        }
        Cohort cohort = cohortOpt.get();
        Learner learner = learnerOpt.get();
        if (cohort.getLearners() == null) {
            cohort.setLearners(new ArrayList<>());
        }
        boolean learnerAlreadyAssigned = cohort.getLearners()
                .stream()
                .anyMatch(existingLearner -> existingLearner.getLearnerId().equals(learnerId));

        if (!learnerAlreadyAssigned) {
            cohort.getLearners().add(learner);
        }

        Cohort updatedCohort = cohortRepository.save(cohort);
        return convertEntityToDto(updatedCohort);
    }

    /**
     * Return all cohorts as DTOs.
     * <p>
     * We mark the method transactional (read-only) so lazy associations can be
     * initialized while the persistence context is open. Also map each entity to a DTO
     * instead of trying to cast the result of findAll() to a single Cohort (was causing
     * a ClassCastException).
     */
    @Transactional(readOnly = true)
    public List<CohortDto> getAllCohortsWithLearnerId() throws CohortNotFoundException {
        List<Cohort> cohorts = cohortRepository.findAll();
        return cohorts.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private List<Learner> findLearnersByIds(List<Long> learnerIds) {
        if (learnerIds == null || learnerIds.isEmpty()) {
            return List.of();
        }

        List<Learner> learners = learnerRepository.findAllById(learnerIds);
        Set<Long> foundLearnerIds = learners.stream()
                .map(Learner::getLearnerId)
                .collect(Collectors.toSet());

        List<Long> missingLearnerIds = learnerIds.stream()
                .filter(learnerId -> !foundLearnerIds.contains(learnerId))
                .toList();

        if (!missingLearnerIds.isEmpty()) {
            throw new IllegalArgumentException("Learner ids not found: " + missingLearnerIds);
        }

        return learners;
    }

    private Course findCourseById(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("courseId is required to create a cohort");
        }

        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course with id " + courseId + " not found"));
    }



//    Here we are saving learner and Saving Cohort
    @Transactional
    public CohortDto createAndAssignLearnerToCohort(Long cohortId, List<Learner> learners) throws CohortNotFoundException {
        Optional<Cohort> cohortOpt = cohortRepository.findById(cohortId);
        if (cohortOpt.isEmpty()) {
            throw new CohortNotFoundException("Cohort with id " + cohortId + " not found");
        }
        Cohort cohort = cohortOpt.get();

        // Ensure cohort learners list is initialized
        if (cohort.getLearners() == null) {
            cohort.setLearners(new ArrayList<>());
        }

        List<Learner> persisted = new ArrayList<>();

        for (Learner input : learners) {
            Learner entity;

            // If caller provided an id, try to load existing learner
            if (input.getLearnerId() != null) {
                entity = learnerRepository.findById(input.getLearnerId()).orElse(null);
            } else {
                entity = null;
            }

            if (entity == null) {
                // create new learner
                entity = new Learner(null, input.getLearnerName(), input.getLearnerEmail(), input.getLearnerPhone());
                entity = learnerRepository.save(entity);
            } else {
                // update simple fields on existing learner and persist
                entity.setLearnerName(input.getLearnerName());
                entity.setLearnerEmail(input.getLearnerEmail());
                entity.setLearnerPhone(input.getLearnerPhone());
                entity = learnerRepository.save(entity);
            }

            // Ensure the learner's cohort collection contains this cohort (bidirectional)
            if (entity.getCohort() == null) {
                entity.setCohort(new ArrayList<>());
            }
            if (!entity.getCohort().contains(cohort)) {
                entity.getCohort().add(cohort);
            }

            persisted.add(entity);
        }

        // Add persisted learners to cohort learners (avoid duplicates)
        for (Learner p : persisted) {
            if (!cohort.getLearners().contains(p)) {
                cohort.getLearners().add(p);
            }
        }

        // Persist changes: rely on cascade from Cohort -> Learner to persist new learners
        Cohort updatedCohort = cohortRepository.save(cohort);
        return convertEntityToDto(updatedCohort);
    }

    public Page<Cohort> fetchPageinatedAndSortedCohort(int pageSize, int pageNumber, String sortBy, String sortDir) {
        Sort.Direction direction;
        if(sortDir.equals("asc")){
            direction = Sort.Direction.ASC;
        }else {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(pageNumber,pageSize,direction,sortBy);
        Page<Cohort> cohorts = cohortRepository.findAll(pageable);
        // Map the Page<Cohort> to Page<CohortDto> preserving paging metadata
        return cohorts;
    }
}
