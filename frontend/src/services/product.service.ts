import apiClient from "./api.client";
import type {
    ProductResponse,
    ProductListRequest,
    ProductListResponse,
    CreateProductRequest,
} from "../types";
import { ApiValidators, SafeTypeAssertions } from "../utils/validation"; // ADD THIS
import { errorLogger } from "../components/common/ErrorBoundary";

export const productService = {
    async getProducts(
        params?: ProductListRequest
    ): Promise<ProductListResponse> {
        const response = await apiClient.get<ProductListResponse>("/products", {
            params,
        });

        // Validate response structure
        if (!response.data || !Array.isArray(response.data.products)) {
            errorLogger.logValidationError(
                "Invalid products list response structure"
            );
            throw new Error("Invalid response format");
        }

        return response.data;
    },

    async getProductById(id: number): Promise<ProductResponse> {
        // Validate input
        if (!SafeTypeAssertions.safeToNumber(id) || id <= 0) {
            errorLogger.logValidationError("Invalid product ID provided");
            throw new Error("Invalid product ID");
        }

        const response = await apiClient.get<ProductResponse>(
            `/products/${id}`
        );

        // Validate response
        if (!ApiValidators.validateProductResponse(response.data)) {
            errorLogger.logValidationError(
                `Invalid product response for ID: ${id}`
            );
            throw new Error("Invalid product data received");
        }

        return response.data;
    },

    async createProduct(data: CreateProductRequest): Promise<ProductResponse> {
        const formData = new FormData();
        formData.append("name", data.name);
        formData.append("description", data.description);
        formData.append("price", data.price.toString());
        formData.append("stock", data.stock.toString());
        formData.append("file", data.file);
        data.categoryIds.forEach((id) =>
            formData.append("categoryIds", id.toString())
        );

        const response = await apiClient.post<ProductResponse>(
            "/admin/products",
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        return response.data;
    },

    async deleteProduct(id: number): Promise<void> {
        await apiClient.delete(`/admin/products/${id}`);
    },
};
