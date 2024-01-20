import clsx from 'clsx';

const Box = ({ children, ...props }) => {
  return <div className={clsx('flex drop-shadow-lg bg-bg px-4 py-3 rounded-3xl', props.className)}>{children}</div>;
};

export default Box;
