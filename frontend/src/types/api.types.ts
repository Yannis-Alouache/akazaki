// API Response Types

export interface ErrorResponse {
  reason: string;
  status: string;
}

// User & Authentication Types
export interface RegisterUserRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  admin?: boolean;
}

export interface RegisterUserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  admin: boolean;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface UserResponse {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  admin: boolean;
}

// Category Types
export interface CategoryResponse {
  id: number;
  name: string;
}

export interface CreateCategoryRequest {
  name: string;
}

// Product Types
export interface ProductResponse {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  imageUrl: string;
  categories: CategoryResponse[];
}

export interface CreateProductRequest {
  name: string;
  description: string;
  price: number;
  stock: number;
  file: File;
  categoryIds: number[];
}

export interface ProductListRequest {
  page?: number;
  size?: number;
  categories?: string[];
}

export interface ProductListResponse {
  products: ProductResponse[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
}

// Cart Types
export interface CartItemResponse {
  id: number;
  quantity: number;
  product: ProductResponse;
}

export interface CartResponse {
  id: number;
  userId: number;
  cartItems: CartItemResponse[];
}

export interface UpdateCartItemQuantityRequest {
  quantity: number;
}

// Address Types
export interface AddressRequest {
  lastName: string;
  firstName: string;
  streetNumber: string;
  street: string;
  addressComplement?: string;
  postalCode: string;
  city: string;
  country: string;
}

export interface AddressResponse extends AddressRequest {
  id: number;
  postCode: string; // Note: API returns 'postCode' not 'postalCode'
}

// Order Types
export interface OrderItemResponse {
  id: number;
  quantity: number;
  price: number;
  product: ProductResponse;
}

export type OrderStatus = 'PENDING' | 'PAID' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface OrderResponse {
  id: number;
  userId: number;
  date: string;
  status: OrderStatus;
  items: OrderItemResponse[];
  totalPrice: number;
  billingAddress: AddressResponse;
  shippingAddress: AddressResponse;
}

export interface UpdateOrderAddressesRequest {
  billingAddress: AddressRequest;
  shippingAddress: AddressRequest;
}

// Payment Types
export interface CreatePaymentIntentRequest {
  orderId: number;
}

export interface PaymentIntentResponse {
  clientSecret: string;
}

// Stock Types
export interface StockAvailabilityResponse {
  orderId: number;
  isStockAvailable: boolean;
  missingItems?: {
    productId: number;
    productName: string;
    requestedQuantity: number;
    availableQuantity: number;
  };
}