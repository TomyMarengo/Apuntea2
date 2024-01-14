import clsx from 'clsx';
import { useMemo } from 'react';
import { NavLink } from 'react-router-dom';

const Button = ({ primary = true, outlined = false, children, ...props }) => {
  const { className, ...restProps } = props;
  const classes = useMemo(
    () =>
      clsx(
        outlined && 'border bg-transparent hover:text-bg',
        primary ? 'bg-pri border-pri hover:bg-dark-pri' : 'bg-sec border-sec',
        outlined && (primary ? 'text-pri hover:bg-pri' : 'text-sec hover:bg-sec'),
        { 'text-bg': !outlined },
        'button',
        className
      ),
    [outlined, primary, className]
  );

  if ('to' in props) {
    const { to, ...anchorProps } = restProps;
    return (
      <NavLink to={to} className={classes} {...anchorProps}>
        {children}
      </NavLink>
    );
  } else {
    return (
      <button type="button" className={classes} {...restProps}>
        {children}
      </button>
    );
  }
};

export default Button;
