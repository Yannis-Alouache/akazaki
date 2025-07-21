import { useState, useEffect } from "react";

export type ToastType = "success" | "error" | "warning" | "info";

interface ToastProps {
    message: string;
    type: ToastType;
    duration?: number;
    onClose: () => void;
}

export function Toast({ message, type, duration = 4000, onClose }: ToastProps) {
    useEffect(() => {
        const timer = setTimeout(onClose, duration);
        return () => clearTimeout(timer);
    }, [duration, onClose]);

    const typeStyles = {
        success: "bg-green-500 text-white",
        error: "bg-red-500 text-white",
        warning: "bg-yellow-500 text-black",
        info: "bg-blue-500 text-white",
    };

    const icons = {
        success: "✅",
        error: "❌",
        warning: "⚠️",
        info: "ℹ️",
    };

    return (
        <div
            className={`
      fixed top-4 right-4 z-50 p-4 rounded-lg shadow-lg max-w-sm
      transform transition-all duration-300 ease-in-out
      ${typeStyles[type]}
    `}
        >
            <div className="flex items-center gap-3">
                <span className="text-lg">{icons[type]}</span>
                <p className="flex-1">{message}</p>
                <button
                    onClick={onClose}
                    className="text-lg hover:opacity-80 transition-opacity"
                >
                    ×
                </button>
            </div>
        </div>
    );
}

// Toast hook for easy usage
export function useToast() {
    const [toasts, setToasts] = useState<
        Array<{ id: string; message: string; type: ToastType }>
    >([]);

    const showToast = (message: string, type: ToastType) => {
        const id = Math.random().toString(36).substr(2, 9);
        setToasts((prev) => [...prev, { id, message, type }]);
    };

    const removeToast = (id: string) => {
        setToasts((prev) => prev.filter((toast) => toast.id !== id));
    };

    const ToastContainer = () => (
        <>
            {toasts.map((toast) => (
                <Toast
                    key={toast.id}
                    message={toast.message}
                    type={toast.type}
                    onClose={() => removeToast(toast.id)}
                />
            ))}
        </>
    );

    return { showToast, ToastContainer };
}
