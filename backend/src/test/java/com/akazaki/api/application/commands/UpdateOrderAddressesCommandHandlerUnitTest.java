package com.akazaki.api.application.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.akazaki.api.domain.exceptions.OrderNotFoundException;
import com.akazaki.api.domain.exceptions.UnauthorizedAccessException;
import com.akazaki.api.domain.model.Address;
import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderStatus;
import com.akazaki.api.domain.model.User;
import com.akazaki.api.domain.ports.in.commands.UpdateOrderAddressesCommand;
import com.akazaki.api.domain.ports.out.OrderRepository;
// TODO: check que ça c'est bon
@DisplayName("Update Order Addresses Unit Tests")
public class UpdateOrderAddressesCommandHandlerUnitTest {
    
    private UpdateOrderAddressesCommandHandler handler;
    private OrderRepository orderRepository;
    
    private User testUser;
    private User otherUser;
    private Order testOrder;
    private Address billingAddress;
    private Address shippingAddress;
    
    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        handler = new UpdateOrderAddressesCommandHandler(orderRepository);
        
        // Créer des utilisateurs de test
        testUser = User.create(
            1L, "Doe", "John", "test@example.com", 
            "password", "0123456789", false
        );
            
        otherUser = User.create(
            2L, "Smith", "Jane", "other@example.com", 
            "password", "0987654321", false
        );
        
        // Créer une commande de test
        testOrder = Order.builder()
            .id(1L)
            .user(testUser)
            .date(LocalDateTime.now())
            .status(OrderStatus.PENDING)
            .items(List.of())
            .totalPrice(100.0)
            .build();
        
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
            1L, billingAddress, shippingAddress, 1L
        );
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        
        Order updatedOrder = Order.builder()
            .id(testOrder.getId())
            .user(testOrder.getUser())
            .date(testOrder.getDate())
            .status(testOrder.getStatus())
            .items(testOrder.getItems())
            .totalPrice(testOrder.getTotalPrice())
            .billingAddress(billingAddress)
            .shippingAddress(shippingAddress)
            .build();
            
        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);
        
        // When
        Order result = handler.handle(command);
        
        // Then
        assertThat(result.getBillingAddress()).isEqualTo(billingAddress);
        assertThat(result.getShippingAddress()).isEqualTo(shippingAddress);
        assertThat(result.getId()).isEqualTo(testOrder.getId());
        assertThat(result.getUser()).isEqualTo(testOrder.getUser());
        
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(updatedOrder);
    }
    
    @Test
    @DisplayName("Should throw OrderNotFoundException when order does not exist")
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExist() {
        // Given
        UpdateOrderAddressesCommand command = new UpdateOrderAddressesCommand(
            999L, billingAddress, shippingAddress, 1L
        );
        
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(OrderNotFoundException.class, () -> {
            handler.handle(command);
        });
        
        verify(orderRepository).findById(999L);
    }
    
    @Test
    @DisplayName("Should throw UnauthorizedAccessException when user is not order owner")
    void shouldThrowUnauthorizedAccessExceptionWhenUserIsNotOrderOwner() {
        // Given
        UpdateOrderAddressesCommand command = new UpdateOrderAddressesCommand(
            1L, billingAddress, shippingAddress, 2L // Different user ID
        );
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        
        // When & Then
        assertThrows(UnauthorizedAccessException.class, () -> {
            handler.handle(command);
        });
        
        verify(orderRepository).findById(1L);
    }
} 