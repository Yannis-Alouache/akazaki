package com.akazaki.api.infrastructure.persistence.Cart;

import com.akazaki.api.domain.model.CartItem;
import com.akazaki.api.infrastructure.persistence.CartItem.CartItemEntity;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity user;

    @OneToMany
    private List<CartItemEntity> cartItems;
}
