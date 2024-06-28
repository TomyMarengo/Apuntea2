import clsx from 'clsx';
import { Button } from '../index';
import { useTranslation } from 'react-i18next';
import { CrossIcon } from '../Utils/Icons';

const Modal = ({ isOpen, onClose, title, action, onSubmit, children }) => {
  const { t } = useTranslation();

  if (!isOpen) {
    return null;
  }

  return (
    <div className={`modal-overlay`}>
      <div className={clsx('flex flex-col bg-bg rounded-3xl modal-animation min-w-[500px]')}>
        <div className="flex justify-between items-center p-4">
          <h2 className="text-xl">{title}</h2>
          <button className="icon-button" onClick={onClose}>
            <CrossIcon className="icon-xxs fill-dark-text" />
          </button>
        </div>
        <hr />
        <div className="p-4">{children}</div>
        <hr />
        <div className="flex p-4 gap-2 justify-end">
          <Button onClick={onClose} primary={false}>
            {t('actions.close')}
          </Button>
          <Button onClick={onSubmit}>{action}</Button>
        </div>
      </div>
    </div>
  );
};

export default Modal;
