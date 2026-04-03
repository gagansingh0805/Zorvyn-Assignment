package com.assignment.backend.dashboard.service;

import com.assignment.backend.dashboard.dto.response.DashboardSummaryResponseDTO;
import com.assignment.backend.dashboard.dto.response.TrendPointDTO;
import com.assignment.backend.record.dto.response.RecordResponseDTO;
import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.record.mapper.RecordMapper;
import com.assignment.backend.record.model.Record;
import com.assignment.backend.record.repository.RecordRepository;
import com.assignment.backend.record.specification.RecordSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int RECENT_LIMIT = 5;
    private static final int MONTH_TREND_COUNT = 6;
    private static final int WEEK_TREND_COUNT = 8;

    private final RecordRepository recordRepository;

    public DashboardSummaryResponseDTO getSummary(LocalDate start, LocalDate end) {
        Specification<Record> specification = (root, query, cb) -> cb.conjunction();
        if (start != null && end != null) {
            specification = specification.and(RecordSpecification.dateBetween(start, end));
        }

        List<Record> records = recordRepository.findAll(specification);

        double totalIncome = 0.0;
        double totalExpense = 0.0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Record record : records) {
            if (record.getAmount() == null || record.getType() == null) {
                continue;
            }
            double amount = record.getAmount();
            if (record.getType() == RecordType.INCOME) {
                totalIncome += amount;
            } else if (record.getType() == RecordType.EXPENSE) {
                totalExpense += amount;
            }

            if (record.getCategory() != null && !record.getCategory().isBlank()) {
                double signed = record.getType() == RecordType.EXPENSE ? -amount : amount;
                categoryTotals.merge(record.getCategory(), signed, Double::sum);
            }
        }

        DashboardSummaryResponseDTO response = new DashboardSummaryResponseDTO();
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        response.setNetBalance(totalIncome - totalExpense);
        response.setCategoryTotals(categoryTotals);
        response.setRecentActivity(buildRecentActivity(records));
        response.setMonthlyTrends(buildMonthlyTrends(records));
        response.setWeeklyTrends(buildWeeklyTrends(records));
        return response;
    }

    private List<RecordResponseDTO> buildRecentActivity(List<Record> records) {
        return records.stream()
                .sorted(Comparator.comparing(this::recordSortDate).reversed())
                .limit(RECENT_LIMIT)
                .map(RecordMapper::toDTO)
                .toList();
    }

    private LocalDate recordSortDate(Record record) {
        if (record.getDate() != null) {
            return record.getDate();
        }
        if (record.getCreatedAt() != null) {
            return record.getCreatedAt().toLocalDate();
        }
        return LocalDate.MIN;
    }

    private List<TrendPointDTO> buildMonthlyTrends(List<Record> records) {
        Map<YearMonth, TrendPointDTO> bucket = new HashMap<>();
        YearMonth current = YearMonth.now();

        for (int i = 0; i < MONTH_TREND_COUNT; i++) {
            YearMonth ym = current.minusMonths(i);
            TrendPointDTO point = new TrendPointDTO();
            point.setLabel(ym.toString());
            point.setTotalIncome(0.0);
            point.setTotalExpense(0.0);
            point.setNetBalance(0.0);
            bucket.put(ym, point);
        }

        for (Record record : records) {
            LocalDate date = recordSortDate(record);
            YearMonth ym = YearMonth.from(date);
            TrendPointDTO point = bucket.get(ym);
            if (point == null || record.getAmount() == null || record.getType() == null) {
                continue;
            }
            if (record.getType() == RecordType.INCOME) {
                point.setTotalIncome(point.getTotalIncome() + record.getAmount());
            } else if (record.getType() == RecordType.EXPENSE) {
                point.setTotalExpense(point.getTotalExpense() + record.getAmount());
            }
            point.setNetBalance(point.getTotalIncome() - point.getTotalExpense());
        }

        List<TrendPointDTO> trends = new ArrayList<>();
        for (int i = MONTH_TREND_COUNT - 1; i >= 0; i--) {
            trends.add(bucket.get(current.minusMonths(i)));
        }
        return trends;
    }

    private List<TrendPointDTO> buildWeeklyTrends(List<Record> records) {
        Map<LocalDate, TrendPointDTO> bucket = new HashMap<>();
        LocalDate currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for (int i = 0; i < WEEK_TREND_COUNT; i++) {
            LocalDate weekStart = currentWeekStart.minusWeeks(i);
            TrendPointDTO point = new TrendPointDTO();
            point.setLabel(weekStart.toString());
            point.setTotalIncome(0.0);
            point.setTotalExpense(0.0);
            point.setNetBalance(0.0);
            bucket.put(weekStart, point);
        }

        for (Record record : records) {
            LocalDate date = recordSortDate(record);
            LocalDate weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            TrendPointDTO point = bucket.get(weekStart);
            if (point == null || record.getAmount() == null || record.getType() == null) {
                continue;
            }
            if (record.getType() == RecordType.INCOME) {
                point.setTotalIncome(point.getTotalIncome() + record.getAmount());
            } else if (record.getType() == RecordType.EXPENSE) {
                point.setTotalExpense(point.getTotalExpense() + record.getAmount());
            }
            point.setNetBalance(point.getTotalIncome() - point.getTotalExpense());
        }

        List<TrendPointDTO> trends = new ArrayList<>();
        for (int i = WEEK_TREND_COUNT - 1; i >= 0; i--) {
            trends.add(bucket.get(currentWeekStart.minusWeeks(i)));
        }
        return trends;
    }
}
