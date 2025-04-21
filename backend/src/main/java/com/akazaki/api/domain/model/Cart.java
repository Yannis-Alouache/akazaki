package com.akazaki.api.domain.model;

import java.util.List;
import java.util.Objects;


public class Cart {
    private Long id;

    private User user;

    private List<CartItem> cartItems;

    public Cart(Long id, User user, List<CartItem> cartItems) {
        this.id = id;
        this.user = user;
        this.cartItems = cartItems;
    }

    public void addItem(CartItem cartItem) {
        if (updateExistingItem(cartItem)) return;
        cartItems.add(cartItem);
    }

    public boolean updateExistingItem(CartItem cartItem) {
        for (CartItem item : cartItems) {
            if (Objects.equals(item.getProduct().getId(), cartItem.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                return true;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(user, cart.user) && Objects.equals(cartItems, cart.cartItems);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user +
                ", cartItems=" + cartItems +
                '}';
    }

    public static class Builder {
        private Long id;
        private User user;
        private List<CartItem> cartItems;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder cartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Cart build() {
            return new Cart(id, user, cartItems);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
