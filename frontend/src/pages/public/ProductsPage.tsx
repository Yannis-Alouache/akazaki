import { useEffect, useState, useMemo, useCallback } from "react";
import { Button, LoadingSpinner } from "../../components/common";
import { ProductCard } from "../../components/public";
import { useProductStore } from "../../store";
import { useToast } from "../../components/common/Toast";

export function ProductsPage() {
    const {
        products,
        isLoading,
        error,
        fetchProducts,
        currentPage,
        totalPages,
        setCurrentPage,
    } = useProductStore();

    const [selectedCategory, setSelectedCategory] = useState<string>("");
    const { showToast, ToastContainer } = useToast();

    // EXTRACT CATEGORIES FROM PRODUCTS
    const availableCategories = useMemo(() => {
        const categorySet = new Set<string>();
        products.forEach((product) => {
            product.categories.forEach((cat) => categorySet.add(cat.name));
        });
        return Array.from(categorySet).sort();
    }, [products]);

    // FILTER PRODUCTS CLIENT-SIDE
    const filteredProducts = useMemo(() => {
        if (!selectedCategory) return products;

        return products.filter((product) =>
            product.categories.some((cat) => cat.name === selectedCategory)
        );
    }, [products, selectedCategory]);

    // Load ALL products initially
    useEffect(() => {
        fetchProducts({
            page: currentPage,
            size: 50,
        });
    }, [fetchProducts, currentPage]);

    // Memoize page change handler
    const handlePageChange = useCallback(
        (page: number) => {
            setCurrentPage(page);
            window.scrollTo(0, 0);
        },
        [setCurrentPage]
    );

    // Memoize category change handler
    const handleCategoryChange = useCallback((category: string) => {
        setSelectedCategory(category);
        // Don't reset page since we're filtering client-side
    }, []);

    // Handle cart actions with toast feedback
    const handleCartAction = useCallback(
        (success: boolean, message: string) => {
            showToast(message, success ? "success" : "error");
        },
        [showToast]
    );

    // Memoize pagination buttons
    const paginationButtons = useMemo(() => {
        if (totalPages <= 1 || selectedCategory) return null; // Hide pagination when filtering

        return (
            <div className="mt-8 flex justify-center gap-2">
                <Button
                    onClick={() => handlePageChange(currentPage - 1)}
                    disabled={currentPage === 0}
                    variant="secondary"
                    size="sm"
                >
                    Précédent
                </Button>

                <div className="flex items-center gap-2">
                    {Array.from({ length: totalPages }, (_, i) => (
                        <button
                            key={i}
                            onClick={() => handlePageChange(i)}
                            className={`w-10 h-10 rounded transition-all ${
                                currentPage === i
                                    ? "bg-primary text-primary-foreground"
                                    : "bg-muted text-muted-foreground hover:opacity-80"
                            }`}
                        >
                            {i + 1}
                        </button>
                    ))}
                </div>

                <Button
                    onClick={() => handlePageChange(currentPage + 1)}
                    disabled={currentPage === totalPages - 1}
                    variant="secondary"
                    size="sm"
                >
                    Suivant
                </Button>
            </div>
        );
    }, [currentPage, totalPages, handlePageChange, selectedCategory]);

    if (error) {
        return (
            <div className="text-center py-12 bg-card rounded-lg shadow border border-border">
                <p className="text-accent text-lg">{error}</p>
                <Button
                    onClick={() => fetchProducts({ page: 0, size: 50 })}
                    className="mt-4"
                >
                    Réessayer
                </Button>
            </div>
        );
    }

    return (
        <div>
            {/* Toast container */}
            <ToastContainer />

            <div className="mb-8">
                <h1 className="text-3xl font-bold text-foreground mb-4">
                    Nos produits
                </h1>

                {/* Category Filter - UPDATED TO USE EXTRACTED CATEGORIES */}
                <div className="flex flex-wrap gap-2 mb-6">
                    <button
                        onClick={() => handleCategoryChange("")}
                        className={`px-4 py-2 rounded-lg transition-all ${
                            selectedCategory === ""
                                ? "bg-primary text-primary-foreground"
                                : "bg-muted text-muted-foreground hover:bg-muted hover:opacity-80"
                        }`}
                    >
                        Tous
                    </button>

                    {/* SHOW CATEGORIES EXTRACTED FROM PRODUCTS */}
                    {availableCategories.map((categoryName) => (
                        <button
                            key={categoryName}
                            onClick={() => handleCategoryChange(categoryName)}
                            className={`px-4 py-2 rounded-lg transition-all ${
                                selectedCategory === categoryName
                                    ? "bg-primary text-primary-foreground"
                                    : "bg-muted text-muted-foreground hover:bg-muted hover:opacity-80"
                            }`}
                        >
                            {categoryName}
                        </button>
                    ))}
                </div>

                {/* Show filter status */}
                {selectedCategory && (
                    <div className="mb-4 text-sm text-muted-foreground">
                        Affichage de {filteredProducts.length} produits dans la catégorie "
                        {selectedCategory}"
                    </div>
                )}
            </div>

            {isLoading ? (
                <LoadingSpinner />
            ) : (
                <>
                    {/* Products Grid - USE FILTERED PRODUCTS */}
                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                        {filteredProducts.map((product) => (
                            <ProductCard
                                key={product.id}
                                product={product}
                                onAddToCart={handleCartAction}
                            />
                        ))}
                    </div>

                    {filteredProducts.length === 0 && !isLoading && (
                        <div className="text-center py-12 bg-card rounded-lg shadow border border-border">
                            <p className="text-muted-foreground text-lg">
                                {selectedCategory
                                    ? `No products found in "${selectedCategory}" category.`
                                    : "No products found."}
                            </p>
                        </div>
                    )}

                    {/* Pagination - Only show when not filtering */}
                    {paginationButtons}
                </>
            )}
        </div>
    );
}
