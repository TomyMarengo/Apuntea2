import { NavLink } from 'react-router-dom';
import useParams from '../hooks/useParams';

import { Pagination } from '../components/index';
import { useSearchQuery } from '../store/slices/searchApiSlice';

const Search = () => {
  const searchParams = useParams();
  console.log('params:', searchParams);

  return <Pagination totalPages={7} />;
};

export default Search;
