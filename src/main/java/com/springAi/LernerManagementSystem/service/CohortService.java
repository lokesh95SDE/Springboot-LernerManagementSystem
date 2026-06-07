package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.dto.CohortDto;
import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.exceptions.CohortNotFoundException;
import com.springAi.LernerManagementSystem.exceptions.LearnerNotFoundException;
import com.springAi.LernerManagementSystem.repository.CohortRepository;
import com.springAi.LernerManagementSystem.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CohortService {

    @Autowired
    private CohortRepository cohortRepository;
    @Autowired
    private LearnerRepository learnerRepository;

    public CohortDto createCohort(CohortDto cohortDto) throws CohortNotFoundException {
        Cohort cohort = ConvertToEntity(cohortDto);
        Cohort savedCohort =  cohortRepository.save(cohort);
        return  ConvertEntityToDto(savedCohort);
    }

    public Cohort ConvertToEntity(CohortDto cohortDto){
        // Handle null learnerIds gracefully when the client omits them in the request body
        List<Learner> learners;
        if (cohortDto.learnerIds() == null || cohortDto.learnerIds().isEmpty()) {
            learners = List.of();
        } else {
            learners = learnerRepository.findAllById(cohortDto.learnerIds());
        }
        return new Cohort(null, cohortDto.cohortName(), cohortDto.cohortDescription(), learners);
    }

    public CohortDto ConvertEntityToDto(Cohort cohort) throws CohortNotFoundException {
        List<Long> learnerIds;
        if (cohort.getLearners() == null || cohort.getLearners().isEmpty()) {
            learnerIds = List.of();
        } else {
            learnerIds = cohort.getLearners()
                    .stream()
                    .map(Learner::getLearnerId)
                    .toList();
        }
        return new CohortDto(cohort.getCohortId(), cohort.getCohortName(), cohort.getCohortDescription(), learnerIds);
    }

    public CohortDto assignLearnerToCohort(Long cohortId, Long learnerId) throws CohortNotFoundException, LearnerNotFoundException {
        Optional<Cohort> cohortOpt = cohortRepository.findById(cohortId);
        if(cohortOpt.isEmpty() || cohortOpt == null){
            throw new CohortNotFoundException("No learners found "+ cohortId +" in this cohort");
        }
        Optional<Learner> learnerOpt = learnerRepository.findById(learnerId);
        if(learnerOpt == null || learnerOpt.isEmpty()){
            throw new LearnerNotFoundException("Learner with id " + learnerId + " not found");
        }
        Cohort cohort = cohortOpt.get();
        Learner learner = learnerOpt.get();
        if (cohort.getLearners() == null) {
            cohort.setLearners(new ArrayList<>());
        }
        cohort.getLearners().add(learner);
        Cohort updatedCohort = cohortRepository.save(cohort);
        return ConvertEntityToDto(updatedCohort);
    }

    /**
     * Return all cohorts as DTOs.
     *
     * We mark the method transactional (read-only) so lazy associations can be
     * initialized while the persistence context is open. Also map each entity to a DTO
     * instead of trying to cast the result of findAll() to a single Cohort (was causing
     * a ClassCastException).
     */
    @Transactional(readOnly = true)
    public List<CohortDto> getAllCohortsWithLearnerId() throws CohortNotFoundException {
        List<Cohort> cohorts = cohortRepository.findAll();
        return cohorts.stream().map(this::safeConvertEntityToDto).collect(Collectors.toList());
    }


    public List<Cohort> getAllCohortsWithLeanerDetails() throws CohortNotFoundException {
        return cohortRepository.findAll();
    }

    /**
     * Safe wrapper to convert entity to DTO and handle any null learners list.
     */
    private CohortDto safeConvertEntityToDto(Cohort cohort) {
        try {
            return ConvertEntityToDto(cohort);
        } catch (CohortNotFoundException e) {
            // ConvertEntityToDto does not actually throw for this case; return an empty DTO as fallback
            return new CohortDto(cohort.getCohortId(), cohort.getCohortName(), cohort.getCohortDescription(), List.of());
        }
    }
}
