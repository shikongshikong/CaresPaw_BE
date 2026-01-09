package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.UnitsTimelineResponse;
import com.example.carespawbe.repository.Shop.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.carespawbe.enums.OrderStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class UnitsTimelineService {

    private final OrderItemRepository orderItemRepository;

    // TODO: đổi đúng status COMPLETED trong ShopOrderStatus của bạn
    private static final int SHOP_ORDER_COMPLETED = 2;

    public List<UnitsTimelineResponse> getUnitsTimeline(Long shopId, int monthsBack, int monthsForward) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1).minusMonths(monthsBack);
        LocalDate end = now.withDayOfMonth(1).plusMonths(monthsForward + 1);

        // query DB: chỉ ra months có bán
        List<Object[]> rows = orderItemRepository.unitsByMonthForShopCompleted(shopId, 4, start, end);

        Map<String, Long> map = new HashMap<>();
        for (Object[] r : rows) {
            int y = ((Number) r[0]).intValue();
            int m = ((Number) r[1]).intValue();
            long units = ((Number) r[2]).longValue();
            map.put(y + "-" + String.format("%02d", m), units);
        }

        // fill monthsBack..0 (past->current)
        List<UnitsTimelineResponse> out = new ArrayList<>();
        for (int d = -monthsBack; d <= 0; d++) {
            LocalDate t = now.withDayOfMonth(1).plusMonths(d);
            int y = t.getYear();
            int m = t.getMonthValue();
            String key = y + "-" + String.format("%02d", m);
            out.add(new UnitsTimelineResponse(y, m, map.getOrDefault(key, 0L)));
        }
        return out;
    }

}
