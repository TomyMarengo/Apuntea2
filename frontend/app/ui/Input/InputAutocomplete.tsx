import React, { useState } from "react";
import Select from "react-select";
import type { Option } from "@/lib/definitions";
import styles from "./Input.module.css";

export interface InputAutocompleteProps {
  placeholder?: string;
  items: readonly Option[];
  defaultValue?: Option;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const InputAutocomplete = ({
  placeholder,
  items,
  defaultValue,
  onChange = () => {},
}: InputAutocompleteProps) => {
  const [isClearable, setIsClearable] = useState(true);
  const [isSearchable, setIsSearchable] = useState(true);

  const classNames = {
    control: () => styles.control,
    container: () => styles.container,
    option: () => styles.option,
    placeholder: () => styles.placeholder,
  };

  return (
    <>
      <Select
        classNames={classNames}
        defaultValue={defaultValue}
        isClearable={isClearable}
        isSearchable={isSearchable}
        name="color"
        options={items}
        placeholder={placeholder}
        theme={(theme) => ({
          //TODO import theme colors from tailwind
          ...theme,
          colors: {
            ...theme.colors,
            primary25: "#fdc286",
            primary: "#ef7765",
          },
        })}
      />
    </>
  );
};

export default InputAutocomplete;
