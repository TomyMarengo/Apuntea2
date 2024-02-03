import { useState, useEffect, forwardRef } from 'react';
import { useTranslation } from 'react-i18next';
import clsx from 'clsx';

import { CrossIcon } from './Icons';

const InputAutocomplete = forwardRef(function InputAutocomplete({ list, onChange, errors, ...props }, ref) {
  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const [listItems, setListItems] = useState(list);
  const { t } = useTranslation();

  const handleChange = (e) => {
    setSelectedIndex(0);
    setListItems(
      list.filter((item) => {
        const len = ref.current.value.length;

        return (
          item.name
            .toLowerCase()
            .substring(0, len)
            .localeCompare(ref.current.value.toLowerCase().substring(0, len), undefined, { sensitivity: 'base' }) == 0
        );
      })
    );
    setOpen(true);
    onChange(e);
  };

  const handleClear = () => {
    if (ref.current) {
      ref.current.value = '';
      handleChange({ target: { name: props.name, value: undefined } });
    }
  };

  const handleDropdownClick = (item) => {
    if (ref.current) {
      ref.current.value = item.name;
      handleChange({ target: { name: props.name, value: item.id } });
      setOpen(false);
    }
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
            className={props.className}
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
            {listItems.map((item, index) => {
              const matchIndex = 0;
              const prefix = item.name.substring(0, matchIndex);
              const match = item.name.substring(matchIndex, matchIndex + ref.current.value.length);
              const suffix = item.name.substring(matchIndex + ref.current.value.length);

              const isSelected = index === selectedIndex;

              return (
                <li
                  key={item.name}
                  onClick={() => handleDropdownClick(item)}
                  className={clsx(isSelected && 'selected text-bg hover:text-bg')}
                >
                  {prefix}
                  <span className={clsx({ 'text-bg': isSelected, 'text-inherit': !isSelected })} tabIndex="0">
                    {match}
                  </span>
                  {suffix}
                </li>
              );
            })}
          </ul>
        )}
      </div>
      {errors?.length > 0 &&
        errors.map((error) => (
          <p className="text-sm text-red-500" key={error}>
            {t(error)}
          </p>
        ))}
    </div>
  );
});

export default InputAutocomplete;
