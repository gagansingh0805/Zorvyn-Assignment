package com.assignment.backend.dashboard;

import com.assignment.backend.dashboard.dto.response.DashboardSummaryResponseDTO;
import com.assignment.backend.dashboard.service.DashboardService;
import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.record.model.Record;
import com.assignment.backend.record.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class DashboardServiceTests {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private RecordRepository recordRepository;

    @BeforeEach
    void setup() {
        recordRepository.deleteAll();
        recordRepository.save(Record.builder()
                .amount(1000.0)
                .type(RecordType.INCOME)
                .category("Salary")
                .date(LocalDate.now().minusDays(3))
                .notes("April salary")
                .build());
        recordRepository.save(Record.builder()
                .amount(200.0)
                .type(RecordType.EXPENSE)
                .category("Food")
                .date(LocalDate.now().minusDays(1))
                .notes("Groceries")
                .build());
    }

    @Test
    void summaryCalculatesTotalsAndCategoryNet() {
        DashboardSummaryResponseDTO summary = dashboardService.getSummary(null, null);

        assertThat(summary.getTotalIncome()).isEqualTo(1000.0);
        assertThat(summary.getTotalExpense()).isEqualTo(200.0);
        assertThat(summary.getNetBalance()).isEqualTo(800.0);
        assertThat(summary.getCategoryTotals().get("Salary")).isEqualTo(1000.0);
        assertThat(summary.getCategoryTotals().get("Food")).isEqualTo(-200.0);
        assertThat(summary.getRecentActivity()).hasSize(2);
    }
}
