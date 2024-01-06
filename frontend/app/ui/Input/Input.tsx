import clsx from "clsx";
import React, { useState } from "react";
import styles from "./Input.module.css";

import { CrossIcon } from "../Icons";

export interface InputProps {
  type?: string;
  defaultValue?: string;
  placeholder?: string;
  erasable?: boolean;
  className?: string;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const Input = ({
  type = "text",
  defaultValue = "",
  placeholder,
  erasable = false,
  className,
  onChange = () => {},
  ...props
}: InputProps) => {
  const [message, setMessage] = useState("");

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setMessage(event.target.value);
    onChange(event);
  };

  const handleClick = () => {
    setMessage("");
  };

  return (
    <div className="flex flex-row-reverse">
      {erasable && (
        <button type="button" className={styles.button} onClick={handleClick}>
          <CrossIcon className={styles.icon} />
        </button>
      )}
      <input
        type={type}
        value={message}
        defaultValue={defaultValue}
        placeholder={placeholder}
        className={clsx(styles.input, className)}
        onChange={handleChange}
        required
        {...props}
      />
    </div>
  );
};

export default Input;
