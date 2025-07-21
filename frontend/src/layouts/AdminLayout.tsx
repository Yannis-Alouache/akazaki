import { Outlet, Link, Navigate } from 'react-router-dom';
import { useAuthStore } from '../store';
import { LoadingSpinner } from '../components/common';

export function AdminLayout() {
  const { isAuthenticated, user, logout } = useAuthStore();

  // Show loading while checking auth
  if (isAuthenticated === null) {
    return <LoadingSpinner />;
  }

  // Redirect if not authenticated
  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: { pathname: '/admin' } }} replace />;
  }

  // Redirect if authenticated but not admin
  if (user && !user.admin) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="bg-card rounded-lg shadow-lg p-8 max-w-md border border-border">
          <h1 className="text-2xl font-bold text-accent mb-4">Access Denied</h1>
          <p className="text-muted-foreground mb-6">You need admin privileges to access this area.</p>
          <Link to="/" className="text-primary hover:text-primary hover:opacity-80 transition-opacity">
            Return to Home
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Admin Header */}
      <header className="bg-card border-b border-border">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <span className="text-xl font-bold text-foreground">🔧 Admin Panel</span>
            </div>
            
            <nav className="flex space-x-4">
              <Link to="/admin" className="text-muted-foreground hover:text-primary hover:bg-muted px-3 py-2 rounded transition-all">
                Dashboard
              </Link>
              <Link to="/admin/products" className="text-muted-foreground hover:text-primary hover:bg-muted px-3 py-2 rounded transition-all">
                Products
              </Link>
              <Link to="/admin/orders" className="text-muted-foreground hover:text-primary hover:bg-muted px-3 py-2 rounded transition-all">
                Orders
              </Link>
              <Link to="/admin/categories" className="text-muted-foreground hover:text-primary hover:bg-muted px-3 py-2 rounded transition-all">
                Categories
              </Link>
            </nav>

            <div className="flex items-center space-x-4">
              <span className="text-sm text-muted-foreground">
                {user?.firstName ? `${user.firstName} ${user.lastName}` : user?.email}
              </span>
              <button
                onClick={logout}
                className="bg-accent text-accent-foreground hover:opacity-90 px-3 py-1 rounded text-sm transition-opacity"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Admin Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>
    </div>
  );
}