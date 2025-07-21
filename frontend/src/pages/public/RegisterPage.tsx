import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Input, Button } from '../../components/common';
import { useAuthStore } from '../../store';
import { FormValidators } from '../../utils/validation';
import { errorLogger } from '../../components/common/ErrorBoundary';

export function RegisterPage() {
  const navigate = useNavigate();
  const register = useAuthStore((state) => state.register);
  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
  });

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setError('');

  if (formData.password !== formData.confirmPassword) {
    setError('Passwords do not match');
    return;
  }

  // Validate form data before submission
  const registrationData = {
    email: formData.email,
    password: formData.password,
    firstName: formData.firstName,
    lastName: formData.lastName,
    phoneNumber: formData.phoneNumber,
  };

  if (!FormValidators.validateRegistrationForm(registrationData)) {
    setError('Please check all fields and ensure they are correctly formatted');
    return;
  }

  setIsLoading(true);

  try {
    await register(registrationData);
    navigate('/');
  } catch (err: any) {
    const errorMessage = err.response?.data?.reason || 'Registration failed. Please try again.';
    errorLogger.logValidationError(`Registration attempt failed: ${errorMessage}`);
    setError(errorMessage);
  } finally {
    setIsLoading(false);
  }
};

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  return (
    <div className="max-w-md mx-auto">
      <div className="bg-card rounded-lg shadow-lg p-8 border border-border">
        <h1 className="text-3xl font-bold text-center mb-6 text-card-foreground">
          Join Akazaki! 🎉
        </h1>
        
        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="First Name"
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              required
              placeholder="John"
            />

            <Input
              label="Last Name"
              type="text"
              name="lastName"
              value={formData.lastName}
              onChange={handleChange}
              required
              placeholder="Doe"
            />
          </div>

          <Input
            label="Email"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
            placeholder="john@example.com"
          />

          <Input
            label="Phone Number"
            type="tel"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={handleChange}
            required
            placeholder="+33612345678"
          />

          <Input
            label="Password"
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            placeholder="••••••••"
          />

          <Input
            label="Confirm Password"
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
            placeholder="••••••••"
          />

          <Button
            type="submit"
            isLoading={isLoading}
            className="w-full"
            size="lg"
          >
            Create Account
          </Button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-muted-foreground">
            Already have an account?{' '}
            <Link to="/login" className="text-primary hover:text-primary hover:opacity-80 font-medium transition-opacity">
              Login
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}