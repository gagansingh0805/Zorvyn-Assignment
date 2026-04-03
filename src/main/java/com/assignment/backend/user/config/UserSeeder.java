package com.assignment.backend.user.config;

import com.assignment.backend.user.enums.Role;
import com.assignment.backend.user.enums.UserStatus;
import com.assignment.backend.user.model.User;
import com.assignment.backend.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        List<User> users = List.of(
                User.builder()
                        .name("Admin User")
                        .email("admin@example.com")
                        .role(Role.ADMIN)
                        .status(UserStatus.ACTIVE)
                        .createdAt(now)
                        .build(),
                User.builder()
                        .name("Analyst User")
                        .email("analyst@example.com")
                        .role(Role.ANALYST)
                        .status(UserStatus.ACTIVE)
                        .createdAt(now)
                        .build(),
                User.builder()
                        .name("Viewer User")
                        .email("viewer@example.com")
                        .role(Role.VIEWER)
                        .status(UserStatus.ACTIVE)
                        .createdAt(now)
                        .build()
        );

        userRepository.saveAll(users);
    }
}
