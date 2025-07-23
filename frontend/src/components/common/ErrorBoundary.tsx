import React, { Component, type ReactNode } from "react";
import { Button } from "./Button";

// Error types for better categorization
export type ErrorType =
    | "auth"
    | "network"
    | "validation"
    | "runtime"
    | "unknown";

export interface ErrorInfo {
    type: ErrorType;
    message: string;
    stack?: string;
    componentStack?: string;
    timestamp: Date;
    userAgent?: string;
    url?: string;
    userId?: string;
}

// Error logging utility
class ErrorLogger {
    private static instance: ErrorLogger;
    private isDevelopment = import.meta.env.DEV; // Vite environment variable

    static getInstance(): ErrorLogger {
        if (!ErrorLogger.instance) {
            ErrorLogger.instance = new ErrorLogger();
        }
        return ErrorLogger.instance;
    }

    log(error: ErrorInfo): void {
        // Development: Full logging
        if (this.isDevelopment) {
            console.group(`🚨 Error [${error.type.toUpperCase()}]`);
            console.error("Message:", error.message);
            console.error("Timestamp:", error.timestamp.toISOString());
            if (error.stack) console.error("Stack:", error.stack);
            if (error.componentStack)
                console.error("Component Stack:", error.componentStack);
            if (error.url) console.error("URL:", error.url);
            if (error.userId) console.error("User ID:", error.userId);
            console.groupEnd();
        } else {
            // Production: Minimal logging
            console.error(`[${error.type}] ${error.message}`);
        }
    }

    logNetworkError(error: any, url: string): void {
        this.log({
            type: "network",
            message: `Network request failed: ${
                error.message || "Unknown error"
            }`,
            stack: error.stack,
            timestamp: new Date(),
            url: url,
            userAgent: navigator.userAgent,
        });
    }

    logAuthError(message: string, userId?: string): void {
        this.log({
            type: "auth",
            message,
            timestamp: new Date(),
            userId,
            url: window.location.href,
        });
    }

    logValidationError(message: string, field?: string): void {
        this.log({
            type: "validation",
            message: `Validation failed: ${message}${
                field ? ` (field: ${field})` : ""
            }`,
            timestamp: new Date(),
            url: window.location.href,
        });
    }
}

// Error boundary props
interface ErrorBoundaryProps {
    children: ReactNode;
    fallback?: ReactNode;
    onError?: (error: Error, errorInfo: React.ErrorInfo) => void;
    level?: "page" | "component" | "critical";
}

// Error boundary state
interface ErrorBoundaryState {
    hasError: boolean;
    error: Error | null;
    errorInfo: React.ErrorInfo | null;
    errorId: string;
}

// Main Error Boundary Component
export class ErrorBoundary extends Component<
    ErrorBoundaryProps,
    ErrorBoundaryState
> {
    private logger = ErrorLogger.getInstance();
    private retryCount = 0;
    private maxRetries = 3;

    constructor(props: ErrorBoundaryProps) {
        super(props);
        this.state = {
            hasError: false,
            error: null,
            errorInfo: null,
            errorId: "",
        };
    }

    static getDerivedStateFromError(error: Error): Partial<ErrorBoundaryState> {
        return {
            hasError: true,
            error,
            errorId: `error_${Date.now()}_${Math.random()
                .toString(36)
                .substr(2, 9)}`,
        };
    }

    componentDidCatch(error: Error, errorInfo: React.ErrorInfo): void {
        // FIXED: Handle componentStack properly
        this.logger.log({
            type: "runtime",
            message: error.message,
            stack: error.stack,
            componentStack: errorInfo.componentStack || undefined, // Convert null to undefined
            timestamp: new Date(),
            url: window.location.href,
            userAgent: navigator.userAgent,
        });

        // Update state
        this.setState({ errorInfo });

        // Call custom error handler
        this.props.onError?.(error, errorInfo);
    }

    handleRetry = (): void => {
        if (this.retryCount < this.maxRetries) {
            this.retryCount++;
            this.setState({
                hasError: false,
                error: null,
                errorInfo: null,
                errorId: "",
            });
        } else {
            // Max retries reached, reload page
            window.location.reload();
        }
    };

    handleReload = (): void => {
        window.location.reload();
    };

    handleGoHome = (): void => {
        window.location.href = "/";
    };

    render(): ReactNode {
        if (this.state.hasError) {
            // Custom fallback provided
            if (this.props.fallback) {
                return this.props.fallback;
            }

            // Default fallback based on error level
            return this.renderDefaultFallback();
        }

        return this.props.children;
    }

    private renderDefaultFallback(): ReactNode {
        const { level = "component" } = this.props;
        const { error } = this.state;
        const canRetry = this.retryCount < this.maxRetries;

        if (level === "critical") {
            return (
                <div className="min-h-screen bg-background flex items-center justify-center p-4">
                    <div className="max-w-md w-full bg-card rounded-lg shadow-lg p-8 text-center border border-border">
                        <div className="text-6xl mb-4">⚠️</div>
                        <h1 className="text-2xl font-bold text-card-foreground mb-4">
                            Une erreur s'est produite
                        </h1>
                        <p className="text-muted-foreground mb-6">
                            Nous sommes désolés, mais quelque chose d'inattendu
                            s'est produit. Veuillez essayer de rafraîchir la
                            page.
                        </p>
                        <div className="space-y-3">
                            <Button
                                onClick={this.handleReload}
                                size="lg"
                                className="w-full"
                            >
                                Rafraîchir la page
                            </Button>
                            <Button
                                onClick={this.handleGoHome}
                                variant="secondary"
                                size="lg"
                                className="w-full"
                            >
                                Aller à l'accueil
                            </Button>
                        </div>
                        {import.meta.env.DEV && (
                            <details className="mt-6 text-left">
                                <summary className="cursor-pointer text-sm text-muted-foreground hover:text-primary">
                                    Error Details (Dev Only)
                                </summary>
                                <pre className="mt-2 text-xs bg-muted p-3 rounded overflow-auto text-accent">
                                    {error?.message}
                                    {error?.stack &&
                                        `\n\nStack:\n${error.stack}`}
                                </pre>
                            </details>
                        )}
                    </div>
                </div>
            );
        }

        if (level === "page") {
            return (
                <div className="min-h-96 bg-card rounded-lg shadow p-8 text-center border border-border">
                    <div className="text-4xl mb-4">🔧</div>
                    <h2 className="text-xl font-bold text-card-foreground mb-4">
                        Erreur de Page
                    </h2>
                    <p className="text-muted-foreground mb-6">
                        Cette page a rencontré une erreur. Vous pouvez réessayer ou retourner à la page d'accueil.
                    </p>
                    <div className="flex gap-3 justify-center">
                        {canRetry && (
                            <Button onClick={this.handleRetry}>
                                Réessayer ({this.maxRetries - this.retryCount}{" "}
                                restant)
                            </Button>
                        )}
                        <Button onClick={this.handleGoHome} variant="secondary">
                            Retour à l'accueil
                        </Button>
                    </div>
                </div>
            );
        }

        // Component level error
        return (
            <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-4">
                <div className="flex items-center gap-3">
                    <div className="text-red-600 dark:text-red-400">⚠️</div>
                    <div className="flex-1">
                        <h3 className="font-medium text-red-800 dark:text-red-200">
                            Erreur de Composant
                        </h3>
                        <p className="text-sm text-red-700 dark:text-red-300">
                            Ce composant n'a pas pu se charger correctement.
                        </p>
                    </div>
                    {canRetry && (
                        <Button
                            onClick={this.handleRetry}
                            size="sm"
                            variant="secondary"
                            className="text-red-700 dark:text-red-300"
                        >
                            Réessayer
                        </Button>
                    )}
                </div>
            </div>
        );
    }
}

// Specialized error boundaries for different use cases
export const PageErrorBoundary: React.FC<{ children: ReactNode }> = ({
    children,
}) => <ErrorBoundary level="page">{children}</ErrorBoundary>;

export const ComponentErrorBoundary: React.FC<{ children: ReactNode }> = ({
    children,
}) => <ErrorBoundary level="component">{children}</ErrorBoundary>;

export const CriticalErrorBoundary: React.FC<{ children: ReactNode }> = ({
    children,
}) => <ErrorBoundary level="critical">{children}</ErrorBoundary>;

// Auth Error Boundary with specific handling
export const AuthErrorBoundary: React.FC<{ children: ReactNode }> = ({
    children,
}) => (
    <ErrorBoundary
        level="page"
        onError={(error: Error) => {
            // FIXED: Added explicit type
            ErrorLogger.getInstance().logAuthError(
                `Auth component error: ${error.message}`
            );
        }}
        fallback={
            <div className="min-h-96 bg-card rounded-lg shadow p-8 text-center border border-border">
                <div className="text-4xl mb-4">🔐</div>
                <h2 className="text-xl font-bold text-card-foreground mb-4">
                    Erreur d'Authentification
                </h2>
                <p className="text-muted-foreground mb-6">
                    Il y a eu un problème avec l'authentification. Veuillez réessayer de vous connecter.
                </p>
                <Button onClick={() => (window.location.href = "/login")}>
                    Aller à la connexion
                </Button>
            </div>
        }
    >
        {children}
    </ErrorBoundary>
);

// Custom hook for error handling in components
export const useErrorHandler = () => {
    const logger = ErrorLogger.getInstance();

    const handleError = (error: Error, type: ErrorType = "unknown") => {
        logger.log({
            type,
            message: error.message,
            stack: error.stack,
            timestamp: new Date(),
            url: window.location.href,
            userAgent: navigator.userAgent,
        });
    };

    const handleNetworkError = (error: any, url: string) => {
        logger.logNetworkError(error, url);
    };

    const handleValidationError = (message: string, field?: string) => {
        logger.logValidationError(message, field);
    };

    return {
        handleError,
        handleNetworkError,
        handleValidationError,
    };
};

// Export logger for direct use
export const errorLogger = ErrorLogger.getInstance();
