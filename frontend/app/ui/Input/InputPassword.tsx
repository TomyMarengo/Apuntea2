import React, { useState } from "react";

import Input from "./Input";
import styles from "./Input.module.css";
import { EyeIcon } from "../Icons";
import { EyeCrossedIcon } from "../Icons";

interface InputPasswordProps {
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const InputPassword = ({ onChange = () => {} }: InputPasswordProps) => {
  const [hidden, setHidden] = useState(true);

  return (
    <div className="flex flex-row-reverse">
      <button
        type="button"
        className={styles.button}
        onClick={() => setHidden(!hidden)}
      >
        {hidden ? (
          <EyeIcon className={styles.icon} />
        ) : (
          <EyeCrossedIcon className={styles.icon} />
        )}
      </button>
      <Input
        type={hidden ? "password" : "text"}
        placeholder="Escribe tu contraseña"
        onChange={onChange}
      />
    </div>
  );
};

export default InputPassword;
