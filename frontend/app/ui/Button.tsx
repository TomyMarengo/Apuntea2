interface ButtonProps {
  primary?: boolean;
  label: string;
  onClick?: () => void;
}

export const Button = ({ primary = false, label, ...props }: ButtonProps) => {
  const mode = primary ? "bg-blue-500" : "bg-gray-500";
  return (
    <button
      type="button"
      className={["rounded-lg p-4", mode].join(" ")}
      {...props}
    >
      {label}
    </button>
  );
};
