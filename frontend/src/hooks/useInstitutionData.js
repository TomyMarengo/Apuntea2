import { useState } from 'react';
import { useGetInstitutionsQuery, useGetCareersQuery, useGetSubjectsQuery } from '../store/slices/institutionsApiSlice';

const useInstitutionData = ({
  skipInstitution = false,
  skipCareers = false,
  skipSubjects = false,
  initialInstitutionId,
  initialCareerId,
}) => {
  const [institutionId, setInstitutionId] = useState(initialInstitutionId);
  const [careerId, setCareerId] = useState(initialCareerId);

  const { data: institutions } = useGetInstitutionsQuery(null, {
    skip: skipInstitution,
  });

  const { data: careers } = useGetCareersQuery(
    { institutionId },
    {
      skip: !institutionId || skipCareers,
    }
  );

  const { data: subjects } = useGetSubjectsQuery(
    { institutionId, careerId },
    {
      skip: !institutionId || !careerId || skipSubjects,
    }
  );

  return {
    institutions,
    careers,
    subjects,
    setInstitutionId,
    setCareerId,
  };
};

export default useInstitutionData;
