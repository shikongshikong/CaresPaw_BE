package com.example.carespawbe.serviceImp.Admin;

import com.example.carespawbe.dto.Admin.AdminDashboardStatsResponse;
import com.example.carespawbe.dto.Admin.AdminYearTrendResponse;
import com.example.carespawbe.enums.OrderStatus;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import com.example.carespawbe.repository.Forum.ForumPostRepository;
import com.example.carespawbe.repository.Shop.OrderRepository;
import com.example.carespawbe.repository.Shop.ShopRepository;
import com.example.carespawbe.service.Admin.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements AdminStatsService {

    private final OrderRepository orderRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final ForumPostRepository forumPostRepository;

    @Override
    public AdminDashboardStatsResponse getDashboardStats() {
        LocalDate today = LocalDate.now();

        // ✅ Revenue: chỉ tính đơn COMPLETED
        Double revenue = orderRepository.sumRevenueByStatus(OrderStatus.COMPLETED.getValue());
        if (revenue == null) revenue = 0.0;

        // ✅ Orders today
        Long ordersToday = orderRepository.countByOrderCreatedAt(today);
        if (ordersToday == null) ordersToday = 0L;

        // ✅ Totals
        Long totalShops = shopRepository.count();
        Long totalUsers = userRepository.count();
        Long totalExperts = expertRepository.count();

        // ✅ Posts today (ForumPostEntity.createAt là LocalDate)
        Long postsToday = forumPostRepository.countByCreateAt(today);
        if (postsToday == null) postsToday = 0L;

        return AdminDashboardStatsResponse.builder()
                .revenue(revenue)
                .ordersToday(ordersToday)
                .totalShops(totalShops)
                .totalExperts(totalExperts)
                .totalUsers(totalUsers)
                .postsToday(postsToday)
                .build();
    }

    @Override
    public List<AdminYearTrendResponse> getYearTrend(int year) {
        List<AdminYearTrendResponse> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.plusMonths(1);

            Long orders = orderRepository.countByOrderCreatedAtBetween(start, end);
            if (orders == null) orders = 0L;

            Double revenue = orderRepository.sumRevenueCompletedBetween(
                    OrderStatus.COMPLETED.getValue(), start, end
            );
            if (revenue == null) revenue = 0.0;

            result.add(AdminYearTrendResponse.builder()
                    .month(month)
                    .orders(orders)
                    .revenue(revenue)
                    .build());
        }

        return result;
    }
}
