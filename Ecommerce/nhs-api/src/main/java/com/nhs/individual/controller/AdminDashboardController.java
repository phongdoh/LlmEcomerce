package com.nhs.individual.controller;

import com.nhs.individual.dto.AdminDashboardStatsDto;
import com.nhs.individual.dto.RecentOrderRowDto;
import com.nhs.individual.dto.SalesOverviewPointDto;
import com.nhs.individual.dto.UserGrowthPointDto;
import com.nhs.individual.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/sales")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public List<SalesOverviewPointDto> getSalesOverview(
            @RequestParam(name = "from", required = false) LocalDate from,
            @RequestParam(name = "to", required = false) LocalDate to,
            @RequestParam(name = "days", required = false, defaultValue = "30") Integer days
    ) {
        return dashboardService.getSalesOverview(from, to, days);
    }

    @GetMapping("/users-growth")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public List<UserGrowthPointDto> getUsersGrowth() {
        return dashboardService.getUserGrowthCurrentMonth();
    }

    @GetMapping("/recent-orders")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    public List<RecentOrderRowDto> getRecentOrders(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return dashboardService.getRecentOrders(limit);
    }
}
