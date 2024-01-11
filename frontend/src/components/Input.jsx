import { useState } from 'react';
import clsx from 'clsx';
import { CrossIcon } from './Icons';

const Input = ({ erasable = false, onChange = () => {}, ...props }) => {
  const [message, setMessage] = useState('');

  const handleChange = (event) => {
    setMessage(event.target.value);
    onChange(event);
  };

  const handleClick = () => {
    setMessage('');
  };

  return erasable ? (
    <div className="flex w-full">
      <label htmlFor={props.id} className="w-full">
        <input
          {...props}
          type={props.type || 'text'}
          placeholder={props.placeholder}
          required={props.required}
          onChange={handleChange}
          value={message}
          className={clsx(props.className, 'rounded-r-none')}
        />
      </label>
      <button className="input-button" type="button" onClick={handleClick}>
        <CrossIcon className="icon" />
      </button>
    </div>
  ) : (
    <div className="flex w-full">
      <label htmlFor={props.id} className="w-full">
        <input
          {...props}
          type={props.type || 'text'}
          placeholder={props.placeholder}
          required={props.required}
          onChange={handleChange}
          value={message}
          className={props.className}
        />
      </label>
    </div>
  );
};

export default Input;
