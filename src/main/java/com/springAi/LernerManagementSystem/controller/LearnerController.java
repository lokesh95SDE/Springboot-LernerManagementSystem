package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.dto.LearnerDto;
import com.springAi.LernerManagementSystem.entity.Learner;
import com.springAi.LernerManagementSystem.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) -----------------> any where if use date formate can use this in controller

@RestController
public class LearnerController {

//    @Autowired
//    private LearnerService _learnerService;
    private final LearnerService _learnerService;
    public LearnerController(LearnerService learnerService) {
        this._learnerService = learnerService;
    }

    @PostMapping("/learners")
    public LearnerDto createLearner(@RequestBody LearnerDto learnerDto ){
        return _learnerService.createLearner(learnerDto);
    }


    @GetMapping("/learners/{learnerId}")
    public Optional<Learner> getLearnerById(@PathVariable("learnerId") Long learnerId){
        return _learnerService.findById(learnerId);
    }

    @GetMapping("/learners")
    public List<Learner> getLearnerById(@RequestParam(value="learnerName",required = false) String learnerName, @RequestParam(value="learnerEmail",required = false) String learnerEmail){
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
