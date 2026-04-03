package com.assignment.backend.user.dto.request;

import com.assignment.backend.user.enums.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusUpdateRequestDTO {
    @NotNull(message = "Status is required")
    private UserStatus status;
}
