import clsx from 'clsx';

const TabContent = ({ children, ...props }) => {
  return (
    <div className={clsx('p-7 bg-bg rounded-3xl rounded-tl-none shadow gap-5 file-list', props.className)}>
      {children}
    </div>
  );
};

export default TabContent;
