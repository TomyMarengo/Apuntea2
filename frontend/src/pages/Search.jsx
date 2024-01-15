import { useSearchParams, NavLink } from 'react-router-dom';

import { Pagination } from '../components/index';

const Search = () => {
  const [searchParams] = useSearchParams();

  return <Pagination totalPages={totalPages} />;
};

export default Search;
