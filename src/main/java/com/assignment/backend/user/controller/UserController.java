package com.assignment.backend.user.controller;

import com.assignment.backend.user.dto.request.UserCreateRequestDTO;
import com.assignment.backend.user.dto.request.UserRoleUpdateRequestDTO;
import com.assignment.backend.user.dto.request.UserStatusUpdateRequestDTO;
import com.assignment.backend.user.dto.response.UserResponseDTO;
import com.assignment.backend.user.model.User;
import com.assignment.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserCreateRequestDTO req
    ) {
        User created = userService.createUser(
                User.builder()
                        .name(req.getName())
                        .email(req.getEmail())
                        .role(req.getRole())
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(userService.getUserById(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateRequestDTO req
    ) {
        return ResponseEntity.ok(
                toResponse(userService.updateUserStatus(id, req.getStatus()))
        );
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequestDTO req
    ) {
        return ResponseEntity.ok(
                toResponse(userService.updateUserRole(id, req.getRole()))
        );
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
