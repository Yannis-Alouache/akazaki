import { useState, useEffect } from 'react';
import { useProductStore } from '../../store';
import { productService, categoryService } from '../../services';
import { Button, Input, LazyImage, LoadingSpinner } from '../../components/common';
import { FormValidators } from '../../utils/validation';
import { errorLogger } from '../../components/common/ErrorBoundary';
import type { CategoryResponse } from '../../types';

export function AdminProductsPage() {
  const { products, isLoading, fetchProducts } = useProductStore();
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [categories, setCategories] = useState<CategoryResponse[]>([]);
  const [isCreating, setIsCreating] = useState(false);
  const [error, setError] = useState('');
  
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: '',
    stock: '',
    categoryIds: [] as number[],
    file: null as File | null,
  });

  useEffect(() => {
    fetchProducts({ page: 0, size: 50 });
    loadCategories();
  }, [fetchProducts]);

  const loadCategories = async () => {
    try {
      const cats = await categoryService.getCategories();
      setCategories(cats);
    } catch (error) {
      console.error('Failed to load categories:', error);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setFormData(prev => ({ ...prev, file: e.target.files![0] }));
    }
  };

  const handleCategoryToggle = (categoryId: number) => {
    setFormData(prev => ({
      ...prev,
      categoryIds: prev.categoryIds.includes(categoryId)
        ? prev.categoryIds.filter(id => id !== categoryId)
        : [...prev.categoryIds, categoryId]
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.file) {
      setError('Please select an image');
      return;
    }

    // Validate form data
    const productData = {
      name: formData.name,
      description: formData.description,
      price: parseFloat(formData.price),
      stock: parseInt(formData.stock),
      categoryIds: formData.categoryIds,
    };

    if (!FormValidators.validateProductForm(productData)) {
      setError('Please check all fields and ensure they are correctly formatted');
      return;
    }

    setIsCreating(true);
    setError('');

    try {
      await productService.createProduct({
        ...productData,
        file: formData.file,
      });
      
      // Reset form and reload products
      setFormData({
        name: '',
        description: '',
        price: '',
        stock: '',
        categoryIds: [],
        file: null,
      });
      setShowCreateModal(false);
      fetchProducts({ page: 0, size: 50 });
    } catch (err: any) {
      const errorMessage = err.response?.data?.reason || 'Failed to create product';
      errorLogger.logValidationError(`Product creation failed: ${errorMessage}`);
      setError(errorMessage);
    } finally {
      setIsCreating(false);
    }
  };

  const handleDelete = async (productId: number) => {
    if (!confirm('Are you sure you want to delete this product?')) return;

    try {
      await productService.deleteProduct(productId);
      fetchProducts({ page: 0, size: 50 });
    } catch (error) {
      console.error('Failed to delete product:', error);
    }
  };

  if (isLoading) return <LoadingSpinner />;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-foreground">Manage Products</h1>
        <Button onClick={() => setShowCreateModal(true)}>
          Add New Product
        </Button>
      </div>

      {/* Products Table */}
      <div className="bg-card rounded-lg shadow overflow-hidden border border-border">
        <table className="min-w-full">
          <thead className="bg-muted">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                Product
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                Categories
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                Price
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                Stock
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-muted-foreground uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="divide-y divide-border">
            {products.map((product) => (
              <tr key={product.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="flex items-center">
                    <div className="flex-shrink-0 h-10 w-10">
                        {product.imageUrl ? (
                            <LazyImage
                            src={`http://localhost:8080${product.imageUrl}`}
                            alt={product.name}
                            className="h-10 w-10 rounded object-cover"
                            fallback={
                                <div className="h-10 w-10 rounded bg-muted flex items-center justify-center">
                                🍿
                                </div>
                            }
                            />
                        ) : (
                            <div className="h-10 w-10 rounded bg-muted flex items-center justify-center">
                            🍿
                            </div>
                        )}
                    </div>
                    <div className="ml-4">
                      <div className="text-sm font-medium text-foreground">
                        {product.name}
                      </div>
                      <div className="text-sm text-muted-foreground">
                        {product.description.substring(0, 50)}...
                      </div>
                    </div>
                  </div>
                </td>
                {/* UPDATED: Categories as array */}
                <td className="px-6 py-4 whitespace-nowrap text-sm text-muted-foreground">
                  {product.categories.map(cat => cat.name).join(', ')}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-foreground">
                  €{product.price.toFixed(2)}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                    product.stock > 10 
                      ? 'bg-green-100 text-green-800 dark:bg-green-900/20 dark:text-green-400' 
                      : product.stock > 0 
                      ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900/20 dark:text-yellow-400'
                      : 'bg-red-100 text-red-800 dark:bg-red-900/20 dark:text-red-400'
                  }`}>
                    {product.stock}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button
                    onClick={() => handleDelete(product.id)}
                    className="text-accent hover:text-accent hover:opacity-80 transition-opacity"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Create Product Modal */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
          <div className="bg-card rounded-lg p-8 max-w-2xl w-full max-h-[90vh] overflow-y-auto border border-border">
            <h2 className="text-2xl font-bold mb-6 text-card-foreground">Add New Product</h2>
            
            {error && (
              <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-400 px-4 py-3 rounded mb-4">
                {error}
              </div>
            )}

            <form onSubmit={handleSubmit}>
              <Input
                label="Product Name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
              />

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Description
                </label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleInputChange}
                  required
                  rows={3}
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <Input
                  label="Price (€)"
                  name="price"
                  type="number"
                  step="0.01"
                  min="0"
                  value={formData.price}
                  onChange={handleInputChange}
                  required
                />

                <Input
                  label="Stock"
                  name="stock"
                  type="number"
                  min="0"
                  value={formData.stock}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Categories
                </label>
                <div className="space-y-2">
                  {categories.map((category) => (
                    <label key={category.id} className="flex items-center">
                      <input
                        type="checkbox"
                        checked={formData.categoryIds.includes(category.id)}
                        onChange={() => handleCategoryToggle(category.id)}
                        className="mr-2"
                      />
                      <span>{category.name}</span>
                    </label>
                  ))}
                </div>
              </div>

              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Product Image
                </label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  required
                  className="w-full"
                />
              </div>

               <div className="flex gap-4">
                <Button
                  type="submit"
                  isLoading={isCreating}
                  disabled={isCreating}
                >
                  Create Product
                </Button>
                <Button
                  type="button"
                  variant="secondary"
                  onClick={() => setShowCreateModal(false)}
                  disabled={isCreating}
                >
                  Cancel
                </Button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}