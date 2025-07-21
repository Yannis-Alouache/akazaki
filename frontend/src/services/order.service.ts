import apiClient from "./api.client";
import type {
    OrderResponse,
    UpdateOrderAddressesRequest,
    StockAvailabilityResponse,
} from "../types";

export const orderService = {
    async createOrder(): Promise<OrderResponse> {
        const response = await apiClient.post<OrderResponse>("/orders");
        return response.data;
    },

    async updateOrderAddresses(
        orderId: number,
        addresses: UpdateOrderAddressesRequest
    ): Promise<OrderResponse> {
        const response = await apiClient.put<OrderResponse>(
            `/orders/${orderId}/address`,
            addresses
        );
        return response.data;
    },

    async checkStockAvailability(
        orderId: number
    ): Promise<StockAvailabilityResponse> {
        const response = await apiClient.get<StockAvailabilityResponse>(
            `/orders/${orderId}/stock-availability`
        );
        return response.data;
    },
};
