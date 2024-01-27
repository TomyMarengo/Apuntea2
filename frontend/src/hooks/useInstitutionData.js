import { useState, useEffect } from 'react';
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
  useGetSubjectsByCareerQuery,
} from '../store/slices/institutionsApiSlice';

const useInstitutionData = ({
  skipInstitutions = false,
  skipCareers = false,
  skipSubjects = false,
  initialInstitution,
  initialCareer,
  initialSubject
}) => {
  const [institution, setInstitution] = useState(initialInstitution);
  const [career, setCareer] = useState(initialCareer);
  const [subject, setSubject] = useState(initialSubject);

  let { currentData: institutions, isFetching: isFetchingInstitutions } = useGetInstitutionsQuery(
    {},
    { skip: skipInstitutions }
  );

  let { currentData: careers, isFetching: isFetchingCareers } = useGetCareersQuery(
    { url: institution?.careers },
    {
      skip: skipCareers || !institution?.careers,
      refetchOnMountOrArgChange: institution
    }
  );

  let { currentData: subjects, isFetching: isFetchingSubjectsByCareer } = useGetSubjectsByCareerQuery(
    { url: career?.subjects },
    {
      skip: skipSubjects || !career?.subjects,
      refetchOnMountOrArgChange: career
    }
  );

  useEffect(() => {
    if (institution?.name) return;
    setCareer({});
    setSubject({});
  }, [institution]);

  useEffect(() => {
    if (career?.name) return;
    setSubject({});
  }, [career]);

  return {
    institution,
    institutions,
    setInstitution,
    isFetchingInstitutions,
    career,
    careers,
    setCareer,
    isFetchingCareers,
    subject,
    subjects,
    setSubject,
    isFetchingSubjects: isFetchingSubjectsByCareer,
  };
};

export default useInstitutionData;
