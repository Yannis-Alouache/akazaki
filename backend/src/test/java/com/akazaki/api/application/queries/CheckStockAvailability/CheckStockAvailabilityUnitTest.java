package com.akazaki.api.application.queries.CheckStockAvailability;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.model.*;
import com.akazaki.api.domain.ports.in.queries.CheckStockAvailabilityQuery;
import com.akazaki.api.domain.ports.out.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Check Stock Availability Unit Tests")
class CheckStockAvailabilityUnitTest {

    @Mock
    private OrderRepository orderRepository;

    private CheckStockAvailabilityQueryHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CheckStockAvailabilityQueryHandler(orderRepository);
    }

    @Test
    @DisplayName("Should throw exception when order not found")
    void should_throw_exception_when_order_not_found() {
        // Given
        Long orderId = 1L;
        CheckStockAvailabilityQuery query = new CheckStockAvailabilityQuery(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(OrderNotFoundException.class, () -> handler.handle(query));
    }

    @Test
    @DisplayName("Should return available when all products have sufficient stock")
    void should_return_available_when_all_products_have_sufficient_stock() {
        // Given
        Long orderId = 1L;
        CheckStockAvailabilityQuery query = new CheckStockAvailabilityQuery(orderId);

        Product product1 = Product.create(1L, "Product 1", "Description", 10.0, 100, "url", List.of());
        Product product2 = Product.create(2L, "Product 2", "Description", 20.0, 50, "url", List.of());

        OrderItem item1 = OrderItem.create(5, 50.0, product1);
        OrderItem item2 = OrderItem.create(3, 60.0, product2);

        Order order = Order.builder()
                .id(orderId)
                .items(List.of(item1, item2))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        StockAvailability result = handler.handle(query);

        // Then
        assertTrue(result.isStockAvailable());
        assertEquals(orderId, result.getOrderId());
        assertTrue(result.getMissingItems().isEmpty());
    }

    @Test
    @DisplayName("Should return unavailable when some products have insufficient stock")
    void should_return_unavailable_when_some_products_have_insufficient_stock() {
        // Given
        Long orderId = 1L;
        CheckStockAvailabilityQuery query = new CheckStockAvailabilityQuery(orderId);

        Product product1 = Product.create(1L, "Product 1", "Description", 10.0, 2, "url", List.of()); // Stock insuffisant
        Product product2 = Product.create(2L, "Product 2", "Description", 20.0, 50, "url", List.of());

        OrderItem item1 = OrderItem.create(5, 50.0, product1); // Demande 5, stock 2
        OrderItem item2 = OrderItem.create(3, 60.0, product2); // Demande 3, stock 50

        Order order = Order.builder()
                .id(orderId)
                .items(List.of(item1, item2))
                .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        StockAvailability result = handler.handle(query);

        // Then
        assertFalse(result.isStockAvailable());
        assertEquals(orderId, result.getOrderId());
        assertEquals(1, result.getMissingItems().size());
        
        MissingItem missingItem = result.getMissingItems().get(0);
        assertEquals(1L, missingItem.getProductId());
        assertEquals("Product 1", missingItem.getProductName());
        assertEquals(5, missingItem.getRequestedQuantity());
        assertEquals(2, missingItem.getAvailableQuantity());
    }
} 