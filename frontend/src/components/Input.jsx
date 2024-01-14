import { useState, useRef, useCallback, useEffect } from 'react';
import clsx from 'clsx';
import { CrossIcon, EyeIcon, EyeCrossedIcon } from './Icons';
import { useTranslation } from 'react-i18next';
import debounce from 'just-debounce-it';

const Input = ({ password = false, list, ...props }) => {
  const inputRef = useRef(null);
  const inputHiddenRef = useRef(null);
  const [hidden, setHidden] = useState(password);
  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
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
    setSelectedIndex(-1);
  };

  const handleClear = () => {
    if (inputRef.current) {
      inputRef.current.value = '';
      if (inputHiddenRef.current) inputHiddenRef.current.value = '';
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

  const handleInputKeyDown = (e) => {
    const listItems = list.filter((item) => item.name.toLowerCase().startsWith(inputRef.current.value.toLowerCase()));

    switch (e.key) {
      case 'ArrowUp':
        e.preventDefault();
        setSelectedIndex((prevIndex) => (prevIndex !== null ? Math.max(prevIndex - 1, 0) : listItems.length - 1));
        break;
      case 'ArrowDown':
        e.preventDefault();
        setSelectedIndex((prevIndex) => (prevIndex !== null ? Math.min(prevIndex + 1, listItems.length - 1) : 0));
        break;
      case 'Enter':
        if (selectedIndex !== null && listItems[selectedIndex]) {
          handleDropdownClick(listItems[selectedIndex]);
        }
        break;
      default:
        break;
    }

    const selectedLi = document.querySelector('.selected');
    if (selectedLi) {
      const container = document.querySelector('.dropdown-autocomplete');
      const scrollOffset = selectedLi.offsetTop - container.offsetTop;
      container.scrollTop = scrollOffset;
    }
  };

  const commonInputProps = {
    ...inputProps,
    value: list ? inputRef.current?.value : props.value,
    pattern: list ? undefined : props.pattern,
    name: list ? undefined : props.name,
    onKeyDown: list ? handleInputKeyDown : undefined,
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
          <ul className="dropdown-autocomplete">
            {list
              .filter((item) => item.name.toLowerCase().startsWith(inputRef.current.value.toLowerCase()))
              .map((item, index) => {
                const matchIndex = item.name.toLowerCase().indexOf(inputRef.current.value.toLowerCase());
                const prefix = item.name.substring(0, matchIndex);
                const match = item.name.substring(matchIndex, matchIndex + inputRef.current.value.length);
                const suffix = item.name.substring(matchIndex + inputRef.current.value.length);

                const isSelected = index === selectedIndex;

                return (
                  <li
                    key={item.name}
                    onClick={() => handleDropdownClick(item)}
                    className={clsx(isSelected && 'selected')}
                  >
                    {prefix}
                    <span className={clsx(isSelected && 'font-bold')} tabIndex="0">
                      {match}
                    </span>
                    {suffix}
                  </li>
                );
              })}
          </ul>
        )}
        {list && (
          <input type="hidden" ref={inputHiddenRef} value={props.value} pattern={props.pattern} name={props.name} />
        )}
      </div>
      {showError && <span className="error">{t(errorMessage)}</span>}
    </div>
  );
};

export default Input;
