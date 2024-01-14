import clsx from 'clsx';

const Card = ({ children, ...props }) => {
  return <div className={clsx('flex px-20 py-14 drop-shadow-lg bg-bg rounded-3xl', props.className)}>{children}</div>;
};

export default Card;
