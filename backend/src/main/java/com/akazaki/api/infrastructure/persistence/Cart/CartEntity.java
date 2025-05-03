package com.akazaki.api.infrastructure.persistence.Cart;

import com.akazaki.api.infrastructure.persistence.CartItem.CartItemEntity;
import com.akazaki.api.infrastructure.persistence.User.UserEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "cart")
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;
}
