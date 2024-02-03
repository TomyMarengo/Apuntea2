import { useTranslation } from 'react-i18next';
import clsx from 'clsx';

import { FolderIcon, FileIcon } from '../Utils/Icons';

const SearchPill = ({ category, setCategory }) => {
  const { t } = useTranslation();

  return (
    <div className="flex">
      <button
        className={clsx('search-pill-button rounded-l-full', {
          active: category === 'directory',
        })}
        onClick={() => setCategory({ target: { name: 'category', value: 'directory' } })}
      >
        <FolderIcon className={clsx('search-pill-button-icon', { active: category === 'directory' })} />
        <span className="ml-2">{t('data.folders')}</span>
      </button>
      <button
        className={clsx('search-pill-button rounded-r-full', {
          active: category !== 'directory',
        })}
        onClick={() => setCategory({ target: { name: 'category', value: 'note' } })}
      >
        <FileIcon className={clsx('search-pill-button-icon', { active: category !== 'directory' })} />
        <span className="ml-2">{t('data.notes')}</span>
      </button>
    </div>
  );
};

export default SearchPill;
