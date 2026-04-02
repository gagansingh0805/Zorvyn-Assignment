package com.assignment.backend.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity h) throws Exception {
        h
                .csrf(c -> c.disable())
                .authorizeHttpRequests(a -> a.anyRequest().permitAll());
        return h.build();
    }
}