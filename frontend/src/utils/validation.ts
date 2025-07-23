import { errorLogger } from "../components/common/ErrorBoundary";

// Runtime type validation utilities
export class TypeValidator {
    /**
     * Validates if value is a string
     */
    static isString(value: unknown): value is string {
        return typeof value === "string";
    }

    /**
     * Validates if value is a number
     */
    static isNumber(value: unknown): value is number {
        return typeof value === "number" && !isNaN(value) && isFinite(value);
    }

    /**
     * Validates if value is a boolean
     */
    static isBoolean(value: unknown): value is boolean {
        return typeof value === "boolean";
    }

    /**
     * Validates if value is an object (non-null, non-array)
     */
    static isObject(value: unknown): value is Record<string, unknown> {
        return (
            value !== null && typeof value === "object" && !Array.isArray(value)
        );
    }

    /**
     * Validates if value is an array
     */
    static isArray(value: unknown): value is unknown[] {
        return Array.isArray(value);
    }

    /**
     * Validates if value is a valid email
     */
    static isEmail(value: unknown): value is string {
        if (!this.isString(value)) return false;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(value);
    }

    /**
     * Validates if value is a valid phone number (basic)
     */
    static isPhoneNumber(value: unknown): value is string {
        if (!this.isString(value)) return false;
        const phoneRegex = /^\+?[\d\s\-\(\)]{10,}$/;
        return phoneRegex.test(value);
    }

    /**
     * Validates if value is a valid postal code (5 digits)
     */
    static isPostalCode(value: unknown): value is string {
        if (!this.isString(value)) return false;
        const postalRegex = /^[0-9]{5}$/;
        return postalRegex.test(value);
    }

    /**
     * Validates if value is a non-empty string
     */
    static isNonEmptyString(value: unknown): value is string {
        return this.isString(value) && value.trim().length > 0;
    }

    /**
     * Validates if value is a positive number
     */
    static isPositiveNumber(value: unknown): value is number {
        return this.isNumber(value) && value > 0;
    }

    /**
     * Validates if value is a non-negative number
     */
    static isNonNegativeNumber(value: unknown): value is number {
        return this.isNumber(value) && value >= 0;
    }

    /**
     * Validates if value is one of the allowed values
     */
    static isOneOf<T>(value: unknown, allowedValues: T[]): value is T {
        return allowedValues.includes(value as T);
    }
}

// API Response validation schemas
export const ApiValidators = {
    /**
     * Validates User Response from API
     */
    validateUserResponse: (
        data: unknown
    ): data is import("../types").UserResponse => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError("User response is not an object");
            return false;
        }

        const required = [
            "id",
            "email",
            "firstName",
            "lastName",
            "phoneNumber",
            "admin",
        ];
        for (const field of required) {
            if (!(field in data)) {
                errorLogger.logValidationError(
                    `Missing required field: ${field}`,
                    field
                );
                return false;
            }
        }

        if (
            !TypeValidator.isNumber(data.id) ||
            !TypeValidator.isPositiveNumber(data.id)
        ) {
            errorLogger.logValidationError("Invalid user ID", "id");
            return false;
        }

        if (!TypeValidator.isEmail(data.email)) {
            errorLogger.logValidationError("Invalid email format", "email");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.firstName)) {
            errorLogger.logValidationError("Invalid first name", "firstName");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.lastName)) {
            errorLogger.logValidationError("Invalid last name", "lastName");
            return false;
        }

        if (!TypeValidator.isPhoneNumber(data.phoneNumber)) {
            errorLogger.logValidationError(
                "Invalid phone number",
                "phoneNumber"
            );
            return false;
        }

        if (!TypeValidator.isBoolean(data.admin)) {
            errorLogger.logValidationError("Invalid admin flag", "admin");
            return false;
        }

        return true;
    },

    /**
     * Validates Product Response from API
     */
    validateProductResponse: (
        data: unknown
    ): data is import("../types").ProductResponse => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError("Product response is not an object");
            return false;
        }

        const required = [
            "id",
            "name",
            "description",
            "price",
            "stock",
            "categories",
        ];
        for (const field of required) {
            if (!(field in data)) {
                errorLogger.logValidationError(
                    `Missing required field: ${field}`,
                    field
                );
                return false;
            }
        }

        if (!TypeValidator.isPositiveNumber(data.id)) {
            errorLogger.logValidationError("Invalid product ID", "id");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.name)) {
            errorLogger.logValidationError("Invalid product name", "name");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.description)) {
            errorLogger.logValidationError(
                "Invalid product description",
                "description"
            );
            return false;
        }

        if (!TypeValidator.isPositiveNumber(data.price)) {
            errorLogger.logValidationError("Invalid product price", "price");
            return false;
        }

        if (!TypeValidator.isNonNegativeNumber(data.stock)) {
            errorLogger.logValidationError("Invalid product stock", "stock");
            return false;
        }

        // FIXED: Validate categories as array
        if (!TypeValidator.isArray(data.categories)) {
            errorLogger.logValidationError(
                "Invalid product categories - should be array",
                "categories"
            );
            return false;
        }

        // Validate each category in the array
        for (const category of data.categories as unknown[]) {
            if (!TypeValidator.isObject(category)) {
                errorLogger.logValidationError(
                    "Invalid category structure in array"
                );
                return false;
            }

            if (!TypeValidator.isPositiveNumber(category.id)) {
                errorLogger.logValidationError("Invalid category ID");
                return false;
            }

            if (!TypeValidator.isNonEmptyString(category.name)) {
                errorLogger.logValidationError("Invalid category name");
                return false;
            }
        }

        return true;
    },

    /**
     * Validates Cart Response from API
     */
    validateCartResponse: (
        data: unknown
    ): data is import("../types").CartResponse => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError("Cart response is not an object");
            return false;
        }

        const required = ["id", "userId", "cartItems"];
        for (const field of required) {
            if (!(field in data)) {
                errorLogger.logValidationError(
                    `Missing required field: ${field}`,
                    field
                );
                return false;
            }
        }

        if (!TypeValidator.isPositiveNumber(data.id)) {
            errorLogger.logValidationError("Invalid cart ID", "id");
            return false;
        }

        if (!TypeValidator.isPositiveNumber(data.userId)) {
            errorLogger.logValidationError("Invalid user ID", "userId");
            return false;
        }

        if (!TypeValidator.isArray(data.cartItems)) {
            errorLogger.logValidationError("Invalid cart items", "cartItems");
            return false;
        }

        // Validate each cart item structure
        for (const item of data.cartItems as unknown[]) {
            if (!TypeValidator.isObject(item)) {
                errorLogger.logValidationError("Invalid cart item structure");
                return false;
            }

            if (!TypeValidator.isPositiveNumber(item.id)) {
                errorLogger.logValidationError("Invalid cart item ID");
                return false;
            }

            if (!TypeValidator.isPositiveNumber(item.quantity)) {
                errorLogger.logValidationError("Invalid cart item quantity");
                return false;
            }

            if (!TypeValidator.isObject(item.product)) {
                errorLogger.logValidationError("Invalid cart item product");
                return false;
            }
        }

        return true;
    },
};

// Form validation schemas
export const FormValidators = {
    /**
     * Validates login form data
     */
    validateLoginForm: (
        data: unknown
    ): data is import("../types").LoginRequest => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError("Login form data is not an object");
            return false;
        }

        if (!TypeValidator.isEmail(data.email)) {
            errorLogger.logValidationError("Invalid email format", "email");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.password)) {
            errorLogger.logValidationError("Password is required", "password");
            return false;
        }

        if ((data.password as string).length < 8) {
            errorLogger.logValidationError(
                "Password must be at least 8 characters",
                "password"
            );
            return false;
        }

        return true;
    },

    /**
     * Validates registration form data
     */
    validateRegistrationForm: (
        data: unknown
    ): data is import("../types").RegisterUserRequest => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError(
                "Registration form data is not an object"
            );
            return false;
        }

        if (!TypeValidator.isEmail(data.email)) {
            errorLogger.logValidationError("Invalid email format", "email");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.password)) {
            errorLogger.logValidationError("Password is required", "password");
            return false;
        }

        if ((data.password as string).length < 8) {
            errorLogger.logValidationError(
                "Password must be at least 8 characters",
                "password"
            );
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.firstName)) {
            errorLogger.logValidationError(
                "First name is required",
                "firstName"
            );
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.lastName)) {
            errorLogger.logValidationError("Last name is required", "lastName");
            return false;
        }

        if (!TypeValidator.isPhoneNumber(data.phoneNumber)) {
            errorLogger.logValidationError(
                "Invalid phone number format",
                "phoneNumber"
            );
            return false;
        }

        return true;
    },

    /**
     * Validates product creation form data
     */
    validateProductForm: (
        data: unknown
    ): data is Omit<import("../types").CreateProductRequest, "file"> => {
        if (!TypeValidator.isObject(data)) {
            errorLogger.logValidationError(
                "Product form data is not an object"
            );
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.name)) {
            errorLogger.logValidationError("Product name is required", "name");
            return false;
        }

        if (!TypeValidator.isNonEmptyString(data.description)) {
            errorLogger.logValidationError(
                "Product description is required",
                "description"
            );
            return false;
        }

        if (!TypeValidator.isPositiveNumber(data.price)) {
            errorLogger.logValidationError("Invalid product price", "price");
            return false;
        }

        if (!TypeValidator.isNonNegativeNumber(data.stock)) {
            errorLogger.logValidationError("Invalid product stock", "stock");
            return false;
        }

        if (
            !TypeValidator.isArray(data.categoryIds) ||
            (data.categoryIds as unknown[]).length === 0
        ) {
            errorLogger.logValidationError(
                "At least one category is required",
                "categoryIds"
            );
            return false;
        }

        return true;
    },
};

// Utility functions for safe type assertions
export const SafeTypeAssertions = {
    /**
     * Safely assert and validate API response
     */
    assertValidResponse: <T>(
        data: unknown,
        validator: (data: unknown) => data is T,
        fallback: T
    ): T => {
        if (validator(data)) {
            return data;
        }
        errorLogger.logValidationError(
            "API response validation failed, using fallback"
        );
        return fallback;
    },

    /**
     * Safely get nested object property
     */
    getNestedProperty: <T>(obj: unknown, path: string[], fallback: T): T => {
        try {
            let current = obj;
            for (const key of path) {
                if (!TypeValidator.isObject(current) || !(key in current)) {
                    return fallback;
                }
                current = current[key];
            }
            return current as T;
        } catch {
            return fallback;
        }
    },

    /**
     * Safely convert to number with fallback
     */
    safeToNumber: (value: unknown, fallback: number = 0): number => {
        if (TypeValidator.isNumber(value)) {
            return value;
        }
        if (TypeValidator.isString(value)) {
            const parsed = parseFloat(value);
            return isNaN(parsed) ? fallback : parsed;
        }
        return fallback;
    },

    /**
     * Safely convert to string with fallback
     */
    safeToString: (value: unknown, fallback: string = ""): string => {
        if (TypeValidator.isString(value)) {
            return value;
        }
        if (value === null || value === undefined) {
            return fallback;
        }
        try {
            return String(value);
        } catch {
            return fallback;
        }
    },
};
