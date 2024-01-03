import clsx from "clsx";


interface ButtonProps {
  primary?: boolean;
  outlined?: boolean;
  label: string;
  className?: string;
  onClick?: () => void; 
}

const Button = ({
  primary = false,
  outlined = true,
  className,
  label,
  onClick,
  ...props
}: ButtonProps) => {
  return (
    <button
      type="button"
      className={clsx(
        outlined && "border-2 bg-opacity-0",
        primary ? "bg-primary border-primary" : "bg-secondary border-secondary",
        outlined && (primary ? "text-primary" : "text-secondary"),
        "rounded-3xl px-4 py-2 min-w-32 text-white",
        className
      )}
      onClick={onClick}
      {...props}
    >
      {label}
    </button>
  );
};

export default Button;
