package com.nhs.individual.controller;

import com.nhs.individual.dto.AdminDashboardStatsDto;
import com.nhs.individual.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/admin/dashboard", "/admin/dashboard"})
@RequiredArgsConstructor
public class AdminDashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public AdminDashboardStatsDto getDashboardStats() {
        return dashboardService.getAdminStats();
    }
}
