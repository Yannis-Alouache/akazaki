import apiClient from "./api.client";
import type { CategoryResponse, CreateCategoryRequest } from "../types";

export const categoryService = {
    async getCategories(): Promise<CategoryResponse[]> {
        const response = await apiClient.get<CategoryResponse[]>(
            "/admin/categories"
        );
        return response.data;
    },

    async createCategory(
        data: CreateCategoryRequest
    ): Promise<CategoryResponse> {
        const response = await apiClient.post<CategoryResponse>(
            "/admin/categories",
            data
        );
        return response.data;
    },
};
