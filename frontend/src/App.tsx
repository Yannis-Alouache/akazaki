import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Suspense, lazy, useEffect } from "react";
import { PublicLayout } from "./layouts/PublicLayout";
import { useAuthStore, useCartStore } from "./store";
import { ThemeProvider } from "./contexts/ThemeContext";
import { CriticalErrorBoundary, LoadingSpinner } from "./components/common";

// Lazy load heavy components
const AdminLayout = lazy(() =>
    import("./layouts/AdminLayout").then((module) => ({
        default: module.AdminLayout,
    }))
);

// Public pages - keep lightweight ones normal, lazy load heavy ones
import {
    HomePage,
    ProductsPage,
    LoginPage,
    RegisterPage,
} from "./pages/public";
import { PaymentPage } from "./pages/public/PaymentPage";
import { PaymentSuccessPage } from "./pages/public/PaymentSuccessPage";
const ProductDetailPage = lazy(() =>
    import("./pages/public/ProductDetailPage").then((module) => ({
        default: module.ProductDetailPage,
    }))
);
const CartPage = lazy(() =>
    import("./pages/public/CartPage").then((module) => ({
        default: module.CartPage,
    }))
);
const CheckoutPage = lazy(() =>
    import("./pages/public/CheckoutPage").then((module) => ({
        default: module.CheckoutPage,
    }))
);

// Admin pages - all lazy loaded
const AdminDashboard = lazy(() =>
    import("./pages/admin/AdminDashboard").then((module) => ({
        default: module.AdminDashboard,
    }))
);
const AdminProductsPage = lazy(() =>
    import("./pages/admin/AdminProductsPage").then((module) => ({
        default: module.AdminProductsPage,
    }))
);
const AdminCategoriesPage = lazy(() =>
    import("./pages/admin/AdminCategoriesPage").then((module) => ({
        default: module.AdminCategoriesPage,
    }))
);

// Loading component for lazy routes
function PageLoader() {
    return (
        <div className="min-h-96 flex items-center justify-center">
            <LoadingSpinner size="lg" text="Loading page..." />
        </div>
    );
}

// Admin loading component
function AdminLoader() {
    return (
        <div className="min-h-screen flex items-center justify-center bg-background">
            <LoadingSpinner size="xl" text="Loading admin panel..." />
        </div>
    );
}

function App() {
    const checkAuth = useAuthStore((state) => state.checkAuth);
    const { isAuthenticated } = useAuthStore(); // ADD THIS
    const fetchCart = useCartStore((state) => state.fetchCart);

    useEffect(() => {
        checkAuth();
    }, [checkAuth]);

    useEffect(() => {
        if (isAuthenticated) {
            fetchCart();
        }
    }, [isAuthenticated, fetchCart]);

    return (
        <CriticalErrorBoundary>
            <ThemeProvider>
                <BrowserRouter>
                    <Routes>
                        {/* Public Routes */}
                        <Route path="/" element={<PublicLayout />}>
                            <Route index element={<HomePage />} />
                            <Route path="products" element={<ProductsPage />} />
                            <Route
                                path="products/:id"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <ProductDetailPage />
                                    </Suspense>
                                }
                            />
                            <Route path="login" element={<LoginPage />} />
                            <Route path="register" element={<RegisterPage />} />
                            <Route
                                path="cart"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <CartPage />
                                    </Suspense>
                                }
                            />
                            <Route
                                path="checkout"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <CheckoutPage />
                                    </Suspense>
                                }
                            />
                            <Route
                                path="payment/:orderId"
                                element={<PaymentPage />}
                            />
                            <Route
                                path="payment-success"
                                element={<PaymentSuccessPage />}
                            />
                        </Route>

                        {/* Admin Routes - Fully Lazy Loaded */}
                        <Route
                            path="/admin"
                            element={
                                <Suspense fallback={<AdminLoader />}>
                                    <AdminLayout />
                                </Suspense>
                            }
                        >
                            <Route
                                index
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <AdminDashboard />
                                    </Suspense>
                                }
                            />
                            <Route
                                path="products"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <AdminProductsPage />
                                    </Suspense>
                                }
                            />
                            <Route
                                path="orders"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <div>Admin Orders (TODO)</div>
                                    </Suspense>
                                }
                            />
                            <Route
                                path="categories"
                                element={
                                    <Suspense fallback={<PageLoader />}>
                                        <AdminCategoriesPage />
                                    </Suspense>
                                }
                            />
                        </Route>

                        {/* Redirect unknown routes */}
                        <Route path="*" element={<Navigate to="/" replace />} />
                    </Routes>
                </BrowserRouter>
            </ThemeProvider>
        </CriticalErrorBoundary>
    );
}

export default App;
