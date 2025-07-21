import apiClient from './api.client';
import type { 
  LoginRequest, 
  LoginResponse, 
  RegisterUserRequest, 
  RegisterUserResponse 
} from '../types';

export const authService = {
  async login(data: LoginRequest): Promise<LoginResponse> {
    const response = await apiClient.post<LoginResponse>('/auth/login', data);
    return response.data;
  },

  async register(data: RegisterUserRequest): Promise<RegisterUserResponse> {
    const response = await apiClient.post<RegisterUserResponse>('/auth/register', data);
    return response.data;
  },
};