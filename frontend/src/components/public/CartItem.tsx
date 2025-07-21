import { useState } from 'react';
import { type CartItemResponse } from '../../types';
import { useCartStore } from '../../store';
import { Button } from '../common';

interface CartItemProps {
  item: CartItemResponse;
}


export function CartItem({ item }: CartItemProps) {
  const updateQuantity = useCartStore((state) => state.updateQuantity);
  const removeFromCart = useCartStore((state) => state.removeFromCart);
  const [isUpdating, setIsUpdating] = useState(false);

  const handleQuantityChange = async (newQuantity: number) => {
    if (newQuantity < 0 || newQuantity > item.product.stock) return;
    
    setIsUpdating(true);
    try {
      await updateQuantity(item.product.id, newQuantity);
    } catch (error) {
      console.error('Failed to update quantity:', error);
    } finally {
      setIsUpdating(false);
    }
  };

  const handleRemove = async () => {
    setIsUpdating(true);
    try {
      await removeFromCart(item.product.id);
    } catch (error) {
      console.error('Failed to remove item:', error);
    } finally {
      setIsUpdating(false);
    }
  };

  return (
    <div className="bg-card rounded-lg shadow p-4 mb-4 border border-border">
      <div className="flex items-center gap-4">
        {/* Product Image */}
        <div className="w-24 h-24 flex-shrink-0">
          {item.product.imageUrl ? (
            <img
              src={`http://localhost:8080${item.product.imageUrl}`}
              alt={item.product.name}
              className="w-full h-full object-cover rounded"
            />
          ) : (
            <div className="w-full h-full bg-muted rounded flex items-center justify-center">
              <span className="text-2xl">🍿</span>
            </div>
          )}
        </div>

        {/* Product Info */}
        <div className="flex-grow">
          <h3 className="font-semibold text-lg text-card-foreground">{item.product.name}</h3>
          <p className="text-muted-foreground text-sm">{item.product.description}</p>
          <p className="text-primary font-bold">€{item.product.price.toFixed(2)}</p>
        </div>

        {/* Quantity Controls */}
        <div className="flex items-center gap-2">
          <button
            onClick={() => handleQuantityChange(item.quantity - 1)}
            disabled={isUpdating || item.quantity <= 1}
            className="w-8 h-8 rounded bg-muted hover:opacity-80 disabled:opacity-50 disabled:cursor-not-allowed transition-opacity"
          >
            -
          </button>
          <span className="w-12 text-center font-medium text-foreground">{item.quantity}</span>
          <button
            onClick={() => handleQuantityChange(item.quantity + 1)}
            disabled={isUpdating || item.quantity >= item.product.stock}
            className="w-8 h-8 rounded bg-muted hover:opacity-80 disabled:opacity-50 disabled:cursor-not-allowed transition-opacity"
          >
            +
          </button>
        </div>

        {/* Subtotal */}
        <div className="text-right">
          <p className="font-bold text-lg text-foreground">
            €{(item.product.price * item.quantity).toFixed(2)}
          </p>
        </div>

        {/* Remove Button */}
        <Button
          variant="danger"
          size="sm"
          onClick={handleRemove}
          disabled={isUpdating}
        >
          Remove
        </Button>
      </div>
    </div>
  );
}