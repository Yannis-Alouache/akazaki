import { Outlet, Link } from "react-router-dom";
import { useCartStore, useAuthStore } from "../store";
import { ThemeToggle } from "../components/common/ThemeToggle";
import { useState } from "react";
import logo from "../assets/logo.png";

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
                Passez au contenu principal
            </a>

            {/* Navigation Header */}
            <header className="bg-card shadow-sm sticky top-0 z-50 transition-colors border-b">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between items-center h-16">
                        {/* Logo */}
                        <Link to="/" className="flex items-center">
                            <img
                                src={logo}
                                alt="Akazaki Logo"
                                className="h-8 w-auto"
                            />
                            <span className="ml-2 text-2xl font-bold text-primary mochiyPopOne">
                                Akazaki
                            </span>
                        </Link>

                        {/* Mobile menu button */}
                        <div className="md:hidden">
                            <button
                                onClick={() =>
                                    setIsMobileMenuOpen(!isMobileMenuOpen)
                                }
                                className="text-muted-foreground hover:text-primary transition p-2"
                                aria-label="Toggle mobile menu"
                            >
                                <svg
                                    className="h-6 w-6"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                >
                                    {isMobileMenuOpen ? (
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeWidth={2}
                                            d="M6 18L18 6M6 6l12 12"
                                        />
                                    ) : (
                                        <path
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeWidth={2}
                                            d="M4 6h16M4 12h16M4 18h16"
                                        />
                                    )}
                                </svg>
                            </button>
                        </div>

                        {/* Desktop Navigation */}
                        <nav className="hidden md:flex space-x-8 mochiyPopOne">
                            <Link
                                to="/"
                                className="text-muted-foreground hover:text-primary transition"
                            >
                                Accueil
                            </Link>
                            <Link
                                to="/products"
                                className="text-muted-foreground hover:text-primary transition"
                            >
                                Produits
                            </Link>
                        </nav>

                        {/* Desktop Auth & Cart */}
                        <div className="hidden md:flex items-center space-x-4">
                            <ThemeToggle />

                            <Link
                                to="/cart"
                                className="relative text-muted-foreground hover:text-primary transition"
                            >
                                <svg
                                    className="w-6 h-6"
                                    fill="none"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    viewBox="0 0 24 24"
                                    stroke="currentColor"
                                >
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
                                        👋{" "}
                                        {user.firstName ||
                                            user.email.split("@")[0]}
                                        !
                                    </span>
                                    {user.admin && (
                                        <>
                                            <Link
                                                to="/admin"
                                                className="bg-blue-600 text-white px-3 py-1 rounded-lg hover:bg-blue-700 transition text-sm"
                                            >
                                                Administration
                                            </Link>
                                        </>
                                    )}
                                    <button
                                        onClick={logout}
                                        className="bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
                                    >
                                        Se déconnecter
                                    </button>
                                </div>
                            ) : (
                                <Link
                                    to="/login"
                                    className="bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition"
                                >
                                    Connexion
                                </Link>
                            )}
                        </div>
                    </div>

                    {/* Mobile Menu */}
                    {isMobileMenuOpen && (
                        <div className="md:hidden border-t border-border mt-4 pt-4 pb-4">
                            <div className="flex flex-col space-y-4">
                                <Link
                                    to="/"
                                    className="text-muted-foreground hover:text-primary transition px-3 py-2"
                                    onClick={() => setIsMobileMenuOpen(false)}
                                >
                                    Accueil
                                </Link>
                                <Link
                                    to="/products"
                                    className="text-muted-foreground hover:text-primary transition px-3 py-2"
                                    onClick={() => setIsMobileMenuOpen(false)}
                                >
                                    Produits
                                </Link>

                                <div className="flex items-center justify-between px-3 py-2">
                                    <Link
                                        to="/cart"
                                        className="flex items-center text-muted-foreground hover:text-primary transition"
                                        onClick={() =>
                                            setIsMobileMenuOpen(false)
                                        }
                                    >
                                        <svg
                                            className="w-5 h-5 mr-2"
                                            fill="none"
                                            strokeLinecap="round"
                                            strokeLinejoin="round"
                                            strokeWidth="2"
                                            viewBox="0 0 24 24"
                                            stroke="currentColor"
                                        >
                                            <path d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17M17 13v6m0 0l-3 3m3-3l3 3"></path>
                                        </svg>
                                        Panier{" "}
                                        {cartItemsCount > 0 &&
                                            `(${cartItemsCount})`}
                                    </Link>
                                    <ThemeToggle />
                                </div>

                                {isAuthenticated && user ? (
                                    <div className="px-3 py-2 border-t border-border pt-4">
                                        <p className="text-sm text-muted-foreground mb-3">
                                            👋{" "}
                                            {user.firstName ||
                                                user.email.split("@")[0]}
                                            !
                                        </p>
                                        {user.admin && (
                                            <div className="flex items-center gap-2">
                                                <span>Administration</span>
                                            </div>
                                        )}
                                        <button
                                            onClick={() => {
                                                logout();
                                                setIsMobileMenuOpen(false);
                                            }}
                                            className="w-full bg-gray-600 text-white px-4 py-2 rounded-lg hover:bg-gray-700 transition"
                                        >
                                            Se déconnecter
                                        </button>
                                    </div>
                                ) : (
                                    <div className="px-3 py-2 border-t border-border pt-4">
                                        <Link
                                            to="/login"
                                            className="block w-full bg-orange-600 text-white px-4 py-2 rounded-lg hover:bg-orange-700 transition text-center"
                                            onClick={() =>
                                                setIsMobileMenuOpen(false)
                                            }
                                        >
                                            Connexion
                                        </Link>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}
                </div>
            </header>

            {/* Main Content */}
            <main
                id="main-content"
                className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8"
            >
                <Outlet />
            </main>

            {/* Footer */}
            <footer className="bg-card border-t border-border mt-16">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                    {/* Footer Top Section */}
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-8">
                        {/* Brand Section */}
                        <div className="space-y-4">
                            <Link to="/" className="flex items-center">
                            <img
                                src={logo}
                                alt="Akazaki Logo"
                                className="h-8 w-auto"
                            />
                            <span className="ml-2 text-2xl font-bold text-primary mochiyPopOne">
                                Akazaki
                            </span>
                        </Link>
                            <p className="text-sm text-muted-foreground">
                                Votre passerelle vers d'authentiques snacks
                                japonais. Découvrez des saveurs uniques et des
                                friandises livrées directement chez vous.
                            </p>
                            {/* Social Media Links */}
                            <div className="flex space-x-4 pt-2">
                                <a
                                    href="#"
                                    className="text-muted-foreground hover:text-primary transition"
                                    aria-label="Facebook"
                                >
                                    <svg
                                        className="w-6 h-6"
                                        fill="currentColor"
                                        viewBox="0 0 24 24"
                                    >
                                        <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
                                    </svg>
                                </a>
                                <a
                                    href="#"
                                    className="text-muted-foreground hover:text-primary transition"
                                    aria-label="Instagram"
                                >
                                    <svg
                                        className="w-6 h-6"
                                        fill="currentColor"
                                        viewBox="0 0 24 24"
                                    >
                                        <path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zM5.838 12a6.162 6.162 0 1112.324 0 6.162 6.162 0 01-12.324 0zM12 16a4 4 0 110-8 4 4 0 010 8zm4.965-10.405a1.44 1.44 0 112.881.001 1.44 1.44 0 01-2.881-.001z" />
                                    </svg>
                                </a>
                                <a
                                    href="#"
                                    className="text-muted-foreground hover:text-primary transition"
                                    aria-label="Twitter"
                                >
                                    <svg
                                        className="w-6 h-6"
                                        fill="currentColor"
                                        viewBox="0 0 24 24"
                                    >
                                        <path d="M23.953 4.57a10 10 0 01-2.825.775 4.958 4.958 0 002.163-2.723c-.951.555-2.005.959-3.127 1.184a4.92 4.92 0 00-8.384 4.482C7.69 8.095 4.067 6.13 1.64 3.162a4.822 4.822 0 00-.666 2.475c0 1.71.87 3.213 2.188 4.096a4.904 4.904 0 01-2.228-.616v.06a4.923 4.923 0 003.946 4.827 4.996 4.996 0 01-2.212.085 4.936 4.936 0 004.604 3.417 9.867 9.867 0 01-6.102 2.105c-.39 0-.779-.023-1.17-.067a13.995 13.995 0 007.557 2.209c9.053 0 13.998-7.496 13.998-13.985 0-.21 0-.42-.015-.63A9.935 9.935 0 0024 4.59z" />
                                    </svg>
                                </a>
                            </div>
                        </div>

                        {/* Quick Links */}
                        <div>
                            <h4 className="font-semibold text-foreground mb-4">
                                Liens Rapides
                            </h4>
                            <ul className="space-y-2">
                                <li>
                                    <Link
                                        to="/products"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Tous les Produits
                                    </Link>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Nouveautés
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Meilleures Ventes
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Promotions
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Cartes Cadeaux
                                    </a>
                                </li>
                            </ul>
                        </div>

                        {/* Customer Service */}
                        <div>
                            <h4 className="font-semibold text-foreground mb-4">
                                Service Client
                            </h4>
                            <ul className="space-y-2">
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Nous Contacter
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Informations de Livraison
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Retours et Échanges
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        FAQ
                                    </a>
                                </li>
                                <li>
                                    <a
                                        href="#"
                                        className="text-sm text-muted-foreground hover:text-primary transition"
                                    >
                                        Suivre ma Commande
                                    </a>
                                </li>
                            </ul>
                        </div>

                        {/* Newsletter */}
                        <div>
                            <h4 className="font-semibold text-foreground mb-4">
                                Restez Connecté
                            </h4>
                            <p className="text-sm text-muted-foreground mb-4">
                                Inscrivez-vous pour recevoir des offres
                                spéciales, des alertes nouveaux produits et des
                                conseils de dégustation !
                            </p>
                            <form
                                className="space-y-2"
                                onSubmit={(e) => e.preventDefault()}
                            >
                                <input
                                    type="email"
                                    placeholder="Entrez votre email"
                                    className="w-full px-3 py-2 text-sm border border-border rounded-lg bg-background text-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                                />
                                <button
                                    type="submit"
                                    className="w-full px-4 py-2 text-sm bg-primary text-primary-foreground rounded-lg hover:opacity-90 transition"
                                >
                                    S'inscrire
                                </button>
                            </form>
                        </div>
                    </div>

                    {/* Footer Bottom Section */}
                    <div className="border-t border-border pt-8">
                        <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
                            <div className="flex flex-wrap justify-center md:justify-start gap-4 text-sm text-muted-foreground">
                                <a
                                    href="#"
                                    className="hover:text-primary transition"
                                >
                                    Politique de Confidentialité
                                </a>
                                <span className="hidden md:inline">•</span>
                                <a
                                    href="#"
                                    className="hover:text-primary transition"
                                >
                                    Conditions d'Utilisation
                                </a>
                                <span className="hidden md:inline">•</span>
                                <a
                                    href="#"
                                    className="hover:text-primary transition"
                                >
                                    Politique de Cookies
                                </a>
                                <span className="hidden md:inline">•</span>
                                <a
                                    href="#"
                                    className="hover:text-primary transition"
                                >
                                    Plan du Site
                                </a>
                            </div>

                            {/* Payment Methods */}
                            <div className="flex items-center space-x-2">
                                <span className="text-sm text-muted-foreground mr-2">
                                    Nous acceptons :
                                </span>
                                <div className="flex space-x-2">
                                    {/* Visa */}
                                    <div className="w-10 h-6 bg-muted rounded flex items-center justify-center">
                                        <span className="text-xs font-bold text-muted-foreground">
                                            VISA
                                        </span>
                                    </div>
                                    {/* Mastercard */}
                                    <div className="w-10 h-6 bg-muted rounded flex items-center justify-center">
                                        <span className="text-xs font-bold text-muted-foreground">
                                            MC
                                        </span>
                                    </div>
                                    {/* PayPal */}
                                    <div className="w-10 h-6 bg-muted rounded flex items-center justify-center">
                                        <span className="text-xs font-bold text-muted-foreground">
                                            PP
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="text-center mt-6">
                            <p className="text-sm text-muted-foreground">
                                &copy; 2025 Akazaki Snacks. Tous droits
                                réservés. Fait avec 🍡 en France
                            </p>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    );
}
