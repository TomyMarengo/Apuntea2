/* eslint-disable react-hooks/exhaustive-deps */
import { InputAutocomplete } from '../index';
import { institutionInputs } from '../../constants/forms';
import { isUuid } from '../../functions/utils';
import { useInstitutionData } from '../../hooks/index';
import { useRef } from 'react';
import { useEffect } from 'react';

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
  } = useInstitutionData({
    skipInstitutions,
    skipCareers,
    skipSubjects,
    initialInstitutionId,
    initialCareerId,
    initialSubjectId,
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

  return (
    <>
      {!noInstitution && (
        <InputAutocomplete
          ref={institutionRef}
          {...institutionInputs.find((input) => input.name === 'institutionId')}
          onChange={onChangeInstitution}
          list={institutions}
          autoComplete="off"
        />
      )}
      {!noCareer && (
        <InputAutocomplete
          ref={careerRef}
          {...institutionInputs.find((input) => input.name === 'careerId')}
          onChange={onChangeCareer}
          list={careers}
          autoComplete="off"
        />
      )}
      {!noSubject && (
        <InputAutocomplete
          ref={subjectRef}
          {...institutionInputs.find((input) => input.name === 'subjectId')}
          onChange={onChangeSubject}
          list={subjects}
          autoComplete="off"
        />
      )}
    </>
  );
};

export default InstitutionDataInputs;
