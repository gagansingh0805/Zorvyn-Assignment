package com.assignment.backend.dashboard.dto.response;

import lombok.Data;

@Data
public class TrendPointDTO {
    private String label;
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
}
