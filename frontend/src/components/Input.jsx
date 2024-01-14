import { useState, useRef, useCallback, useEffect } from 'react';
import clsx from 'clsx';
import { CrossIcon, EyeIcon, EyeCrossedIcon } from './Icons';
import { useTranslation } from 'react-i18next';
import debounce from 'just-debounce-it';

const Input = ({ password = false, list, ...props }) => {
  const inputRef = useRef(null);
  const [hidden, setHidden] = useState(true);
  const [open, setOpen] = useState(false);
  const [showError, setShowError] = useState(false);
  const { errorMessage, onChange, ...inputProps } = props;
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

  const handleDropdownClick = (item) => {
    inputRef.current.value = item.name;
    handleChange({ target: { name: props.name, value: item.id } });
    setOpen(false);
  };

  const handleDocumentClick = (e) => {
    if (!inputRef.current.contains(e.target)) {
      setOpen(false);
    }
  };

  useEffect(() => {
    // Attach click event listener to the document
    document.addEventListener('click', handleDocumentClick);

    // Clean up event listener on component unmount
    return () => {
      document.removeEventListener('click', handleDocumentClick);
    };
  }, []);

  const commonInputProps = {
    ...inputProps,
    placeholder: t(props.placeholder),
    required: props.required,
    onChange: handleChange,
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex w-full relative">
        <label htmlFor={props.id} className="relative w-full">
          <input
            ref={inputRef}
            type={hidden ? 'password' : 'text'}
            {...commonInputProps}
            className={clsx(props.className, password && 'rounded-r-none')}
            onClick={() => setOpen(true)}
          />
          <button tabIndex="-1" className="erasable-button" type="button" onClick={handleClear}>
            <CrossIcon className="w-[10px] h-[10px] fill-text/50" />
          </button>
        </label>
        {password && (
          <button tabIndex="-1" className="input-button" type="button" onClick={() => setHidden((hidden) => !hidden)}>
            {hidden ? <EyeIcon className="icon-xs" /> : <EyeCrossedIcon className="icon-xs" />}
          </button>
        )}
        {open && list && (
          <ul className="dropdown-list">
            {list.map((item) => (
              <li key={item.name} onClick={() => handleDropdownClick(item)}>
                {item.name}
              </li>
            ))}
          </ul>
        )}
      </div>
      {showError && <span className="error">{t(errorMessage)}</span>}
    </div>
  );
};

export default Input;
