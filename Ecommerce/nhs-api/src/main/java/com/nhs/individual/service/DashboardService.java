package com.nhs.individual.service;

import com.nhs.individual.constant.OrderStatus;
import com.nhs.individual.dto.ActivityFeedItemDto;
import com.nhs.individual.dto.AdminDashboardStatsDto;
import com.nhs.individual.dto.LowStockAlertDto;
import com.nhs.individual.repository.ShopOrderRepository;
import com.nhs.individual.repository.UserRepository;
import com.nhs.individual.repository.WarehouseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ShopOrderRepository shopOrderRepository;
    private final UserRepository userRepository;
        private final WarehouseItemRepository warehouseItemRepository;

        private static final int LOW_STOCK_THRESHOLD = 10;
        private static final int ACTIVITY_LIMIT = 5;

    public AdminDashboardStatsDto getAdminStats() {
        LocalDate now = LocalDate.now();

        LocalDate currentMonthStart = now.withDayOfMonth(1);
        LocalDate nextMonthStart = currentMonthStart.plusMonths(1);
        LocalDate previousMonthStart = currentMonthStart.minusMonths(1);

        Date currentMonthStartDate = asDate(currentMonthStart);
        Date nextMonthStartDate = asDate(nextMonthStart);
        Date previousMonthStartDate = asDate(previousMonthStart);

        BigDecimal totalRevenue = nonNull(
                shopOrderRepository.sumCompletedRevenue(OrderStatus.COMPLETED.id)
        );

        BigDecimal currentMonthRevenue = nonNull(
                shopOrderRepository.sumCompletedRevenueBetween(
                        currentMonthStartDate,
                        nextMonthStartDate,
                        OrderStatus.COMPLETED.id
                )
        );

        BigDecimal previousMonthRevenue = nonNull(
                shopOrderRepository.sumCompletedRevenueBetween(
                        previousMonthStartDate,
                        currentMonthStartDate,
                        OrderStatus.COMPLETED.id
                )
        );

        long totalUsers = userRepository.count();

        long currentMonthUsers = nonNull(
                userRepository.countRegisteredUsersBetween(
                        currentMonthStartDate,
                        nextMonthStartDate
                )
        );

        long previousMonthUsers = nonNull(
                userRepository.countRegisteredUsersBetween(
                        previousMonthStartDate,
                        currentMonthStartDate
                )
        );

                List<LowStockAlertDto> lowStockAlerts = warehouseItemRepository
                                .findLowStockProducts(LOW_STOCK_THRESHOLD)
                                .stream()
                                .map(item -> new LowStockAlertDto(item.getProductName(), nonNull(item.getCurrentStock())))
                                .toList();

                List<ActivityFeedItemDto> activityFeed = buildRecentActivityFeed();

        return new AdminDashboardStatsDto(
                totalRevenue,
                totalUsers,
                calculateGrowthPercent(currentMonthRevenue, previousMonthRevenue),
                                calculateGrowthPercent(BigDecimal.valueOf(currentMonthUsers), BigDecimal.valueOf(previousMonthUsers)),
                                lowStockAlerts,
                                activityFeed
        );
    }

        private List<ActivityFeedItemDto> buildRecentActivityFeed() {
                List<ActivityFeedItemDto> events = new ArrayList<>();

                shopOrderRepository.findRecentOrderActivities(ACTIVITY_LIMIT).forEach(order -> {
                        Instant createdAt = asInstant(order.getCreatedAt());
                        if (createdAt == null) {
                                return;
                        }

                        String displayName = fullName(order.getFirstName(), order.getLastName());
                        String message = "New order #" + order.getOrderId() + " placed by " + displayName;
                        events.add(new ActivityFeedItemDto(message, createdAt));
                });

                userRepository.findRecentUserActivities(ACTIVITY_LIMIT).forEach(user -> {
                        Instant createdAt = asInstant(user.getCreatedAt());
                        if (createdAt == null) {
                                return;
                        }

                        String displayName = fullName(user.getFirstName(), user.getLastName());
                        String message = displayName + " just registered";
                        events.add(new ActivityFeedItemDto(message, createdAt));
                });

                return events.stream()
                                .sorted(Comparator.comparing(ActivityFeedItemDto::getCreatedAt).reversed())
                                .limit(ACTIVITY_LIMIT)
                                .toList();
        }

    private Date asDate(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

        private Instant asInstant(Date date) {
                return date == null ? null : date.toInstant();
        }

        private String fullName(String firstName, String lastName) {
                String full = ((firstName == null ? "" : firstName.trim()) + " " + (lastName == null ? "" : lastName.trim())).trim();
                return full.isBlank() ? "Unknown user" : full;
        }

    private BigDecimal calculateGrowthPercent(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0
                    ? BigDecimal.valueOf(100)
                    : BigDecimal.ZERO;
        }

        return current.subtract(previous)
                .divide(previous, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal nonNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

        private Integer nonNull(Integer value) {
                return value == null ? 0 : value;
        }

    private Long nonNull(Long value) {
        return value == null ? 0L : value;
    }
}
