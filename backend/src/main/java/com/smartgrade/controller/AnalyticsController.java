package com.smartgrade.controller;

import com.smartgrade.dto.AnalyticsResponse;
import com.smartgrade.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/rankings")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'CC', 'HOD')")
    public ResponseEntity<AnalyticsResponse> getRankings(
            @RequestParam(required = false) String department) {
        return ResponseEntity.ok(analyticsService.getAnalytics(department));
    }
}
