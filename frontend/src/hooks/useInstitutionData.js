import { useState, useEffect, useRef } from 'react';
import {
  useLazyGetInstitutionsQuery,
  useLazyGetInstitutionQuery,
  useLazyGetCareersQuery,
  useLazyGetCareerQuery,
  useLazyGetSubjectsByCareerQuery,
  useLazyGetSubjectQuery
} from '../store/slices/institutionsApiSlice';

const useInstitutionData = ({
  skipInstitutions = false,
  skipCareers = false,
  skipSubjects = false,
  initialInstitutionId,
  initialCareerId,
  initialSubjectId
}) => {
  const [institutionId, setInstitutionId] = useState(initialInstitutionId);
  const [careerId, setCareerId] = useState(initialCareerId);
  const [subjectId, setSubjectId] = useState(initialSubjectId);
  const [institutions, setInstitutions] = useState(null);
  const [institution, setInstitution] = useState(null);
  const [careers, setCareers] = useState(null);
  const [career, setCareer] = useState(null);
  const [subjects, setSubjects] = useState(null);
  const [subject, setSubject] = useState(null);

  const [getInstitutionsTrigger, { isFetching: isFetchingInstitutions }] = useLazyGetInstitutionsQuery();
  const [getInstitutionTrigger, { isFetching: isFetchingInstitution }] = useLazyGetInstitutionQuery();
  const [getCareersTrigger, { isFetching: isFetchingCareers }] = useLazyGetCareersQuery();
  const [getCareerTrigger, { isFetching: isFetchingCareer }] = useLazyGetCareerQuery();
  const [getSubjectsByCareerTrigger, { isFetching: isFetchingSubjectsByCareer }] = useLazyGetSubjectsByCareerQuery();
  const [getSubjectTrigger, { isFetching: isFetchingSubject }] = useLazyGetSubjectQuery();

  const getInstitutions = async () => {
    if (skipInstitutions) return;
    setInstitution(null);
    setCareers(null);
    setSubjects(null);
    setCareer(null);
    setSubject(null);
    setCareerId(null);
    setSubjectId(null);
    const { data } = await getInstitutionsTrigger();
    setInstitutions(data);
  };

  const getInstitution = async () => {
    if (skipInstitutions) return;
    if (!institutionId) {
      setInstitution(null);
      setCareers(null);
      setSubjects(null);
      setCareer(null);
      setSubject(null);
      setCareerId(null);
      setSubjectId(null);
      return;
    }
    const { data } = await getInstitutionTrigger({ institutionId });
    setInstitution(data);
    getCareers();
  };

  const getCareers = async () => {
    if (!institutionId || skipCareers) return;
    setCareer(null);
    setSubjects(null);
    setSubject(null);
    setSubjectId(null);
    const { data } = await getCareersTrigger({ institutionId });
    setCareers(data);
  };

  const getCareer = async () => {
    if (!institutionId || skipCareers) return;
    if (!careerId) {
      setCareer(null);
      setSubjects(null);
      setSubject(null);
      setSubjectId(null);
      return;
    }
    const { data } = await getCareerTrigger({ institutionId, careerId });
    setCareer(data);
    getSubjects();
  };

  const getSubjects = async () => {
    if (!institutionId || !careerId || skipSubjects) return;
    setSubject(null);
    const { data } = await getSubjectsByCareerTrigger({ careerId });
    setSubjects(data);
  };

  const getSubject = async () => {
    if (!institutionId || !careerId || skipSubjects) return;
    if (!subjectId) {
      setSubject(null);
      return;
    }
    const { data } = await getSubjectTrigger({ subjectId });
    setSubject(data);
  };

  useEffect(() => {
    if (institutionId) {
      getInstitution();
    } else {
      getInstitutions();
    }
  }, [institutionId]);

  useEffect(() => {
    if (careerId) {
      getCareer();
    } else {
      getCareers();
    }
  }, [careerId]);

  useEffect(() => {
    if (subjectId) {
      getSubject();
    } else {
      getSubjects();
    }
  }, [subjectId]);

  return {
    institutions,
    institution,
    institutionId,
    setInstitutionId,
    careers,
    career,
    careerId,
    setCareerId,
    subjects,
    subject,
    subjectId,
    setSubjectId,
    isFetchingInstitutions,
    isFetchingInstitution
  };
};

export default useInstitutionData;
