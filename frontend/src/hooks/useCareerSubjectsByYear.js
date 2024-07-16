import { useGetCareerSubjectsByYearQuery } from '../store/slices/institutionsApiSlice';

const useCareerSubjectsByYear = (careerId, year) => {
  const { data, isLoading } = useGetCareerSubjectsByYearQuery({ careerId, year }, { skip: !careerId });
  return { data, isLoading };
};

export default useCareerSubjectsByYear;
