import { useState, useEffect } from "react";
import { categoryService } from "../../services";
import { Button, Input, LoadingSpinner } from "../../components/common";
import type { CategoryResponse } from "../../types";

export function AdminCategoriesPage() {
    const [categories, setCategories] = useState<CategoryResponse[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [isCreating, setIsCreating] = useState(false);
    const [categoryName, setCategoryName] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        loadCategories();
    }, []);

    const loadCategories = async () => {
        try {
            const data = await categoryService.getCategories();
            setCategories(data);
        } catch (error) {
            console.error("Failed to load categories:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        setIsCreating(true);

        try {
            await categoryService.createCategory({ name: categoryName });
            setCategoryName("");
            setShowCreateModal(false);
            loadCategories();
        } catch (err: any) {
            setError(err.response?.data?.reason || "Failed to create category");
        } finally {
            setIsCreating(false);
        }
    };

    if (isLoading) return <LoadingSpinner />;

    return (
        <div>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold text-foreground">
                    Gestion des catégories
                </h1>
                <Button onClick={() => setShowCreateModal(true)}>
                    Ajouter une catégorie
                </Button>
            </div>

            {/* Categories Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {categories.map((category) => (
                    <div
                        key={category.id}
                        className="bg-card rounded-lg shadow p-6 border border-border"
                    >
                        <h3 className="text-lg font-semibold text-card-foreground">
                            {category.name}
                        </h3>
                        <p className="text-sm text-muted-foreground mt-2">
                            ID: {category.id}
                        </p>
                    </div>
                ))}
            </div>

            {categories.length === 0 && (
                <div className="text-center py-12 bg-card rounded-lg shadow border border-border">
                    <p className="text-muted-foreground">
                        Aucune catégorie trouvée. Créez votre première catégorie !
                    </p>
                </div>
            )}

            {/* Create Category Modal */}
            {showCreateModal && (
                <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                    <div className="bg-card rounded-lg p-8 max-w-md w-full border border-border">
                        <h2 className="text-2xl font-bold mb-6 text-card-foreground">
                            Ajouter une nouvelle catégorie
                        </h2>

                        {error && (
                            <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
                                {error}
                            </div>
                        )}

                        <form onSubmit={handleSubmit}>
                            <Input
                                label="Category Name"
                                value={categoryName}
                                onChange={(e) =>
                                    setCategoryName(e.target.value)
                                }
                                required
                                placeholder="e.g., Snacks, Boissons"
                            />

                            <div className="flex gap-4">
                                <Button
                                    type="submit"
                                    isLoading={isCreating}
                                    disabled={isCreating}
                                >
                                    Créer la catégorie
                                </Button>
                                <Button
                                    type="button"
                                    variant="secondary"
                                    onClick={() => {
                                        setShowCreateModal(false);
                                        setCategoryName("");
                                        setError("");
                                    }}
                                    disabled={isCreating}
                                >
                                    Annuler
                                </Button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
