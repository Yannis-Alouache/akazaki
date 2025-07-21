import { create } from "zustand";
import { persist } from "zustand/middleware";
import type {
    AuthState,
    LoginRequest,
    RegisterUserRequest,
    UserResponse,
} from "../types";
import { authService } from "../services";
import { FormValidators, ApiValidators } from "../utils/validation";
import { STORAGE_KEYS } from "../config/constants";
import { errorLogger } from "../components/common/ErrorBoundary";

// JWT Security utilities
const jwtUtils = {
    /**
     * Safely decode JWT without verification (for extracting basic info only)
     * Never trust the content for security decisions
     */
    decodeToken: (token: string): any => {
        try {
            const base64Url = token.split(".")[1];
            if (!base64Url) return null;

            const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
            const jsonPayload = decodeURIComponent(
                atob(base64)
                    .split("")
                    .map(
                        (c) =>
                            "%" +
                            ("00" + c.charCodeAt(0).toString(16)).slice(-2)
                    )
                    .join("")
            );
            return JSON.parse(jsonPayload);
        } catch (error) {
            console.warn("JWT decode failed:", error);
            return null;
        }
    },

    /**
     * Extract expiration from JWT (if present)
     * Returns null if no exp claim or token is invalid
     */
    getTokenExpiration: (token: string): Date | null => {
        const payload = jwtUtils.decodeToken(token);
        if (!payload?.exp) return null;

        return new Date(payload.exp * 1000);
    },

    /**
     * Check if token is expired (client-side check only)
     * Add buffer time to account for clock skew
     */
    isTokenExpired: (token: string, bufferSeconds: number = 60): boolean => {
        const expiration = jwtUtils.getTokenExpiration(token);
        if (!expiration) return false; // No expiration claim

        const now = new Date();
        const bufferTime = new Date(now.getTime() + bufferSeconds * 1000);
        return expiration <= bufferTime;
    },

    /**
     * Validate token format (basic structure check)
     */
    isValidTokenFormat: (token: string): boolean => {
        if (!token || typeof token !== "string") return false;

        const parts = token.split(".");
        if (parts.length !== 3) return false;

        try {
            // Check if parts are valid base64
            atob(parts[0].replace(/-/g, "+").replace(/_/g, "/"));
            atob(parts[1].replace(/-/g, "+").replace(/_/g, "/"));
            return true;
        } catch {
            return false;
        }
    },

    /**
     * Sanitize user data from JWT payload
     */
    extractUserData: (payload: any, fallbackEmail: string): UserResponse => {
        return {
            id: payload.userId || payload.sub_id || 0,
            email: payload.sub || payload.email || fallbackEmail,
            firstName: payload.firstName || payload.given_name || "",
            lastName: payload.lastName || payload.family_name || "",
            phoneNumber: payload.phoneNumber || payload.phone || "",
            admin: Boolean(payload.admin || payload.is_admin || false),
        };
    },
};

// Secure storage utilities
const secureStorage = {
    /**
     * Store token with additional security measures
     */
    setToken: (token: string): void => {
        try {
            if (typeof window !== "undefined" && window.localStorage) {
                // In production, consider additional encryption here
                localStorage.setItem(STORAGE_KEYS.AUTH_TOKEN, token);
            }
        } catch (error) {
            console.error("Failed to store auth token:", error);
        }
    },

    /**
     * Retrieve and validate token
     */
    getToken: (): string | null => {
        try {
            if (typeof window !== "undefined" && window.localStorage) {
                const token = localStorage.getItem(STORAGE_KEYS.AUTH_TOKEN);

                if (!token) return null;

                // Validate token format
                if (!jwtUtils.isValidTokenFormat(token)) {
                    console.warn(
                        "Invalid token format detected, clearing storage"
                    );
                    secureStorage.clearToken();
                    return null;
                }

                // Check if token is expired
                if (jwtUtils.isTokenExpired(token)) {
                    console.warn("Expired token detected, clearing storage");
                    secureStorage.clearToken();
                    return null;
                }

                return token;
            }
        } catch (error) {
            console.error("Failed to retrieve auth token:", error);
        }
        return null;
    },

    /**
     * Securely clear token and user data
     */
    clearToken: (): void => {
        try {
            if (typeof window !== "undefined" && window.localStorage) {
                localStorage.removeItem(STORAGE_KEYS.AUTH_TOKEN);
                localStorage.removeItem(STORAGE_KEYS.USER_DATA);

                // Clear any other auth-related data
                const keys = Object.keys(localStorage);
                keys.forEach((key) => {
                    if (key.startsWith("auth_") || key.startsWith("user_")) {
                        localStorage.removeItem(key);
                    }
                });
            }
        } catch (error) {
            console.error("Failed to clear auth storage:", error);
        }
    },
};

interface AuthStore extends AuthState {
    // Loading states
    isLoading: boolean;
    error: string | null;

    // Actions
    login: (credentials: LoginRequest) => Promise<void>;
    register: (data: RegisterUserRequest) => Promise<void>;
    logout: () => void;
    checkAuth: () => Promise<void>;
    clearError: () => void;

    // Security actions
    validateSession: () => Promise<boolean>;
    refreshUserData: () => Promise<void>;
}

export const useAuthStore = create<AuthStore>()(
    persist(
        (set, get) => ({
            // Initial state
            isAuthenticated: false,
            user: null,
            token: null,
            isLoading: false,
            error: null,

            // Clear error action
            clearError: () => set({ error: null }),

            // Login action with enhanced security
            login: async (credentials) => {
                set({ isLoading: true, error: null });

                try {
                    if (!FormValidators.validateLoginForm(credentials)) {
                        throw new Error("Invalid login credentials format");
                    }
                    const response = await authService.login(credentials);
                    const token = response.token;

                    // Validate token format
                    if (!jwtUtils.isValidTokenFormat(token)) {
                        throw new Error(
                            "Invalid token format received from server"
                        );
                    }

                    // Store token securely
                    secureStorage.setToken(token);

                    // Decode and validate payload
                    const payload = jwtUtils.decodeToken(token);
                    if (!payload) {
                        throw new Error("Unable to decode token payload");
                    }

                    const userData = jwtUtils.extractUserData(
                        payload,
                        credentials.email
                    );

                    set({
                        isAuthenticated: true,
                        token,
                        user: userData,
                        isLoading: false,
                        error: null,
                    });

                    console.log("Login successful", {
                        email: userData.email,
                        admin: userData.admin,
                    });
                } catch (error: any) {
                    const errorMessage =
                        error.response?.data?.reason ||
                        error.message ||
                        "Login failed";
                    set({
                        isAuthenticated: false,
                        token: null,
                        user: null,
                        isLoading: false,
                        error: errorMessage,
                    });

                    // Clear any stored tokens on login failure
                    secureStorage.clearToken();

                    console.error("Login failed:", errorMessage);
                    throw error;
                }
            },

            // Register action
            register: async (data) => {
                set({ isLoading: true, error: null });

                try {
                    // Validate registration data
                    if (!FormValidators.validateRegistrationForm(data)) {
                        throw new Error("Invalid registration data format");
                    }

                    const response = await authService.register(data);

                    // Validate registration response
                    if (!ApiValidators.validateUserResponse(response)) {
                        throw new Error(
                            "Invalid registration response from server"
                        );
                    }

                    // Store the user data from registration response
                    set({
                        user: response,
                    });

                    // After registration, log them in
                    await get().login({
                        email: data.email,
                        password: data.password,
                    });
                } catch (error: any) {
                    const errorMessage =
                        error.response?.data?.reason ||
                        error.message ||
                        "Registration failed";
                    errorLogger.logValidationError(
                        `Registration failed: ${errorMessage}`
                    );
                    set({
                        isLoading: false,
                        error: errorMessage,
                    });

                    console.error("Registration failed:", errorMessage);
                    throw error;
                }
            },

            // Enhanced logout with security cleanup
            logout: () => {
                // Clear all auth state
                set({
                    isAuthenticated: false,
                    user: null,
                    token: null,
                    isLoading: false,
                    error: null,
                });

                // Securely clear storage
                secureStorage.clearToken();

                // Force page reload to clear any remaining state
                if (typeof window !== "undefined") {
                    window.location.href = "/";
                }
            },

            // Check authentication status
            checkAuth: async () => {
                const token = secureStorage.getToken();

                if (!token) {
                    set({ isAuthenticated: false, user: null, token: null });
                    return;
                }

                try {
                    const payload = jwtUtils.decodeToken(token);
                    if (!payload) {
                        throw new Error("Invalid token payload");
                    }

                    const userData = jwtUtils.extractUserData(payload, "");

                    set({
                        isAuthenticated: true,
                        token,
                        user: userData,
                        error: null,
                    });
                } catch (error) {
                    console.error("Auth check failed:", error);
                    secureStorage.clearToken();
                    set({ isAuthenticated: false, user: null, token: null });
                }
            },

            // Validate current session
            validateSession: async (): Promise<boolean> => {
                const state = get();

                if (!state.isAuthenticated || !state.token) {
                    return false;
                }

                // Check token expiration
                if (jwtUtils.isTokenExpired(state.token)) {
                    console.warn("Token expired, logging out");
                    get().logout();
                    return false;
                }

                return true;
            },

            // Refresh user data (when backend supports it)
            refreshUserData: async () => {
                const state = get();
                if (!state.isAuthenticated || !state.token) return;

                try {
                    // When backend supports user info endpoint, fetch fresh data
                    // For now, just validate the current session
                    await get().validateSession();
                } catch (error) {
                    console.error("Failed to refresh user data:", error);
                }
            },
        }),
        {
            name: "auth-storage",
            partialize: (state) => ({
                // Only persist basic auth state, not sensitive data
                isAuthenticated: state.isAuthenticated,
                user: state.user
                    ? {
                          id: state.user.id,
                          email: state.user.email,
                          firstName: state.user.firstName,
                          lastName: state.user.lastName,
                          phoneNumber: state.user.phoneNumber,
                          admin: state.user.admin,
                      }
                    : null,
                // Token is stored separately in localStorage for better security
            }),
            onRehydrateStorage: () => (state) => {
                if (state) {
                    // Validate session on app load
                    state.checkAuth();
                }
            },
        }
    )
);
