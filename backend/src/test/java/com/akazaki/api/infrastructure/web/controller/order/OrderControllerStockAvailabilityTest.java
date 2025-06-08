package com.akazaki.api.infrastructure.web.controller.order;

import com.akazaki.api.application.queries.CheckStockAvailability.CheckStockAvailabilityQueryHandler;
import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.MissingItem;
import com.akazaki.api.domain.model.StockAvailability;
import com.akazaki.api.domain.ports.in.queries.CheckStockAvailabilityQuery;
import com.akazaki.api.infrastructure.web.mapper.stockAvailability.StockAvailabilityMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerStockAvailabilityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheckStockAvailabilityQueryHandler checkStockAvailabilityQueryHandler;

    @MockBean
    private StockAvailabilityMapper stockAvailabilityMapper;

    // Mock other dependencies to avoid Spring context issues
    @MockBean
    private com.akazaki.api.application.commands.CreateOrder.CreateOrderCommandHandler createOrderCommandHandler;
    
    @MockBean
    private com.akazaki.api.application.commands.UpdateOrderAddressesCommandHandler updateOrderAddressesCommandHandler;
    
    @MockBean
    private com.akazaki.api.infrastructure.web.mapper.order.OrderResponseMapper orderMapper;

    @Test
    @WithMockUser
    void should_return_stock_available_when_all_products_have_sufficient_stock() throws Exception {
        // Given
        Long orderId = 1L;
        StockAvailability stockAvailability = StockAvailability.available(orderId);
        
        when(checkStockAvailabilityQueryHandler.handle(any(CheckStockAvailabilityQuery.class)))
                .thenReturn(stockAvailability);
        
        when(stockAvailabilityMapper.toResponse(stockAvailability))
                .thenReturn(new com.akazaki.api.infrastructure.web.dto.response.StockAvailabilityResponse(
                        1L, true, List.of()));

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("1"))
                .andExpect(jsonPath("$.isStockAvailable").value(true))
                .andExpect(jsonPath("$.missingItems").isEmpty());
    }

    @Test
    @WithMockUser
    void should_return_stock_unavailable_when_some_products_have_insufficient_stock() throws Exception {
        // Given
        Long orderId = 1L;
        MissingItem missingItem = MissingItem.create(1L, "Product 1", 5, 2);
        StockAvailability stockAvailability = StockAvailability.unavailable(orderId, List.of(missingItem));
        
        when(checkStockAvailabilityQueryHandler.handle(any(CheckStockAvailabilityQuery.class)))
                .thenReturn(stockAvailability);
        
        when(stockAvailabilityMapper.toResponse(stockAvailability))
                .thenReturn(new com.akazaki.api.infrastructure.web.dto.response.StockAvailabilityResponse(
                        1L, false, List.of(
                                new com.akazaki.api.infrastructure.web.dto.response.MissingItemResponse(
                                        1L, "Product 1", 5, 2))));

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("1"))
                .andExpect(jsonPath("$.isStockAvailable").value(false))
                .andExpect(jsonPath("$.missingItems").isArray())
                .andExpect(jsonPath("$.missingItems[0].productId").value("1"))
                .andExpect(jsonPath("$.missingItems[0].productName").value("Product 1"))
                .andExpect(jsonPath("$.missingItems[0].requestedQuantity").value(5))
                .andExpect(jsonPath("$.missingItems[0].availableQuantity").value(2));
    }

    @Test
    @WithMockUser
    void should_return_404_when_order_not_found() throws Exception {
        // Given
        Long orderId = 999L;
        when(checkStockAvailabilityQueryHandler.handle(any(CheckStockAvailabilityQuery.class)))
                .thenThrow(new OrderNotFoundException());

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_401_when_not_authenticated() throws Exception {
        // Given
        Long orderId = 1L;

        // When & Then
        mockMvc.perform(get("/api/orders/{orderId}/stock-availability", orderId))
                .andExpect(status().isUnauthorized());
    }
} 