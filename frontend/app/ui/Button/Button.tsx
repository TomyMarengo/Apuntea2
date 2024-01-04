import clsx from "clsx";
import styles from "./button.module.css";

interface ButtonProps {
  primary?: boolean;
  outlined?: boolean;
  label: string;
  className?: string;
  onClick?: () => void;
}

const Button = ({
  primary = true,
  outlined = false,
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
        { "text-white": !outlined },
        styles.button,
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
