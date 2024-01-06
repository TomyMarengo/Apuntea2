import clsx from "clsx";
import React, { useState, ChangeEvent, FC, InputHTMLAttributes } from "react";
import styles from "./Input.module.css";
import InputPassword from "./InputPassword";
import InputAutocomplete from "./InputAutocomplete";
import { CrossIcon } from "../Icons";

export interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  erasable?: boolean;
  onChange?: (e: ChangeEvent<HTMLInputElement>) => void;
}

const Input: FC<InputProps> = ({
  erasable = false,
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
    <>
      {erasable && (
        <button type="button" className={styles.button} onClick={handleClick}>
          <CrossIcon className={styles.icon} />
        </button>
      )}
      <label htmlFor={props.id} className="w-full">
        <input
          {...props}
          type={props.type || "text"}
          defaultValue={props.defaultValue || ""}
          placeholder={props.placeholder}
          required={props.required}
          onChange={handleChange}
          value={message}
          className={clsx(styles.input, props.className)}
        />
      </label>
    </>
  );
};

export default Input;

export { InputPassword, InputAutocomplete };
