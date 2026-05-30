package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LearnerController {

    @Autowired
    private LearnerService _learnerService;

    @PostMapping("/learners")
    public Learner createLearner(@RequestBody Learner learner ){
        return _learnerService.createLearner(learner);
    }

    @GetMapping("/learners")
    public List<Learner> getAllLearners(){
        return _learnerService.getAllLearners();
    }
}
