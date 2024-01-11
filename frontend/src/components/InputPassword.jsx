import { useState } from 'react';

import Input from './Input';
import { EyeIcon, EyeCrossedIcon } from './Icons';

const InputPassword = ({ onChange = () => {}, ...props }) => {
  const [hidden, setHidden] = useState(true);

  return (
    <div className="flex w-full">
      <Input type={hidden ? 'password' : 'text'} onChange={onChange} required className="rounded-r-none" {...props} />
      <button className="input-button" type="button" onClick={() => setHidden(!hidden)}>
        {hidden ? <EyeIcon className="icon-xs" /> : <EyeCrossedIcon className="icon" />}
      </button>
    </div>
  );
};

export default InputPassword;
