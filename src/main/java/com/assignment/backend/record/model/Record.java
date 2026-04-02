package com.assignment.backend.record.model;


import com.assignment.backend.record.enums.RecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "records")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private RecordType type;

    private String category;

    private LocalDate date;

    private String notes;

    private Long userId;

    private LocalDateTime createdAt;
}
