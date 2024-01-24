import useParams from '../hooks/useParams';
import { useTranslation } from 'react-i18next';

import { BottomNavbar, Pagination, SearchTable, SearchForm } from '../components/index';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';

const Search = () => {
  const { t } = useTranslation();
  const { params, query } = useParams();
  const currentPage = Number(params['page']) || 1;
  params['page'] = currentPage;
  const pageSize = Number(params['pageSize']) || 12;
  const { data, isLoading, error } = useSearchNotesQuery(params);
  const { totalCount, totalPages, notes } = data || {};

  return (
    <section className="flex flex-col items-center mt-16 gap-8 text-center">
      <BottomNavbar title={t('pages.search.title')} to="/search" />
      <SearchForm params={params}/>
      {notes?.length > 0 && (
        <>
          <SearchTable notes={notes} />
          <Pagination
            totalPages={totalPages}
            currentPage={currentPage}
            pageSize={pageSize}
            totalCount={totalCount}
            dataLength={notes.length}
          />
        </>
      )}
    </section>
  );
};

export default Search;
