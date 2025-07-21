import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { loadStripe } from '@stripe/stripe-js';
import { Elements, PaymentElement, useStripe, useElements } from '@stripe/react-stripe-js';
import { Button, LoadingSpinner } from '../../components/common';
import { paymentService } from '../../services/payment.service';

// Get your Stripe publishable key from environment variables
const stripePromise = loadStripe(import.meta.env.VITE_STRIPE_PUBLISHABLE_KEY);

// Payment form component
function CheckoutForm({ orderId }: { orderId: number }) {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();
  const [isProcessing, setIsProcessing] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!stripe || !elements) return;

    setIsProcessing(true);

    const { error } = await stripe.confirmPayment({
      elements,
      confirmParams: {
        return_url: `${window.location.origin}/payment-success?orderId=${orderId}`,
      },
    });

    if (error) {
      setMessage(error.message || 'An unexpected error occurred.');
      setIsProcessing(false);
    }
    // Stripe will redirect on success
  };

  return (
    <form onSubmit={handleSubmit}>
      <PaymentElement />
      {message && (
        <div className="mt-4 text-red-600">{message}</div>
      )}
      <Button
        type="submit"
        size="lg"
        className="w-full mt-6"
        disabled={isProcessing || !stripe || !elements}
        isLoading={isProcessing}
      >
        {isProcessing ? 'Processing...' : 'Pay now'}
      </Button>
    </form>
  );
}

// Main payment page
export function PaymentPage() {
  const { orderId } = useParams<{ orderId: string }>();
  const navigate = useNavigate();
  const [clientSecret, setClientSecret] = useState('');

  useEffect(() => {
    if (!orderId) {
      navigate('/cart');
      return;
    }

    // Create payment intent
    paymentService
      .createPaymentIntent({ orderId: parseInt(orderId) })
      .then((data) => setClientSecret(data.clientSecret))
      .catch((error) => {
        console.error('Error:', error);
        navigate('/cart');
      });
  }, [orderId, navigate]);

  if (!clientSecret) {
    return <LoadingSpinner size="lg" text="Initializing payment..." />;
  }

  return (
    <div className="max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-6">Complete Payment</h1>
      <Elements stripe={stripePromise} options={{ clientSecret }}>
        <CheckoutForm orderId={parseInt(orderId!)} />
      </Elements>
    </div>
  );
}