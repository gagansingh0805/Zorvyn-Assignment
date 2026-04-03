package com.assignment.backend.user.dto.response;

import com.assignment.backend.user.enums.Role;
import com.assignment.backend.user.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
