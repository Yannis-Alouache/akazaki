import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useCartStore, useAuthStore } from '../../store';
import { CartItem } from '../../components/public/CartItem';
import { Button, LoadingSpinner } from '../../components/common';

export function CartPage() {
  const navigate = useNavigate();
  const { items, isLoading, error, fetchCart, getTotalPrice, getTotalItems } = useCartStore();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
    }
  }, [fetchCart, isAuthenticated]);

  const handleCheckout = () => {
    if (!isAuthenticated) {
      navigate('/login', { state: { from: { pathname: '/checkout' } } });
    } else {
      navigate('/checkout');
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="max-w-4xl mx-auto text-center py-12">
        <div className="bg-card rounded-lg shadow-lg p-8 border border-border">
          <h1 className="text-3xl font-bold mb-4 text-card-foreground">Shopping Cart</h1>
          <p className="text-muted-foreground mb-6">Please login to view your cart</p>
          <Button onClick={() => navigate('/login')}>
            Login to Continue
          </Button>
        </div>
      </div>
    );
  }

  if (isLoading) return <LoadingSpinner />;

  if (error) {
    return (
      <div className="text-center py-12">
        <div className="bg-card rounded-lg shadow p-8 border border-border">
          <p className="text-accent text-lg">{error}</p>
          <Button onClick={() => fetchCart()} className="mt-4">
            Try Again
          </Button>
        </div>
      </div>
    );
  }

  const totalItems = getTotalItems();
  const totalPrice = getTotalPrice();

  return (
    <div className="max-w-4xl mx-auto">
      <h1 className="text-3xl font-bold mb-8 text-foreground">Shopping Cart</h1>

      {items.length === 0 ? (
        <div className="text-center py-12 bg-card rounded-lg shadow border border-border">
          <p className="text-muted-foreground text-lg mb-4">Your cart is empty</p>
          <Link to="/products">
            <Button>Continue Shopping</Button>
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart Items */}
          <div className="lg:col-span-2">
            {items.map((item) => (
              <CartItem key={item.id} item={item} />
            ))}
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1">
            <div className="bg-card rounded-lg shadow p-6 sticky top-20 border border-border">
              <h2 className="text-xl font-bold mb-4 text-card-foreground">Order Summary</h2>
              
              <div className="space-y-2 mb-4">
                <div className="flex justify-between text-muted-foreground">
                  <span>Items ({totalItems})</span>
                  <span>€{totalPrice.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-muted-foreground">
                  <span>Shipping</span>
                  <span className="text-green-600 dark:text-green-400">Free</span>
                </div>
              </div>
              
              <div className="border-t border-border pt-4 mb-6">
                <div className="flex justify-between text-lg font-bold">
                  <span className="text-foreground">Total</span>
                  <span className="text-primary">€{totalPrice.toFixed(2)}</span>
                </div>
              </div>

              <Button
                onClick={handleCheckout}
                size="lg"
                className="w-full mb-3"
              >
                Proceed to Checkout
              </Button>
              
              <Link to="/products">
                <Button variant="secondary" size="lg" className="w-full">
                  Continue Shopping
                </Button>
              </Link>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}