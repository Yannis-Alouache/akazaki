import apiClient from "./api.client";
import type { CartResponse, UpdateCartItemQuantityRequest } from "../types";
import {
    ApiValidators,
    SafeTypeAssertions,
    TypeValidator,
} from "../utils/validation";
import { errorLogger } from "../components/common/ErrorBoundary";

export const cartService = {
    async getCart(): Promise<CartResponse> {
        const response = await apiClient.get<CartResponse>("/cart");

        // Validate response
        if (!ApiValidators.validateCartResponse(response.data)) {
            errorLogger.logValidationError("Invalid cart response structure");
            throw new Error("Invalid cart data received");
        }

        return response.data;
    },

    async addToCart(productId: number): Promise<void> {
        await apiClient.post(`/cart/add/${productId}`);
    },

    async updateCartItemQuantity(
        productId: number,
        data: UpdateCartItemQuantityRequest
    ): Promise<void> {
        // Validate inputs
        if (!SafeTypeAssertions.safeToNumber(productId) || productId <= 0) {
            errorLogger.logValidationError(
                "Invalid product ID for quantity update"
            );
            throw new Error("Invalid product ID");
        }

        if (!TypeValidator.isNumber(data.quantity) || data.quantity < 0) {
            errorLogger.logValidationError("Invalid quantity for cart update");
            throw new Error("Invalid quantity");
        }

        await apiClient.put(
            `/cart/items/${productId}?quantity=${data.quantity}`
        );
    },
};
