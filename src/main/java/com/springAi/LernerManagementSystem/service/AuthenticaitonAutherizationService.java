package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.entity.User;
import com.springAi.LernerManagementSystem.entity.VerificationToken;
import com.springAi.LernerManagementSystem.repository.UserRepository;
import com.springAi.LernerManagementSystem.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticaitonAutherizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    public User RegisterUser(User user) throws Exception {
        Optional<User> existingUser = userRepository.findByEmailId(user.getEmailId());
        if (existingUser.isPresent()) {
            throw new Exception("User with email " + user.getEmailId() + " already exists.");

        }
        String hashedpass = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashedpass);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =  userRepository.findByUserName(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        User fetchDetails = user.get();
        return org.springframework.security.core.userdetails.User.builder()
                .username(fetchDetails.getUserName())
                .password(fetchDetails.getPassword())
                .roles(fetchDetails.getRole())
                .disabled(false)
                .build();
    }

    public void saveVerificationToken(String token, User persistedUser) {
        VerificationToken verificationToken =  new VerificationToken();
        verificationToken.setUser(persistedUser);
        verificationToken.setToken(token);
        verificationToken.setExpiryAt(new java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // Token valid for 24 hours
        // Save the token to the database (you'll need a repository for this)
        // Assuming you have a VerificationTokenRepository, you would do something like:
        verificationTokenRepository.save(verificationToken);
    }

    public String verifyUser(String token) {
        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);
        if (!optionalToken.isPresent()) {
            return "Verification failed , incorrect token";
        }
        VerificationToken regesitedToken = optionalToken.get();
        if(regesitedToken.getExpiryAt().before(new java.util.Date())){
            verificationTokenRepository.delete(regesitedToken);
            return "verification token got expired please re register";
        }
        User user = regesitedToken.getUser();
        user.setEnabled(true);
        verificationTokenRepository.delete(regesitedToken);
        userRepository.save(user);
        return "Verification is successful";
    }

}
