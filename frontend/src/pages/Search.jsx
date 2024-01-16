import { NavLink } from 'react-router-dom';
import useParams from '../hooks/useParams';

import { Pagination, SearchTable } from '../components/index';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';

const Search = () => {
  const { params, query } = useParams();
  const currentPage = Number(params['page']) || 1;
  params['page'] = currentPage;
  const { data, isLoading, error } = useSearchNotesQuery(params);
  const { totalCount, totalPages, ...notes } = data || {};

  console.log(notes);
  return (
    <section className="flex flex-col items-center mt-16 gap-8 text-center">
      <SearchTable data={notes} />
      <Pagination totalPages={totalPages} />
    </section>
  );
};

export default Search;
