import clsx from "clsx";
import styles from "./Input.module.css";

interface InputProps {
  type?: string;
  initialValue?: string;
  placeholder?: string;
  className?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const Input = ({
  type = "text",
  initialValue = "",
  placeholder,
  className,
  onChange = () => {},
  ...props
}: InputProps) => {
  return (
    <input
      type={type}
      defaultValue={initialValue}
      placeholder={placeholder}
      className={clsx(styles.input, className)}
      onChange={(e) => onChange(e)}
      required
      {...props}
    />
  );
};

export default Input;
