package com.akazaki.api.infrastructure.web.mapper.orderStatus;

import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.infrastructure.web.dto.response.OrderStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusResponseMapper {
    public OrderStatusResponse toResponse(OrderStatus status) {
        return switch (status) {
            case PENDING -> OrderStatusResponse.PENDING;
            case PAID -> OrderStatusResponse.PAID;
            case SHIPPED -> OrderStatusResponse.SHIPPED;
            case DELIVERED -> OrderStatusResponse.DELIVERED;
            case CANCELLED -> OrderStatusResponse.CANCELLED;
        };
    }
}
