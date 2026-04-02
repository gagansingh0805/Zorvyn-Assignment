package com.assignment.backend.record.dto.response;

import com.assignment.backend.record.enums.RecordType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecordResponseDTO {
    private Long id;
    private Double amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String notes;
    private LocalDateTime createdAt;
}