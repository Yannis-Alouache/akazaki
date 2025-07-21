import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';

type Theme = 'light' | 'dark';

interface ThemeContextType {
  theme: Theme;
  toggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextType | undefined>(undefined);

// Safe storage utilities with error handling
const safeStorage = {
  getItem: (key: string): string | null => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        return localStorage.getItem(key);
      }
    } catch (error) {
      console.warn(`Failed to read from localStorage: ${error}`);
    }
    return null;
  },
  
  setItem: (key: string, value: string): void => {
    try {
      if (typeof window !== 'undefined' && window.localStorage) {
        localStorage.setItem(key, value);
      }
    } catch (error) {
      console.warn(`Failed to write to localStorage: ${error}`);
    }
  }
};

// Get system preference safely
const getSystemPreference = (): Theme => {
  try {
    if (typeof window !== 'undefined' && window.matchMedia) {
      return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }
  } catch (error) {
    console.warn(`Failed to read system preference: ${error}`);
  }
  return 'light'; // Fallback
};

// Theme detection with fallback strategy
const getInitialTheme = (): Theme => {
  // 1. Try saved preference first
  const savedTheme = safeStorage.getItem('theme') as Theme;
  if (savedTheme && (savedTheme === 'light' || savedTheme === 'dark')) {
    return savedTheme;
  }
  
  // 2. Fall back to system preference
  return getSystemPreference();
};

export function ThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setTheme] = useState<Theme>(getInitialTheme);
  const [isHydrated, setIsHydrated] = useState(false);

  // Handle hydration mismatch in SSR
  useEffect(() => {
    setIsHydrated(true);
    setTheme(getInitialTheme());
  }, []);

  useEffect(() => {
    if (!isHydrated) return;

    // Update document class
    const root = document.documentElement;
    root.classList.remove('light', 'dark');
    root.classList.add(theme);

    // Persist theme preference
    safeStorage.setItem('theme', theme);
  }, [theme, isHydrated]);

  // Listen to system preference changes
  useEffect(() => {
    if (!isHydrated) return;

    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
    const handleSystemChange = (e: MediaQueryListEvent) => {
      // Only auto-switch if user hasn't set a preference
      const savedTheme = safeStorage.getItem('theme');
      if (!savedTheme) {
        setTheme(e.matches ? 'dark' : 'light');
      }
    };

    try {
      mediaQuery.addEventListener('change', handleSystemChange);
      return () => mediaQuery.removeEventListener('change', handleSystemChange);
    } catch (error) {
      console.warn('Failed to listen to system theme changes:', error);
    }
  }, [isHydrated]);

  const toggleTheme = () => {
    setTheme(prev => prev === 'light' ? 'dark' : 'light');
  };

  // Prevent hydration mismatch
  if (!isHydrated) {
    return (
      <ThemeContext.Provider value={{ theme: 'light', toggleTheme }}>
        {children}
      </ThemeContext.Provider>
    );
  }

  return (
    <ThemeContext.Provider value={{ theme, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

export const useTheme = () => {
  const context = useContext(ThemeContext);
  if (!context) {
    throw new Error('useTheme must be used within ThemeProvider');
  }
  return context;
};