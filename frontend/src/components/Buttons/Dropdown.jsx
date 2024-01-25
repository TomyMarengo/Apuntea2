import { useState, cloneElement, useEffect } from 'react';
import clsx from 'clsx';
import { Button } from '../index';

const Dropdown = ({ trigger, menu, ...props }) => {
  const [open, setOpen] = useState(false);

  const handleOpen = () => {
    setOpen(!open);
  };

  const handleClose = () => {
    setOpen(false);
  };

  useEffect(() => {
    const handleDocumentClick = (event) => {
      // Verifica si el clic fue dentro del dropdown o en el propio botón de activación
      if (!event.target.closest('.dropdown') && !event.target.closest('.dropdown-trigger')) {
        handleClose();
      }
    };

    document.addEventListener('click', handleDocumentClick);

    return () => {
      document.removeEventListener('click', handleDocumentClick);
    };
  }, []);

  return (
    <div className="dropdown">
      {cloneElement(trigger, {
        onClick: handleOpen,
        className: clsx(trigger.props.className, 'dropdown-trigger'),
      })}
      {open ? (
        <ul className={clsx('dropdown-menu', props.className)}>
          {menu.map((menuItem, index) => (
            <li key={index} className="dropdown-menu-item">
              {cloneElement(menuItem, {
                onClick:
                  menuItem.type === Button
                    ? (event) => {
                        if (menuItem.props.onClick) menuItem.props.onClick(event);
                        handleClose();
                      }
                    : undefined,
              })}
            </li>
          ))}
        </ul>
      ) : null}
    </div>
  );
};

export default Dropdown;
