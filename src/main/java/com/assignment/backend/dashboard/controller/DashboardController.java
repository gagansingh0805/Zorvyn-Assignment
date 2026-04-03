package com.assignment.backend.dashboard.controller;

import com.assignment.backend.dashboard.dto.response.DashboardSummaryResponseDTO;
import com.assignment.backend.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDTO> getSummary(
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    ) {
        if ((start == null) != (end == null)) {
            throw new IllegalArgumentException("Both start and end dates are required together");
        }
        if (start != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
        return ResponseEntity.ok(dashboardService.getSummary(start, end));
    }
}
