package com.example.demo.CommonItems.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
    @Bean(name = "authenticationSecurityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF (ONLY for local testing - REMOVE IN PRODUCTION)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Allow H2-console access
                        .anyRequest().permitAll()  //  Allow all other requests - ADJUST IN PRODUCTION
                )
                .headers((headers) -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())  // Disable frame options to allow H2 console
                );

        return http.build();
    }
*/
    @Bean(name = "authenticationSecurityFilterChain")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF (ONLY for local testing)
                // jeg har deaktivert csrf for ellers vil ikke innsending ac skjema funke
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // Tillat H2-konsollen (KUN i dev!)
                        .anyRequest().permitAll() //La til en kommentar
                )
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.DENY))
                        .addHeaderWriter((request, response) -> {
                            response.setHeader("Report-To", "{ \"group\": \"csp-endpoint\", \"max_age\": 10886400, \"endpoints\": [ { \"url\": \"/csp-report-endpoint\" } ] }");
                            response.setHeader("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains; preload");
                            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
                            response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                            response.setHeader("Permissions-Policy", "geolocation=(), microphone=(), camera=(), payment=(), usb=(), gyroscope=(), magnetometer=(), picture-in-picture=(), display-capture=(), fullscreen=(self)");
                        })
                );

        return http.build();
    }



    @Bean // Bean er oppskrifter på hvordan lage ting, når den er laget brukes den til andre ting
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
