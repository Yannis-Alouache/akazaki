import { Link } from "react-router-dom";
import { type ProductResponse } from "../../types";
import { Button, LazyImage } from "../common";
import { useCartStore } from "../../store";
import { useState, memo } from "react";

interface ProductCardProps {
    product: ProductResponse;
    onAddToCart?: (success: boolean, message: string) => void;
}

export const ProductCard = memo(function ProductCard({
    product,
    onAddToCart,
}: ProductCardProps) {
    const addToCart = useCartStore((state) => state.addToCart);
    const [isAdding, setIsAdding] = useState(false);

    const handleAddToCart = async () => {
        setIsAdding(true);
        try {
            await addToCart(product.id);
            onAddToCart?.(true, `${product.name} added to cart!`);
        } catch (error) {
            console.error("Failed to add to cart:", error);
            onAddToCart?.(false, "Failed to add item to cart");
        } finally {
            setIsAdding(false);
        }
    };

    return (
        <div className="bg-card rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-all border">
            <Link to={`/products/${product.id}`}>
                <div className="aspect-w-4 aspect-h-3 bg-gray-200">
                    {product.imageUrl ? (
                        <LazyImage
                            src={`http://localhost:8080${product.imageUrl}`}
                            alt={product.name}
                            className="w-full h-48 object-cover"
                            placeholder="Loading image..."
                            fallback={<span className="text-4xl">🍿</span>}
                        />
                    ) : (
                        <div className="w-full h-48 flex items-center justify-center bg-gray-100">
                            <span className="text-4xl">🍿</span>
                        </div>
                    )}
                </div>
            </Link>

            <div className="p-4">
                <Link to={`/products/${product.id}`}>
                    <h3 className="text-lg font-semibold text-foreground hover:text-primary transition">
                        {product.name}
                    </h3>
                </Link>

                <p className="text-sm text-muted-foreground mt-1 line-clamp-2">
                    {product.description}
                </p>

                {/* UPDATED: Categories as array */}
                <p className="text-sm text-muted-foreground mt-2">
                    Catégories :{" "}
                    <span className="font-medium text-foreground">
                        {product.categories.map((cat) => cat.name).join(", ")}
                    </span>
                </p>

                <div className="mt-4 flex items-center justify-between">
                    <div>
                        <span className="text-2xl font-bold text-primary">
                            €{product.price.toFixed(2)}
                        </span>
                        {product.stock > 0 ? (
                            <p className="text-sm text-green-600">
                                En stock ({product.stock})
                            </p>
                        ) : (
                            <p className="text-sm text-red-600">En rupture de stock</p>
                        )}
                    </div>

                    <Button
                        size="sm"
                        onClick={handleAddToCart}
                        disabled={product.stock === 0 || isAdding}
                        isLoading={isAdding}
                    >
                        Ajouter au panier
                    </Button>
                </div>
            </div>
        </div>
    );
});
