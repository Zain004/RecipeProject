package com.example.demo.Authentication.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// denne kilden er en del av Spring konfigurasjonen og vil
// generere objekter (beans - utstyr) som kan brukes
@Configuration
public class SecurityConfig {
    @Bean // Bean er oppskrifter på hvordan lage ting, når den er laget brukes den til andre ting
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
