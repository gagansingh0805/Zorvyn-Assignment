package com.assignment.backend.user.dto.request;

import com.assignment.backend.user.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleUpdateRequestDTO {
    @NotNull(message = "Role is required")
    private Role role;
}
