package com.springAi.LernerManagementSystem;
import com.springAi.LernerManagementSystem.entity.User;
import com.springAi.LernerManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableMethodSecurity
public class AuthenticationAutherisationConfig{

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        System.out.println("CUSTOM SECURITY CONFIG LOADED");
//        http
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers("/h2-console/**", "/Register","/Signin") // Added /Register here
//                )
////        This line tells Spring Security to disable CSRF protection only for requests to /h2-console/**. For all other POST, PUT, and DELETE requests (including your POST to /Register), CSRF protection is enabled. Since your curl command does not include a CSRF token, Spring Security rejects the request, and the ExceptionTranslationFilter then redirects the unauthenticated user to the login page.
//                .headers(headers ->
//                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/Register",
//                                "/verify",
//                                "/Signin",
//                                "/h2-console/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .defaultSuccessUrl("/", true)
//                        .permitAll()
//                );
//
//        return http.build();

        http .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/Register", "/Signin", "/verify", "/cohorts", "/h2-console/**" ).permitAll()
                        .anyRequest().authenticated() )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) .permitAll() );

        return http.build();


    }


}
