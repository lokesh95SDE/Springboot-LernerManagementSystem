package com.springAi.LernerManagementSystem.controller;

import com.springAi.LernerManagementSystem.entity.User;
import com.springAi.LernerManagementSystem.service.AuthenticaitonAutherizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

@Autowired
private AuthenticaitonAutherizationService authenticaitonAutherizationService;


@PostMapping("/Register")
    public User registerUser(@RequestBody User user) throws Exception {
        User persistedUser = authenticaitonAutherizationService.RegisterUser(user);
        String token = UUID.randomUUID().toString();
        String verificationUrl = "http://localhost:1001/verify?token=" + token;
        System.out.println(verificationUrl);
        authenticaitonAutherizationService.saveVerificationToken(token, persistedUser);
        return persistedUser;
    }


    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        return authenticaitonAutherizationService.verifyUser(token);
    }

}
