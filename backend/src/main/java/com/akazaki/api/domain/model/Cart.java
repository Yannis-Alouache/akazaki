package com.akazaki.api.domain.model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.akazaki.api.domain.exceptions.ProductNotInCartException;


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
        if (incrementItemQuantityIfItemExists(cartItem)) return;
        cartItems.add(cartItem);
    }

    public boolean incrementItemQuantityIfItemExists(CartItem cartItem) {
        for (CartItem item : cartItems) {
            if (Objects.equals(item.getProduct().getId(), cartItem.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                return true;
            }
        }
        return false;
    }

    public void removeItem(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void updateItemQuantity(Long productId, int quantity) {
        CartItem cartItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(ProductNotInCartException::new);

        cartItem.setQuantity(quantity);
    }

    public double getTotalPrice() {
        double total = cartItems.stream()
                .map(item -> item.getProduct().getPrice() * item.getQuantity())
                .reduce(0.0, (a, b) -> a + b);
                
        return Math.round(total * 100.0) / 100.0;
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

    public Cart copy() {
        return new Cart(id, user.copy(), cartItems.stream().map(CartItem::copy).collect(Collectors.toList()));
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
