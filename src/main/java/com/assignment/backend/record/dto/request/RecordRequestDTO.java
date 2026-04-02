package com.assignment.backend.record.dto.request;

import com.assignment.backend.record.enums.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RecordRequestDTO {
    @NotNull(message = "Amount is required")
    private Double amount;

    private RecordType type;

    @NotBlank(message = "Category is required")
    private String category;

    private LocalDate date;
    private String notes;
}