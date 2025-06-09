package com.akazaki.api.application.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akazaki.api.config.fixtures.OrderFixture;
import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UnauthorizedAccessException;
import com.akazaki.api.domain.model.Address;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.in.commands.UpdateOrderAddressesCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.infrastructure.persistence.Order.InMemoryOrderRepository;

@DisplayName("Update Order Addresses Unit Tests")
public class UpdateOrderAddressesCommandHandlerUnitTest {
    
    private UpdateOrderAddressesCommandHandler handler;
    private OrderRepository orderRepository;
    
    private Order testOrder;
    private Address billingAddress;
    private Address shippingAddress;
    
    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        handler = new UpdateOrderAddressesCommandHandler(orderRepository);
        
        testOrder = orderRepository.save(OrderFixture.orderWithItems);
        
        // Créer des adresses de test
        billingAddress = Address.create(
            "Dupont", "Pierre", "123", "Rue de la Paix", 
            "Apt 4", "75001", "Paris", "France"
        );
        
        shippingAddress = Address.create(
            "Martin", "Marie", "456", "Avenue des Champs", 
            null, "75008", "Paris", "France"
        );
    }
    
    @Test
    @DisplayName("Should update order addresses successfully")
    void shouldUpdateOrderAddressesSuccessfully() {
        // Given
        UpdateOrderAddressesCommand command = new UpdateOrderAddressesCommand(
            testOrder.getId(), billingAddress, shippingAddress, testOrder.getUser().getId()
        );

        // When
        Order result = handler.handle(command);
        
        // Then
        assertThat(result.getBillingAddress()).isEqualTo(billingAddress);
        assertThat(result.getShippingAddress()).isEqualTo(shippingAddress);
        assertThat(result.getId()).isEqualTo(testOrder.getId());
        assertThat(result.getUser()).isEqualTo(testOrder.getUser());
    }
    
    @Test
    @DisplayName("Should throw OrderNotFoundException when order does not exist")
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExist() {
        // Given
        UpdateOrderAddressesCommand command = new UpdateOrderAddressesCommand(
            999L, billingAddress, shippingAddress, testOrder.getUser().getId()
        );
        
        // When & Then
        assertThrows(OrderNotFoundException.class, () -> {
            handler.handle(command);
        });
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedAccessException when user is not order owner")
    void shouldThrowUnauthorizedAccessExceptionWhenUserIsNotOrderOwner() {
        // Given
        UpdateOrderAddressesCommand command = new UpdateOrderAddressesCommand(
            testOrder.getId(), billingAddress, shippingAddress, 999L
        );
                
        // When & Then
        assertThrows(UnauthorizedAccessException.class, () -> {
            handler.handle(command);
        });
    }
} 