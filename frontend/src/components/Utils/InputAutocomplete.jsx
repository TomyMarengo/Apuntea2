import { useState, useCallback, useEffect, forwardRef } from 'react';
import { useTranslation } from 'react-i18next';
import debounce from 'just-debounce-it';
import clsx from 'clsx';

import { CrossIcon } from './Icons';

const InputAutocomplete = forwardRef(function InputAutocomplete(
  { password, list, errorMessage, onChange, ...props },
  ref
) {
  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const [showError, setShowError] = useState(false);
  const { t } = useTranslation();

  const showErrorCondition = () => {
    if (ref.current && ref.current.value !== '') {
      setShowError(!ref.current.validity.valid);
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
    if (ref.current) {
      ref.current.value = '';
      handleChange({ target: { name: props.name, value: '' } });
    }
  };

  const handleDropdownClick = (item) => {
    if (ref.current) ref.current.value = item.name;
    handleChange({ target: { name: props.name, value: item.id } });
    setOpen(false);
  };

  const handleDocumentClick = (e) => {
    if (!ref.current?.contains(e.target)) {
      setOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener('click', handleDocumentClick);
    return () => {
      document.removeEventListener('click', handleDocumentClick);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleInputKeyDown = (e) => {
    const listItems = list.filter((item) => {
      const len = ref.current.value.length;

      return (
        item.name
          .toLowerCase()
          .substring(0, len)
          .localeCompare(ref.current.value.toLowerCase().substring(0, len), undefined, { sensitivity: 'base' }) == 0
      );
    });

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
        e.preventDefault();
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

  return (
    <div className="flex flex-col gap-2 w-full">
      <div className="flex w-full relative">
        <label htmlFor={props.id} className="relative w-full">
          <input
            {...props}
            ref={ref}
            className={clsx(props.className, password && 'rounded-r-none')}
            onClick={() => setOpen(true)}
            onKeyDown={handleInputKeyDown}
            onChange={handleChange}
            placeholder={t(props.placeholder)}
          />
          <button tabIndex="-1" className="erasable-button" type="button" onClick={handleClear}>
            <CrossIcon className="w-[10px] h-[10px] fill-text/50" />
          </button>
        </label>
        {open && list && (
          <ul className="dropdown-autocomplete">
            {list
              .filter((item) => {
                const len = ref.current.value.length;
                return (
                  item.name
                    .toLowerCase()
                    .substring(0, len)
                    .localeCompare(ref.current.value.toLowerCase().substring(0, len), undefined, {
                      sensitivity: 'base',
                    }) == 0
                );
              })
              .map((item, index) => {
                const matchIndex = 0;
                const prefix = item.name.substring(0, matchIndex);
                const match = item.name.substring(matchIndex, matchIndex + ref.current.value.length);
                const suffix = item.name.substring(matchIndex + ref.current.value.length);

                const isSelected = index === selectedIndex;

                return (
                  <li
                    key={item.name}
                    onClick={() => handleDropdownClick(item)}
                    className={clsx(isSelected && 'selected')}
                  >
                    {prefix}
                    <span className={clsx('font-bold')} tabIndex="0">
                      {match}
                    </span>
                    {suffix}
                  </li>
                );
              })}
          </ul>
        )}
      </div>
      {showError && <span className="error">{t(errorMessage)}</span>}
    </div>
  );
});

export default InputAutocomplete;
