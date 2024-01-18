import clsx from 'clsx';

const Box = ({ children, ...props }) => {
  return <div className={clsx('flex drop-shadow-lg bg-bg p-3 rounded-2xl', props.className)}>{children}</div>;
};

export default Box;
