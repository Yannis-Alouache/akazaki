import { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Input, Button } from '../../components/common';
import { useAuthStore } from '../../store';
import { FormValidators } from '../../utils/validation';
import { errorLogger } from '../../components/common/ErrorBoundary';

export function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const login = useAuthStore((state) => state.login);
  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const from = location.state?.from?.pathname || '/';

  const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  setError('');
  
  // Validate form data before submission
  if (!FormValidators.validateLoginForm(formData)) {
    setError('Please check your email and password format');
    return;
  }
  
  setIsLoading(true);

  try {
    await login(formData);
    navigate(from, { replace: true });
  } catch (err: any) {
    const errorMessage = err.response?.data?.reason || 'Login failed. Please try again.';
    errorLogger.logValidationError(`Login attempt failed: ${errorMessage}`);
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
          Welcome Back! 🍿
        </h1>
        
        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
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
            label="Password"
            type="password"
            name="password"
            value={formData.password}
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
            Login
          </Button>
        </form>

        <div className="mt-6 text-center">
          <p className="text-muted-foreground">
            Don't have an account?{' '}
            <Link to="/register" className="text-primary hover:text-primary hover:opacity-80 font-medium transition-opacity">
              Sign up
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}