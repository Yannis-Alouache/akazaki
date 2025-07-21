import { create } from "zustand";
import { devtools, persist } from "zustand/middleware";
import type { CartItemResponse } from "../types";
import { cartService } from "../services";
import {
    ApiValidators,
    SafeTypeAssertions,
    TypeValidator,
} from "../utils/validation"; // ADD THIS
import { errorLogger } from "../components/common/ErrorBoundary";

interface CartStore {
    items: CartItemResponse[];
    isLoading: boolean;
    error: string | null;

    // Actions
    fetchCart: () => Promise<void>;
    addToCart: (productId: number) => Promise<void>;
    updateQuantity: (productId: number, quantity: number) => Promise<void>;
    removeFromCart: (productId: number) => Promise<void>;
    clearCart: () => void;

    // Computed values
    getTotalItems: () => number;
    getTotalPrice: () => number;
}

export const useCartStore = create<CartStore>()(
    persist(
        devtools(
            (set, get) => ({
                // Initial state
                items: [],
                isLoading: false,
                error: null,

                fetchCart: async () => {
                    set({ isLoading: true, error: null });
                    try {
                        const cart = await cartService.getCart();

                        // Validate cart response
                        if (!ApiValidators.validateCartResponse(cart)) {
                            throw new Error(
                                "Invalid cart response from server"
                            );
                        }

                        // Validate each cart item
                        const validatedItems = cart.cartItems.filter((item) => {
                            if (!item || !item.product) {
                                errorLogger.logValidationError(
                                    `Invalid cart item structure`
                                );
                                return false;
                            }

                            const isValidProduct =
                                ApiValidators.validateProductResponse(
                                    item.product
                                );
                            if (!isValidProduct) {
                                errorLogger.logValidationError(
                                    `Invalid product in cart item: ${item.id}`
                                );
                                return false;
                            }

                            return true;
                        });

                        set({ items: validatedItems, isLoading: false });
                    } catch (error) {
                        errorLogger.logNetworkError(error, "/cart");
                        set({
                            error: "Failed to fetch cart",
                            isLoading: false,
                        });
                        console.error("Fetch cart error:", error);
                    }
                },

                // Add item to cart
                addToCart: async (productId) => {
                    // Validate productId
                    if (
                        !SafeTypeAssertions.safeToNumber(productId) ||
                        productId <= 0
                    ) {
                        errorLogger.logValidationError(
                            "Invalid product ID for cart addition"
                        );
                        throw new Error("Invalid product ID");
                    }

                    set({ isLoading: true, error: null });
                    try {
                        await cartService.addToCart(productId);
                        // Refresh cart after adding
                        await get().fetchCart();
                    } catch (error) {
                        errorLogger.logNetworkError(
                            error,
                            `/cart/add/${productId}`
                        );
                        set({
                            error: "Failed to add item to cart",
                            isLoading: false,
                        });
                        console.error("Add to cart error:", error);
                        throw error;
                    }
                },

                // Update item quantity
                updateQuantity: async (productId, quantity) => {
                    // Validate inputs
                    if (
                        !SafeTypeAssertions.safeToNumber(productId) ||
                        productId <= 0
                    ) {
                        errorLogger.logValidationError(
                            "Invalid product ID for quantity update"
                        );
                        throw new Error("Invalid product ID");
                    }

                    // FIXED: Allow 0 quantity - only reject negative numbers
                    if (!TypeValidator.isNumber(quantity) || quantity < 0) {
                        errorLogger.logValidationError(
                            "Invalid quantity for cart update"
                        );
                        throw new Error("Invalid quantity");
                    }

                    set({ isLoading: true, error: null });
                    try {
                        await cartService.updateCartItemQuantity(productId, {
                            quantity,
                        });
                        await get().fetchCart();
                    } catch (error) {
                        errorLogger.logNetworkError(
                            error,
                            `/cart/items/${productId}`
                        );
                        set({
                            error: "Failed to update quantity",
                            isLoading: false,
                        });
                        console.error("Update quantity error:", error);
                        throw error;
                    }
                },

                // Remove item from cart
                removeFromCart: async (productId) => {
                    // Validate productId
                    if (
                        !SafeTypeAssertions.safeToNumber(productId) ||
                        productId <= 0
                    ) {
                        errorLogger.logValidationError(
                            "Invalid product ID for cart removal"
                        );
                        throw new Error("Invalid product ID");
                    }

                    set({ isLoading: true, error: null });
                    try {
                        // FIXED: Call updateQuantity with 0 (which should now be valid)
                        await cartService.updateCartItemQuantity(productId, {
                            quantity: 0,
                        });
                        await get().fetchCart();
                    } catch (error) {
                        errorLogger.logNetworkError(
                            error,
                            `/cart/items/${productId}`
                        );
                        set({
                            error: "Failed to remove item from cart",
                            isLoading: false,
                        });
                        console.error("Remove from cart error:", error);
                        throw error;
                    }
                },
                // Clear cart (local only, used after order)
                clearCart: () => {
                    set({ items: [], error: null });
                },

                getTotalItems: () => {
                    return get().items.reduce(
                        (sum, item) => sum + item.quantity,
                        0
                    );
                },

                // Computed: total price
                getTotalPrice: () => {
                    return get().items.reduce(
                        (sum, item) => sum + item.product.price * item.quantity,
                        0
                    );
                },
            }),
            {
                name: "cart-store",
            }
        ),
        {
            // ADD PERSIST CONFIG
            name: "cart-storage",
            partialize: (state) => ({
                items: state.items, // Only persist items, not loading states
            }),
            onRehydrateStorage: () => (state) => {
                // Optional: Refresh cart from server on rehydration
                if (state && state.items.length > 0) {
                    // Uncomment if you want to sync with server on page refresh
                    // state.fetchCart();
                }
            },
        }
    )
);
