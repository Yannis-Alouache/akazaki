package com.akazaki.api.infrastructure.web.mapper.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.infrastructure.web.dto.response.OrderResponse;
import com.akazaki.api.infrastructure.web.mapper.address.AddressResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.orderItem.OrderItemResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.orderStatus.OrderStatusResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.payment.PaymentResponseMapper;
import com.akazaki.api.infrastructure.web.dto.response.OrderItemResponse;

@Component
public class OrderResponseMapper {
    @Autowired
    private OrderItemResponseMapper orderItemMapper;

    @Autowired
    private OrderStatusResponseMapper orderStatusMapper;

    @Autowired
    private AddressResponseMapper addressMapper;

    @Autowired
    private PaymentResponseMapper paymentMapper;


    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> orderItems = orderItemMapper.toResponseList(order.getItems());

        return new OrderResponse(
            order.getId(),
            order.getUser().getId(),
            order.getDate(),
            orderStatusMapper.toResponse(order.getStatus()),
            orderItems,
            order.getTotalPrice(),
            addressMapper.toResponse(order.getBillingAddress()),
            addressMapper.toResponse(order.getShippingAddress()),
            paymentMapper.toResponse(order.getPayment())
        );
    }
}
