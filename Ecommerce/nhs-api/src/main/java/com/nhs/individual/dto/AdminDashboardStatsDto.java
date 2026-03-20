package com.nhs.individual.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardStatsDto implements Serializable {
    private final BigDecimal totalRevenue;
    private final Long totalUsers;
    private final BigDecimal revenueGrowthRate;
    private final BigDecimal usersGrowthRate;
    private final List<LowStockAlertDto> lowStockAlerts;
    private final List<ActivityFeedItemDto> activityFeed;

    public AdminDashboardStatsDto(
            BigDecimal totalRevenue,
            Long totalUsers,
            BigDecimal revenueGrowthRate,
            BigDecimal usersGrowthRate,
            List<LowStockAlertDto> lowStockAlerts,
            List<ActivityFeedItemDto> activityFeed
    ) {
        this.totalRevenue = totalRevenue;
        this.totalUsers = totalUsers;
        this.revenueGrowthRate = revenueGrowthRate;
        this.usersGrowthRate = usersGrowthRate;
        this.lowStockAlerts = lowStockAlerts;
        this.activityFeed = activityFeed;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public BigDecimal getRevenueGrowthRate() {
        return revenueGrowthRate;
    }

    public BigDecimal getRevenueGrowth() {
        return revenueGrowthRate;
    }

    public BigDecimal getUsersGrowthRate() {
        return usersGrowthRate;
    }

    public BigDecimal getUsersGrowth() {
        return usersGrowthRate;
    }

    public List<LowStockAlertDto> getLowStockAlerts() {
        return lowStockAlerts;
    }

    public List<ActivityFeedItemDto> getActivityFeed() {
        return activityFeed;
    }
}
