package com.springAi.LernerManagementSystem.service;

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

    public LearnerDto createLearner(LearnerDto learnerDto) {
        Learner learner = convertToEntity(learnerDto);
        Learner savedLearner = _learnerRepository.save(learner);
        return convertToDTO(savedLearner);
    }

    public List<Learner> getAllLearners() {
        return _learnerRepository.findAll();
    }

    public Optional<Learner> findById(Long learnerId) {
        return _learnerRepository.findById(learnerId);
    }

//    https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
//    JPA Query Methods
    public List<Learner> findByName(String learnerName) {
        return _learnerRepository.findByLearnerName(learnerName);
    }

    public List<Learner> findByEmail(String learnerEmail) {
        return _learnerRepository.findByLearnerEmail(learnerEmail);
    }

    public List<Learner> findByNameandEmail(String learnerName, String learnerEmail) {
        return _learnerRepository.findByLearnerNameAndLearnerEmail(
                learnerName,
                learnerEmail);
    }

    public List<LearnerDto> searchLearners(String learnerName, String learnerEmail, String learnerPhone) {
        Specification<Learner> spec = Specification
                .where(LearnerSpecification.hasName(learnerName))
                .and(LearnerSpecification.hasEmail(learnerEmail))
                .and(LearnerSpecification.hasPhone(learnerPhone));

        List<Learner> entities = _learnerRepository.findAll(spec);

        // Convert to DTOs and return
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    private LearnerDto convertToDTO(Learner learner) {
        return new LearnerDto(
            learner.getLearnerId(),
            learner.getLearnerName(),
            learner.getLearnerEmail(),
            learner.getLearnerPhone()
        );
    }

    private Learner convertToEntity(LearnerDto learnerDto) {
        return new Learner(
                learnerDto.learnerId(),
                learnerDto.learnerName(),
                learnerDto.learnerEmail(),
                learnerDto.learnerPhone()
        );
    }

}
