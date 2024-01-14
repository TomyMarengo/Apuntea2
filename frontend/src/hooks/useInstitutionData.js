import { useEffect, useState } from 'react';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsQuery,
} from '../store/slices/institutionsApiSlice';

const useInstitutionData = () => {
  const [institutions, setInstitutions] = useState(null);

  // Queries
  const {
    data: institutionsFetched,
    isSuccess: institutionsSuccess,
  } = useGetInstitutionsQuery();

  useEffect(() => {
    if (institutionsSuccess) {
      setInstitutions(institutionsFetched);
    }
  }, [institutionsSuccess, institutionsFetched]);

  /* const {
    data: careers,
    error: careersError,
    isLoading: careersLoading,
    isSuccess: careersSuccess,
  } = useGetCareersQuery(selectedInstitutionId, {
    skip: !selectedInstitutionId || !institutionsSuccess,
    refetchOnReconnect: true,
  });

  const {
    data: subjects,
    error: subjectsError,
    isLoading: subjectsLoading,
  } = useGetSubjectsQuery(selectedInstitutionId, selectedCareerId, {
    skip: !selectedCareerId || !careersSuccess,
    refetchOnReconnect: true,
  }); */

  return {
    institutions,
    setInstitutions,
  };
};

export default useInstitutionData;
