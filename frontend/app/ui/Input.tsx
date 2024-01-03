import clsx from "clsx";
import React, { useState } from "react";

interface InputProps {
    type?: string;
    placeholder?: string;
    className?: string;
    initialValue?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void; 
}

const Input = ({
    type = "text",
    initialValue = "",
    placeholder,
    className,
    onChange = _ => {},
    ...props
    
} : InputProps) => {
    const [hidden, setHidden] = useState(true);

    return (
        <>
            <input type={hidden ? type : "text"}
                className={clsx("form-input px-4 py-3 rounded-md", 
                    className
                )}
                onChange={onChange}
                placeholder={placeholder} required
                {...props}
            />
            { type === "password" && 
                <button type="button" className="" onClick={() => setHidden(!hidden)}>
                    {hidden ? 
                    <img src="svg/eye.svg" alt="" className="icon-s"/> :
                    <img src="svg/eye-crossed.svg" alt="" className="icon-s"/> }
                </button>
            }
        </>
    );
};

export default Input;