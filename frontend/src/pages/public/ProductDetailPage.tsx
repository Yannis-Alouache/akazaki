import { useEffect, useState } from "react";
import { Button, LazyImage, LoadingSpinner } from "../../components/common";
import { useCartStore, useProductStore } from "../../store";
import { useNavigate, useParams } from "react-router-dom";

export function ProductDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { currentProduct, isLoading, error, fetchProductById } = useProductStore();
  const addToCart = useCartStore((state) => state.addToCart);
  const [isAdding, setIsAdding] = useState(false);
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    if (id) {
      fetchProductById(parseInt(id));
    }
  }, [id, fetchProductById]);

  const handleAddToCart = async () => {
    if (!currentProduct) return;
    
    setIsAdding(true);
    try {
      for (let i = 0; i < quantity; i++) {
        await addToCart(currentProduct.id);
      }
      navigate('/cart');
    } catch (error) {
      console.error('Failed to add to cart:', error);
    } finally {
      setIsAdding(false);
    }
  };

  if (isLoading) return <LoadingSpinner />;
  
  if (error || !currentProduct) {
    return (
      <div className="text-center py-12 bg-card rounded-lg shadow border border-border">
        <p className="text-accent text-lg mb-4">{error || 'Product not found'}</p>
        <Button onClick={() => navigate('/products')}>
          Back to Products
        </Button>
      </div>
    );
  }

  return (
    <div className="max-w-6xl mx-auto">
      <Button
        variant="secondary"
        size="sm"
        onClick={() => navigate('/products')}
        className="mb-6"
      >
        ← Back to Products
      </Button>

      <div className="bg-card rounded-lg shadow-lg overflow-hidden border border-border">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          {/* Product Image */}
            <div className="bg-muted">
            {currentProduct.imageUrl ? (
                <LazyImage
                src={`http://localhost:8080${currentProduct.imageUrl}`}
                alt={currentProduct.name}
                className="w-full h-full object-cover min-h-[400px]"
                placeholder="Loading product image..."
                fallback={
                    <div className="w-full h-full min-h-[400px] flex items-center justify-center">
                    <span className="text-8xl">🍿</span>
                    </div>
                }
                />
            ) : (
                <div className="w-full h-full min-h-[400px] flex items-center justify-center">
                <span className="text-8xl">🍿</span>
                </div>
            )}
            </div>

          {/* Product Info */}
          <div className="p-8">
            <h1 className="text-3xl font-bold text-card-foreground mb-4">
              {currentProduct.name}
            </h1>
            
            <div className="mb-6">
              <span className="text-4xl font-bold text-primary">
                €{currentProduct.price.toFixed(2)}
              </span>
            </div>

            <div className="mb-6">
              <h2 className="text-lg font-semibold mb-2 text-foreground">Description</h2>
              <p className="text-muted-foreground">{currentProduct.description}</p>
            </div>

            <div className="mb-6">
              {/* UPDATED: Categories as array */}
              <p className="text-sm text-muted-foreground">
                Categories: <span className="font-medium text-foreground">
                  {currentProduct.categories.map(cat => cat.name).join(', ')}
                </span>
              </p>
              {currentProduct.stock > 0 ? (
                <p className="text-sm text-green-600 dark:text-green-400 mt-1">
                  ✓ In stock ({currentProduct.stock} available)
                </p>
              ) : (
                <p className="text-sm text-accent mt-1">
                  ✗ Out of stock
                </p>
              )}
            </div>

            {/* Quantity Selector */}
            <div className="mb-6">
              <label className="block text-sm font-medium text-foreground mb-2">
                Quantity
              </label>
              <div className="flex items-center gap-3">
                <button
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  className="w-10 h-10 rounded-lg bg-muted hover:opacity-80 flex items-center justify-center transition-opacity"
                >
                  -
                </button>
                <input
                  type="number"
                  value={quantity}
                  onChange={(e) => setQuantity(Math.max(1, Math.min(currentProduct.stock, parseInt(e.target.value) || 1)))}
                  className="w-20 text-center border border-border rounded-lg px-3 py-2 bg-card text-card-foreground"
                  min="1"
                  max={currentProduct.stock}
                />
                <button
                  onClick={() => setQuantity(Math.min(currentProduct.stock, quantity + 1))}
                  className="w-10 h-10 rounded-lg bg-muted hover:opacity-80 flex items-center justify-center transition-opacity"
                >
                  +
                </button>
              </div>
            </div>

            <Button
              size="lg"
              onClick={handleAddToCart}
              disabled={currentProduct.stock === 0 || isAdding}
              isLoading={isAdding}
              className="w-full"
            >
              {currentProduct.stock === 0 ? 'Out of Stock' : 'Add to Cart'}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}