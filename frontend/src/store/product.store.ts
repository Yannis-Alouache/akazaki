import { create } from "zustand";
import type { ProductResponse, ProductListRequest } from "../types";
import { productService } from "../services";
import { ApiValidators, SafeTypeAssertions } from "../utils/validation";
import { errorLogger } from "../components/common/ErrorBoundary";

interface ProductStore {
    products: ProductResponse[];
    currentProduct: ProductResponse | null;
    isLoading: boolean;
    error: string | null;

    // Pagination
    currentPage: number;
    totalPages: number;
    totalElements: number;

    // Actions
    fetchProducts: (params?: ProductListRequest) => Promise<void>;
    fetchProductById: (id: number) => Promise<void>;
    setCurrentPage: (page: number) => void;
}

export const useProductStore = create<ProductStore>((set) => ({
    // Initial state
    products: [],
    currentProduct: null,
    isLoading: false,
    error: null,
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,

    // Fetch products list
    fetchProducts: async (params) => {
        set({ isLoading: true, error: null });
        try {
            const response = await productService.getProducts(params);

            // Validate the response structure
            if (!response || !Array.isArray(response.products)) {
                throw new Error("Invalid products response structure");
            }

            // Validate each product and filter out invalid ones
            const validatedProducts: ProductResponse[] = [];

            for (const product of response.products) {
                if (ApiValidators.validateProductResponse(product)) {
                    validatedProducts.push(product);
                } else {
                    // Safe logging without accessing potentially undefined properties
                    errorLogger.logValidationError(
                        "Invalid product data received from API"
                    );
                }
            }

            set({
                products: validatedProducts,
                currentPage: SafeTypeAssertions.safeToNumber(
                    response.pageNumber,
                    0
                ),
                totalPages: SafeTypeAssertions.safeToNumber(
                    response.totalPages,
                    0
                ),
                totalElements: SafeTypeAssertions.safeToNumber(
                    response.totalElements,
                    0
                ),
                isLoading: false,
            });
        } catch (error) {
            errorLogger.logNetworkError(error, "/products");
            set({
                error: "Failed to fetch products",
                isLoading: false,
            });
            console.error("Fetch products error:", error);
        }
    },

    // Fetch single product
    fetchProductById: async (id) => {
        set({ isLoading: true, error: null });
        try {
            const product = await productService.getProductById(id);

            // Validate product response
            if (!ApiValidators.validateProductResponse(product)) {
                throw new Error("Invalid product response from server");
            }

            set({
                currentProduct: product,
                isLoading: false,
            });
        } catch (error) {
            errorLogger.logNetworkError(error, `/products/${id}`);
            set({
                error: "Failed to fetch product",
                isLoading: false,
            });
            console.error("Fetch product error:", error);
        }
    },

    // Set current page
    setCurrentPage: (page) => {
        set({ currentPage: page });
    },
}));
