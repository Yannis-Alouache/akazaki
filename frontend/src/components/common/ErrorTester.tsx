import { useState } from 'react';
import { Button } from './Button';
import { useErrorHandler } from './ErrorBoundary';

export function ErrorTester() {
  const [shouldError, setShouldError] = useState(false);
  const { handleError, handleNetworkError, handleValidationError } = useErrorHandler();

  // This will trigger a component error boundary
  if (shouldError) {
    throw new Error('This is a test error triggered by ErrorTester component');
  }

  const triggerNetworkError = () => {
    const fakeError = new Error('Network timeout');
    handleNetworkError(fakeError, '/api/test');
  };

  const triggerValidationError = () => {
    handleValidationError('Invalid email format', 'email');
  };

  const triggerRuntimeError = () => {
    const error = new Error('Runtime error test');
    handleError(error, 'runtime');
  };

  return (
    <div className="bg-card rounded-lg shadow p-6 border border-border">
      <h3 className="text-lg font-bold text-card-foreground mb-4">Error Boundary Tester</h3>
      <div className="space-y-3">
        <Button 
          onClick={() => setShouldError(true)}
          variant="danger"
          size="sm"
        >
          Trigger Component Error
        </Button>
        
        <Button 
          onClick={triggerNetworkError}
          variant="secondary"
          size="sm"
        >
          Log Network Error
        </Button>
        
        <Button 
          onClick={triggerValidationError}
          variant="secondary"
          size="sm"
        >
          Log Validation Error
        </Button>
        
        <Button 
          onClick={triggerRuntimeError}
          variant="secondary"
          size="sm"
        >
          Log Runtime Error
        </Button>
      </div>
    </div>
  );
}