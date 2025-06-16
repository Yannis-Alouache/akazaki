package com.akazaki.api.infrastructure.web.mapper.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akazaki.api.domain.model.Address;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.UpdateOrderAddressesCommand;
import com.akazaki.api.infrastructure.web.dto.response.OrderResponse;
import com.akazaki.api.infrastructure.web.mapper.address.AddressResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.orderItem.OrderItemResponseMapper;
import com.akazaki.api.infrastructure.web.mapper.orderStatus.OrderStatusResponseMapper;

import com.akazaki.api.infrastructure.web.dto.request.UpdateOrderAddressesRequest;
import com.akazaki.api.infrastructure.web.dto.response.OrderItemResponse;

@Component
public class OrderResponseMapper {
    @Autowired
    private OrderItemResponseMapper orderItemMapper;

    @Autowired
    private OrderStatusResponseMapper orderStatusMapper;

    @Autowired
    private AddressResponseMapper addressMapper;


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
            addressMapper.toResponse(order.getShippingAddress())
        );
    }

    public UpdateOrderAddressesCommand toCommand(Long orderId, Long userId, UpdateOrderAddressesRequest request) {
        Address billingAddress = Address.builder()
            .lastName(request.billingAddress().lastName())
            .firstName(request.billingAddress().firstName())
            .streetNumber(request.billingAddress().streetNumber())
            .street(request.billingAddress().street())
            .addressComplement(request.billingAddress().addressComplement())
            .postalCode(request.billingAddress().postalCode())
            .city(request.billingAddress().city())
            .country(request.billingAddress().country())
            .build();

        Address shippingAddress = Address.builder()
            .lastName(request.shippingAddress().lastName())
            .firstName(request.shippingAddress().firstName())
            .streetNumber(request.shippingAddress().streetNumber())
            .street(request.shippingAddress().street())
            .addressComplement(request.shippingAddress().addressComplement())
            .postalCode(request.shippingAddress().postalCode())
            .city(request.shippingAddress().city())
            .country(request.shippingAddress().country())
            .build();

        return new UpdateOrderAddressesCommand(
            orderId,
            billingAddress,
            shippingAddress,
            userId
        );
    }
}
