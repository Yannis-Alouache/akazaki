
interface LoadingSpinnerProps {
  size?: 'sm' | 'md' | 'lg' | 'xl';
  variant?: 'primary' | 'secondary' | 'accent';
  text?: string;
  className?: string;
}

export function LoadingSpinner({ 
  size = 'md', 
  variant = 'primary', 
  text, 
  className = '' 
}: LoadingSpinnerProps) {
  const sizeClasses = {
    sm: 'h-4 w-4',
    md: 'h-8 w-8',
    lg: 'h-12 w-12',
    xl: 'h-16 w-16'
  };

  const colorClasses = {
    primary: 'border-primary',
    secondary: 'border-muted-foreground',
    accent: 'border-accent'
  };

  const textSizes = {
    sm: 'text-xs',
    md: 'text-sm',
    lg: 'text-base',
    xl: 'text-lg'
  };

  return (
    <div className={`flex flex-col items-center justify-center py-8 ${className}`}>
      <div 
        className={`
          animate-spin rounded-full border-2 border-t-transparent
          ${sizeClasses[size]} ${colorClasses[variant]}
        `}
      />
      {text && (
        <p className={`mt-3 text-muted-foreground ${textSizes[size]}`}>
          {text}
        </p>
      )}
    </div>
  );
}