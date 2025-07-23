import type { CartItemResponse, UserResponse } from "./api.types";

// Re-export all types for easier imports
export * from "./api.types";

// Additional app-specific types can go here
export interface AuthState {
    isAuthenticated: boolean;
    user: UserResponse | null;
    token: string | null;
}

export interface CartState {
    items: CartItemResponse[];
    isLoading: boolean;
}
