import { useTranslation } from 'react-i18next';

import { BottomNavbar, Pagination, SearchTable, SearchForm } from '../components/index';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';
import { useParams, useUserData } from '../hooks/index';

const DEFAULT_PAGE_SIZE = 12;
const DEFAULT_PAGE = 1;

const Search = () => {
  const { t } = useTranslation();
  const { params } = useParams();
  const currentPage = Number(params['page']) || DEFAULT_PAGE;
  params['page'] = currentPage;
  const pageSize = Number(params['pageSize']) || DEFAULT_PAGE_SIZE;
  params['pageSize'] = pageSize;
  const { data, isLoading: isLoadingSearchNotes } = useSearchNotesQuery(params);
  const { totalCount, totalPages, notes } = data || {};
  const { user, institution, career, isLoading: isLoadingUserData } = useUserData();

  return (
    <section className="flex flex-col items-center mt-16 gap-8 text-center">
      <BottomNavbar title={t('pages.search.title')} to="/search" />
      {!isLoadingUserData && <SearchForm params={params} user={user} institution={institution} career={career} />}
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
