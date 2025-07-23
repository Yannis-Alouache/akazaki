import apiClient from "./api.client";
import type { UserResponse } from "../types";

export const userService = {
    async getAllUsers(): Promise<UserResponse[]> {
        const response = await apiClient.get<UserResponse[]>("/users");
        return response.data;
    },

    async getUserById(id: number): Promise<UserResponse> {
        const response = await apiClient.get<UserResponse>(`/users/${id}`);
        return response.data;
    },

    async getCurrentUser(): Promise<UserResponse> {
        // First, we need to decode the token to get the user ID
        const token = localStorage.getItem("akazaki_auth_token");
        if (!token) throw new Error("No token found");

        // Decode token to get user ID (from the token payload)
        const base64Url = token.split(".")[1];
        const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
        const payload = JSON.parse(atob(base64));

        // Try different possible fields for user ID in the token
        const userId = payload.sub || payload.userId || payload.id;

        if (!userId) throw new Error("No user ID in token");

        return await this.getUserById(userId);
    },
};
