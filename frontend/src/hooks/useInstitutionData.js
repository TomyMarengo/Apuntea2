import { useState } from 'react';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsQuery,
} from '../store/slices/institutionsApiSlice';

const useInstitutionData = ({ skipCareers = false, skipSubjects = false }) => {
  const [institutionId, setInstitutionId] = useState(null);
  const [careerId, setCareerId] = useState(null);

  const {
    data: institutions,
  } = useGetInstitutionsQuery();

  const {
    data: careers,
  } = useGetCareersQuery(institutionId, {
    skip: !institutionId || skipCareers,
  });

  const {
    data: subjects,
  } = useGetSubjectsQuery({ institutionId, careerId }, {
    skip: !institutionId || !careerId || skipSubjects,
  });


  return {
    institutions,
    careers,
    subjects,
    setInstitutionId,
    setCareerId,
  };
};

export default useInstitutionData;