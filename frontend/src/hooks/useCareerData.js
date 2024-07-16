import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../store/slices/authSlice';
import { useGetCareerQuery } from '../store/slices/institutionsApiSlice';

const useCareerData = () => {
  const user = useSelector(selectCurrentUser);
  const { data: career, isLoading: isLoadingCareer } = useGetCareerQuery({
    url: user.career,
  });
  return { career, isLoadingCareer };
};

export default useCareerData;
