import { useState, useRef, useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import debounce from 'just-debounce-it';
import clsx from 'clsx';

import { CrossIcon, EyeIcon, EyeCrossedIcon } from './Icons';


const Input = ({ password, errorMessage, onChange, ...props }) => {
  const [hidden, setHidden] = useState(password);
  const inputRef = useRef(null);
  const [showError, setShowError] = useState(false);
  const { t } = useTranslation();

  const showErrorCondition = () => {
    if (inputRef.current && inputRef.current.value !== '') {
      setShowError(!inputRef.current.validity.valid);
    } else {
      setShowError(false);
    }
  };

  // eslint-disable-next-line react-hooks/exhaustive-deps
  const debouncedShowError = useCallback(
    debounce(() => {
      showErrorCondition();
    }, 200),
    []
  );

  const handleChange = (e) => {
    onChange(e);
    debouncedShowError();
  };

  const handleClear = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      handleChange({ target: { name: props.name, value: '' } });
    }
  };

  const commonInputProps = {
    ...props,
    placeholder: t(props.placeholder),
    required: props.required,
    onChange: handleChange,
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex w-full relative">
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
            {hidden ? <EyeIcon className="icon-s fill-pri" /> : <EyeCrossedIcon className="icon-s fill-pri" />}
          </button>
        )}
      </div>
      {showError && <span className="error">{t(errorMessage)}</span>}
    </div>
  );
};

export default Input;
