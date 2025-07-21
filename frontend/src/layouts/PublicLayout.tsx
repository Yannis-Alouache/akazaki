import { Outlet, Link } from 'react-router-dom';
import { useCartStore, useAuthStore } from '../store';
import { ThemeToggle } from '../components/common/ThemeToggle';
import { useState } from 'react';
import { AdminBadge } from '../components/common/AdminBadge';

export function PublicLayout() {
  const cartItemsCount = useCartStore((state) => state.getTotalItems());
  const { isAuthenticated, user, logout } = useAuthStore();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  

 return (
    <div className="min-h-screen bg-background transition-colors">
      {/* Skip Navigation */}
      <a 
        href="#main-content" 
        className="sr-only focus:not-sr-only focus:absolute focus:top-4 focus:left-4 bg-primary text-primary-foreground px-4 py-2 rounded-lg z-50"
      >
        Skip to main content
      </a>

      {/* Navigation Header */}
      <header className="bg-card shadow-sm sticky top-0 z-50 transition-colors border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            {/* Logo */}
            <Link to="/" className="flex items-center">
              <span className="text-2xl font-bold text-primary">
                🍿 Akazaki
              </span>
            </Link>

            {/* Mobile menu button - ADD THIS */}
            <div className="md:hidden">
              <button
                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                className="text-muted-foreground hover:text-primary transition p-2"
                aria-label="Toggle mobile menu"
              >
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  {isMobileMenuOpen ? (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  ) : (
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                  )}
                </svg>
              </button>
            </div>

            {/* Desktop Navigation */}
            <nav className="hidden md:flex space-x-8">
              <Link to="/" className="text-muted-foreground hover:text-primary transition">
                Home
              </Link>
              <Link to="/products" className="text-muted-foreground hover:text-primary transition">
                Products
              </Link>
            </nav>

            {/* Desktop Auth & Cart */}
            <div className="hidden md:flex items-center space-x-4">
              <ThemeToggle />
              
              <Link to="/cart" className="relative text-muted-foreground hover:text-primary transition">
                <svg className="w-6 h-6" fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24" stroke="currentColor">
                  <path d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17M17 13v6m0 0l-3 3m3-3l3 3"></path>
                </svg>
                {cartItemsCount > 0 && (
                  <span className="absolute -top-2 -right-2 bg-orange-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                    {cartItemsCount}
                  </span>
                )}
              </Link>
              
              {isAuthenticated && user ? (
                <div className="flex items-center space-x-3">
                  <span className="text-sm text-gray-600">
                    Hi, {user.firstName || user.email.split('@')[0]}!
                  </span>
                  {user.admin && (
                    <>
                        <Link 
                            to="/admin" 
                            className="bg-blue-600 text-white px-3 py-1 rounded-lg hover:bg-blue-700 transition text-sm"
                        >
                            Admin Panel
                        </Link>
                    </>
                    )}
                  <button
                    onClick={logout}
                    className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
                  >
                    Logout
                  </button>
                </div>
              ) : (
                <Link to="/login" className="bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition">
                  Login
                </Link>
              )}
            </div>
          </div>

          {/* Mobile Menu - ADD THIS ENTIRE SECTION */}
          {isMobileMenuOpen && (
            <div className="md:hidden border-t border-border mt-4 pt-4 pb-4">
              <div className="flex flex-col space-y-4">
                <Link 
                  to="/" 
                  className="text-muted-foreground hover:text-primary transition px-3 py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Home
                </Link>
                <Link 
                  to="/products" 
                  className="text-muted-foreground hover:text-primary transition px-3 py-2"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Products
                </Link>
                
                <div className="flex items-center justify-between px-3 py-2">
                  <Link 
                    to="/cart" 
                    className="flex items-center text-muted-foreground hover:text-primary transition"
                    onClick={() => setIsMobileMenuOpen(false)}
                  >
                    <svg className="w-5 h-5 mr-2" fill="none" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" viewBox="0 0 24 24" stroke="currentColor">
                      <path d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17M17 13v6m0 0l-3 3m3-3l3 3"></path>
                    </svg>
                    Cart {cartItemsCount > 0 && `(${cartItemsCount})`}
                  </Link>
                  <ThemeToggle />
                </div>
                
                {isAuthenticated && user ? (
                  <div className="px-3 py-2 border-t border-border pt-4">
                    <p className="text-sm text-muted-foreground mb-3">
                      Hi, {user.firstName || user.email.split('@')[0]}!
                    </p>
                    {user.admin && (
                        <div className="flex items-center gap-2">
                            <span>Admin Panel</span>
                        </div>
                    )}
                    <button
                      onClick={() => {
                        logout();
                        setIsMobileMenuOpen(false);
                      }}
                      className="w-full bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
                    >
                      Logout
                    </button>
                  </div>
                ) : (
                  <div className="px-3 py-2 border-t border-border pt-4">
                    <Link 
                      to="/login" 
                      className="block w-full bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition text-center"
                      onClick={() => setIsMobileMenuOpen(false)}
                    >
                      Login
                    </Link>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </header>

      {/* Main Content */}
      <main id="main-content" className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Outlet />
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-white py-8 mt-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <p>&copy; 2025 Akazaki Snacks. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}