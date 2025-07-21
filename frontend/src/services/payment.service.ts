import apiClient from "./api.client";
import type {
    CreatePaymentIntentRequest,
    PaymentIntentResponse,
} from "../types";

export const paymentService = {
    async createPaymentIntent(
        data: CreatePaymentIntentRequest
    ): Promise<PaymentIntentResponse> {
        const response = await apiClient.post<PaymentIntentResponse>(
            "/payments/create-payment-intent",
            data
        );
        return response.data;
    },
};
