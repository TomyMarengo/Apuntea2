import clsx from 'clsx';

const TabContent = ({ children, active, ...props }) => {
  return (
    <div
      className={clsx('p-7 bg-bg rounded-3xl rounded-tl-none shadow gap-5 file-list', props.className, {
        hidden: !active,
      })}
    >
      {children}
    </div>
  );
};

export default TabContent;
