import { useEffect, useState } from 'react';
import { useProductStore } from '../../store';
import { Link } from 'react-router-dom';

export function AdminDashboard() {
  const { products, fetchProducts } = useProductStore();
  const [stats, setStats] = useState({
    totalProducts: 0,
    lowStockProducts: 0,
    outOfStockProducts: 0,
    totalValue: 0,
  });

  useEffect(() => {
    fetchProducts({ page: 0, size: 100 });
  }, [fetchProducts]);

  useEffect(() => {
    const totalProducts = products.length;
    const lowStockProducts = products.filter(p => p.stock > 0 && p.stock <= 10).length;
    const outOfStockProducts = products.filter(p => p.stock === 0).length;
    const totalValue = products.reduce((sum, p) => sum + (p.price * p.stock), 0);

    setStats({
      totalProducts,
      lowStockProducts,
      outOfStockProducts,
      totalValue,
    });
  }, [products]);

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8 text-foreground">Admin Dashboard</h1>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <div className="bg-card rounded-lg shadow p-6 border border-border">
          <h3 className="text-sm font-medium text-muted-foreground uppercase">Total Products</h3>
          <p className="text-3xl font-bold text-foreground mt-2">{stats.totalProducts}</p>
        </div>
        
        <div className="bg-card rounded-lg shadow p-6 border border-border">
          <h3 className="text-sm font-medium text-muted-foreground uppercase">Low Stock</h3>
          <p className="text-3xl font-bold text-yellow-600 dark:text-yellow-400 mt-2">{stats.lowStockProducts}</p>
        </div>
        
        <div className="bg-card rounded-lg shadow p-6 border border-border">
          <h3 className="text-sm font-medium text-muted-foreground uppercase">Out of Stock</h3>
          <p className="text-3xl font-bold text-accent mt-2">{stats.outOfStockProducts}</p>
        </div>
        
        <div className="bg-card rounded-lg shadow p-6 border border-border">
          <h3 className="text-sm font-medium text-muted-foreground uppercase">Inventory Value</h3>
          <p className="text-3xl font-bold text-green-600 dark:text-green-400 mt-2">€{stats.totalValue.toFixed(2)}</p>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="bg-card rounded-lg shadow p-6 border border-border">
        <h2 className="text-xl font-bold mb-4 text-card-foreground">Quick Actions</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link to="/admin/products" className="block p-4 bg-primary/10 rounded-lg hover:bg-primary/20 transition-colors border border-primary/20">
            <h3 className="font-semibold text-primary">Manage Products</h3>
            <p className="text-sm text-muted-foreground mt-1">Add, edit, or remove products</p>
          </Link>
          <Link to="/admin/orders" className="block p-4 bg-blue-500/10 rounded-lg hover:bg-blue-500/20 transition-colors border border-blue-500/20">
            <h3 className="font-semibold text-blue-600 dark:text-blue-400">View Orders</h3>
            <p className="text-sm text-muted-foreground mt-1">Process and manage orders</p>
          </Link>
          <Link to="/admin/categories" className="block p-4 bg-green-500/10 rounded-lg hover:bg-green-500/20 transition-colors border border-green-500/20">
            <h3 className="font-semibold text-green-600 dark:text-green-400">Categories</h3>
            <p className="text-sm text-muted-foreground mt-1">Organize product categories</p>
          </Link>
        </div>
      </div>
    </div>
  );
}