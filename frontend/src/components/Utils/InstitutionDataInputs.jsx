/* eslint-disable react-hooks/exhaustive-deps */
import { useRef, useEffect } from 'react';

import { institutionInputs } from '../../constants/forms';
import { useInstitutionData } from '../../hooks/index';
import { isUuid } from '../../functions/utils';
import { InputAutocomplete, InputSkeleton } from '../index';

const InstitutionDataInputs = ({
  onChange,
  initialInstitutionId,
  initialCareerId,
  initialSubjectId,
  skipInstitutions,
  skipCareers,
  skipSubjects,
  noInstitution,
  noCareer,
  noSubject,
}) => {
  const {
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
    isFetchingInstitution,
  } = useInstitutionData({
    skipInstitutions,
    skipCareers,
    skipSubjects,
    initialInstitutionId: initialInstitutionId || '',
    initialCareerId: initialCareerId || '',
    initialSubjectId: initialSubjectId || '',
  });

  const institutionRef = useRef(null);
  const careerRef = useRef(null);
  const subjectRef = useRef(null);

  const onChangeInstitution = (e) => {
    if (isUuid(e.target.value)) setInstitutionId(e.target.value);
    else setInstitutionId('');
    onChange(e);
  };

  const onChangeCareer = (e) => {
    if (isUuid(e.target.value)) setCareerId(e.target.value);
    else setCareerId('');
    onChange(e);
  };

  const onChangeSubject = (e) => {
    if (isUuid(e.target.value)) setSubjectId(e.target.value);
    else setSubjectId('');
    onChange(e);
  };

  useEffect(() => {
    if (institutionRef.current) {
      if (institutionId) {
        institutionRef.current.value = institution?.name;
      } else {
        institutionRef.current.value = '';
      }
    }
  }, [institution]);

  useEffect(() => {
    if (careerRef.current) {
      if (careerId) {
        careerRef.current.value = career?.name;
      } else {
        careerRef.current.value = '';
      }
    }
  }, [career]);

  useEffect(() => {
    if (subjectRef.current) {
      if (subjectId) {
        subjectRef.current.value = subject?.name;
      } else {
        subjectRef.current.value = '';
      }
    }
  }, [subject]);

  useEffect(() => {
    console.log(isFetchingInstitution);
  }, [isFetchingInstitution]);

  return (
    <>
      {!noInstitution &&
        (!isFetchingInstitution ? (
          <InputAutocomplete
            defaultValue={institution?.name || ''}
            ref={institutionRef}
            {...institutionInputs.find((input) => input.name === 'institutionId')}
            onChange={onChangeInstitution}
            list={institutions}
            autoComplete="off"
          />
        ) : (
          <InputSkeleton />
        ))}
      {!noCareer &&
        (!isFetchingInstitution ? (
          <InputAutocomplete
            defaultValue={career?.name || ''}
            ref={careerRef}
            {...institutionInputs.find((input) => input.name === 'careerId')}
            onChange={onChangeCareer}
            list={careers}
            autoComplete="off"
          />
        ) : (
          <InputSkeleton />
        ))}
      {!noSubject &&
        (!isFetchingInstitution ? (
          <InputAutocomplete
            defaultValue={subject?.name || ''}
            ref={subjectRef}
            {...institutionInputs.find((input) => input.name === 'subjectId')}
            onChange={onChangeSubject}
            list={subjects}
            autoComplete="off"
          />
        ) : (
          <InputSkeleton />
        ))}
    </>
  );
};

export default InstitutionDataInputs;
