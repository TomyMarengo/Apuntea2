import { useState, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import clsx from 'clsx';

import { CrossIcon, EyeIcon, EyeCrossedIcon } from './Icons';

const Input = ({ password, onChange, errors, ...props }) => {
  const [hidden, setHidden] = useState(password);
  const inputRef = useRef(null);
  const { t } = useTranslation();

  const handleClear = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      onChange({ target: { name: props.name, value: '' } });
    }
  };

  const commonInputProps = {
    ...props,
    placeholder: t(props.placeholder),
    required: props.required,
    onChange,
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex relative w-full">
        <label htmlFor={props.id} className="relative w-full">
          <input
            {...commonInputProps}
            ref={inputRef}
            type={hidden ? 'password' : 'text'}
            className={clsx(props.className, password && 'rounded-r-none')}
          />
          <button tabIndex="-1" className="erasable-button" type="button" onClick={handleClear}>
            <CrossIcon className="w-[10px] h-[10px] fill-text/50" />
          </button>
        </label>
        {password && (
          <button tabIndex="-1" className="input-button" type="button" onClick={() => setHidden((hidden) => !hidden)}>
            {hidden ? <EyeIcon className="icon-xs fill-pri" /> : <EyeCrossedIcon className="icon-s fill-pri" />}
          </button>
        )}
      </div>
      {errors?.length > 0 &&
        errors.map((error) => (
          <p className="mt-2 text-sm text-red-500" key={error}>
            {error} {/* TODO: Poner el t de translation*/}
          </p>
        ))}
    </div>
  );
};

export default Input;
