package com.assignment.backend.auth.config;

import com.assignment.backend.auth.filter.HeaderUserAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final HeaderUserAuthenticationFilter headerUserAuthenticationFilter;

    public SecurityConfig(HeaderUserAuthenticationFilter headerUserAuthenticationFilter) {
        this.headerUserAuthenticationFilter = headerUserAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity h) throws Exception {
        h
                .csrf(c -> c.disable())
                .addFilterBefore(headerUserAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/health").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/records").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/records/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/records/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/records/**").hasAnyRole("ANALYST", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/dashboard/**").hasAnyRole("VIEWER", "ANALYST", "ADMIN")
                        .anyRequest().authenticated()
                );
        return h.build();
    }
}
