package com.assignment.backend.dashboard.dto.response;

import com.assignment.backend.record.dto.response.RecordResponseDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardSummaryResponseDTO {
    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;
    private Map<String, Double> categoryTotals;
    private List<RecordResponseDTO> recentActivity;
    private List<TrendPointDTO> monthlyTrends;
    private List<TrendPointDTO> weeklyTrends;
}
