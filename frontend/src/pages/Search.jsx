import { useTranslation } from 'react-i18next';

import { BottomNavbar, Pagination, SearchTable, SearchForm } from '../components/index';
import { useSearch } from '../hooks/index';

const Search = () => {
  const { t } = useTranslation();
  const {
    notes,
    directories,
    totalCountNotes,
    totalPagesNotes,
    totalCountDirectories,
    totalPagesDirectories,
    institution,
    career,
    subject,
    isLoadingInputs,
    isLoadingData,
    params,
  } = useSearch();

  return (
    <section className="max-container center gap-8">
      <BottomNavbar title={t('pages.search.title')} to="/search" />

      {!isLoadingInputs && <SearchForm params={params} institution={institution} career={career} subject={subject} />}

      {!isLoadingData &&
        (notes?.length > 0 ? (
          <>
            <SearchTable params={params} notes={notes} directories={directories} />
            <Pagination
              totalPages={Math.max(totalPagesNotes, totalPagesDirectories)}
              currentPage={params['page']}
              pageSize={params['pageSize']}
              totalCount={totalCountNotes + totalCountDirectories}
              dataLength={notes?.length + directories?.length}
            />
          </>
        ) : (
          <span>No content</span> /* TODO: i18n */
        ))}
    </section>
  );
};

export default Search;
