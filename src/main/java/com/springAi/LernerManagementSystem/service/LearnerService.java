package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearnerService {

    @Autowired
    private LearnerRepository  _learnerRepository;

    public Learner createLearner(Learner learner) {
        return _learnerRepository.save(learner);
    }

    public List<Learner> getAllLearners() {
        return _learnerRepository.findAll();
    }
}
