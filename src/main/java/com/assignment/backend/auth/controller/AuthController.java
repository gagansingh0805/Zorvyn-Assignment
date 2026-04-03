package com.assignment.backend.auth.controller;

import com.assignment.backend.auth.dto.request.RegisterRequestDTO;
import com.assignment.backend.user.dto.response.UserResponseDTO;
import com.assignment.backend.user.enums.Role;
import com.assignment.backend.user.model.User;
import com.assignment.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        User created = userService.createUser(
                User.builder()
                        .name(req.getName())
                        .email(req.getEmail())
                        .role(Role.VIEWER)
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    private UserResponseDTO toResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
