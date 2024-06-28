import clsx from 'clsx';

const Card = ({ children, ...props }) => {
  return <div className={clsx('px-20 py-14 bg-bg rounded-3xl', props.className)}>{children}</div>;
};

export default Card;
