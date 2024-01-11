import { useState, cloneElement } from 'react';
import clsx from 'clsx';
import { Button } from '.';

const Dropdown = ({ trigger, menu, ...props }) => {
  const [open, setOpen] = useState(false);

  const handleOpen = () => {
    setOpen(!open);
  };

  return (
    <div className="dropdown">
      {cloneElement(trigger, {
        onClick: handleOpen,
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
                        setOpen(false);
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
