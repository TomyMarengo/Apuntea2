import { useTranslation } from 'react-i18next';

import { ArrowUpIcon, ArrowDownIcon } from '../Utils/Icons';

const SortBySelect = ({ sortBy, setSortBy, asc, setAsc }) => {
  const { t } = useTranslation();

  const handleAsc = () => {
    setAsc({ target: { name: 'asc', value: asc === 'true' ? 'false' : 'true' } });
  };

  const handleSortBy = (e) => {
    setSortBy({ target: { name: 'sortBy', value: e.target.value } });
  };

  return (
    <div className="flex">
      <label htmlFor="sortBy" className="sr-only">
        {t('data.sortBy')}
      </label>
      <button
        type="button"
        className="border border-text/20 border-r-0 bg-dark-bg/20 rounded-l-md px-3 py-2"
        onClick={(e) => handleAsc(e)}
      >
        {asc === 'true' ? <ArrowUpIcon className="icon-s fill-pri" /> : <ArrowDownIcon className="icon-s fill-pri" />}
      </button>
      <select
        id="sortBy"
        name="sortBy"
        value={sortBy}
        onChange={(e) => handleSortBy(e)}
        className="rounded-l-none border-r-8 border-r-transparent focus:border-r"
      >
        <option value="name">{t('data.name')}</option>
        <option value="modified">{t('data.lastModifiedAt')}</option>
        <option value="score">{t('data.score')}</option>
        <option value="date">{t('data.createdAt')}</option>
      </select>
    </div>
  );
};

export default SortBySelect;
