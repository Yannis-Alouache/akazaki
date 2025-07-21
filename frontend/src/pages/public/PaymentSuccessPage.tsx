import { useNavigate } from "react-router-dom";
import { Button } from "../../components/common";

export function PaymentSuccessPage() {
  const navigate = useNavigate();
  const orderId = new URLSearchParams(window.location.search).get('orderId');

  return (
    <div className="text-center py-12">
      <div className="text-6xl mb-4">✅</div>
      <h1 className="text-3xl font-bold mb-4">Payment Successful!</h1>
      <p className="text-gray-600 mb-8">
        Your order #{orderId} has been confirmed.
      </p>
      <Button onClick={() => navigate('/')}>
        Continue Shopping
      </Button>
    </div>
  );
}