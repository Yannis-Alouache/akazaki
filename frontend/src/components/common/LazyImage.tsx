import { useState, useRef, useEffect } from 'react';

interface LazyImageProps {
  src: string;
  alt: string;
  className?: string;
  placeholder?: string;
  fallback?: React.ReactNode;
}

export function LazyImage({ src, alt, className = '', placeholder, fallback }: LazyImageProps) {
  const [isLoaded, setIsLoaded] = useState(false);
  const [hasError, setHasError] = useState(false);
  const [isInView, setIsInView] = useState(false);
  const imgRef = useRef<HTMLImageElement>(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setIsInView(true);
          observer.disconnect();
        }
      },
      { threshold: 0.1 }
    );

    if (imgRef.current) {
      observer.observe(imgRef.current);
    }

    return () => observer.disconnect();
  }, []);

  const handleLoad = () => {
    setIsLoaded(true);
  };

  const handleError = () => {
    setHasError(true);
  };

  if (hasError) {
    return (
      <div className={`flex items-center justify-center bg-muted ${className}`}>
        {fallback || <span className="text-2xl">🍿</span>}
      </div>
    );
  }

  return (
    <div className="relative">
      {/* Placeholder while loading */}
      {!isLoaded && (
        <div className={`absolute inset-0 bg-muted animate-pulse ${className}`}>
          {placeholder && (
            <div className="flex items-center justify-center h-full">
              <span className="text-muted-foreground">{placeholder}</span>
            </div>
          )}
        </div>
      )}
      
      {/* Actual image */}
      <img
        ref={imgRef}
        src={isInView ? src : undefined}
        alt={alt}
        className={`transition-opacity duration-300 ${className} ${
          isLoaded ? 'opacity-100' : 'opacity-0'
        }`}
        onLoad={handleLoad}
        onError={handleError}
        loading="lazy"
      />
    </div>
  );
}