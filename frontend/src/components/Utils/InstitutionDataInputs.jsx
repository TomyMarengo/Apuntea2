/* eslint-disable react-hooks/exhaustive-deps */
import { useRef, useEffect } from 'react';

import { institutionInputs } from '../../constants/forms';
import { useInstitutionData } from '../../hooks/index';
import { isUuid } from '../../functions/utils';
import { InputAutocomplete, InputSkeleton } from '../index';

const InstitutionDataInputs = ({
  onChange,
  initialInstitution,
  initialCareer,
  initialSubject,
  skipInstitutions,
  skipCareers,
  skipSubjects,
  noInstitution,
  noCareer,
  noSubject,
  errors,
  ...props
}) => {
  const {
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
    isFetchingSubjects,
  } = useInstitutionData({
    skipInstitutions,
    skipCareers,
    skipSubjects,
    initialInstitution,
    initialCareer,
    initialSubject,
  });

  const institutionRef = useRef(null);
  const careerRef = useRef(null);
  const subjectRef = useRef(null);

  const onChangeInstitution = (e) => {
    if (isUuid(e.target.value)) setInstitution(institutions.find((institution) => institution.id === e.target.value));
    else if (e.target.value === '') setInstitution({});
    onChange(e);
  };

  const onChangeCareer = (e) => {
    if (isUuid(e.target.value)) setCareer(careers.find((career) => career.id === e.target.value));
    else if (e.target.value === '') setCareer({});
    onChange(e);
  };

  const onChangeSubject = (e) => {
    if (isUuid(e.target.value)) setSubject(subjects.find((subject) => subject.id === e.target.value));
    else if (e.target.value === '') setSubject({});
    onChange(e);
  };

  useEffect(() => {
    if (institutionRef.current) {
      if (institution?.name) {
        institutionRef.current.value = institution?.name;
      } else {
        institutionRef.current.value = '';
      }
    }
  }, [institution]);

  useEffect(() => {
    if (careerRef.current) {
      if (career?.name) {
        careerRef.current.value = career?.name;
      } else {
        careerRef.current.value = '';
      }
    }
  }, [career]);

  useEffect(() => {
    if (subjectRef.current) {
      if (subject?.name) {
        subjectRef.current.value = subject?.name;
      } else {
        subjectRef.current.value = '';
      }
    }
  }, [subject]);

  return (
    <>
      {!noInstitution &&
        (!isFetchingInstitutions ? (
          <InputAutocomplete
            defaultValue={institution?.name || ''}
            ref={institutionRef}
            {...institutionInputs.find((input) => input.name === 'institutionId')}
            onChange={onChangeInstitution}
            list={institutions}
            autoComplete="off"
            className={props.className}
            errors={errors?.institutionId}
          />
        ) : (
          <InputSkeleton />
        ))}
      {!noCareer &&
        (!isFetchingCareers ? (
          <InputAutocomplete
            defaultValue={career?.name || ''}
            ref={careerRef}
            {...institutionInputs.find((input) => input.name === 'careerId')}
            onChange={onChangeCareer}
            list={careers}
            autoComplete="off"
            className={props.className}
            errors={errors?.careerId}
          />
        ) : (
          <InputSkeleton />
        ))}
      {!noSubject &&
        (!isFetchingSubjects ? (
          <InputAutocomplete
            defaultValue={subject?.name || ''}
            ref={subjectRef}
            {...institutionInputs.find((input) => input.name === 'subjectId')}
            onChange={onChangeSubject}
            list={subjects}
            autoComplete="off"
            className={props.className}
            errors={errors?.subjectId}
          />
        ) : (
          <InputSkeleton />
        ))}
    </>
  );
};

export default InstitutionDataInputs;
