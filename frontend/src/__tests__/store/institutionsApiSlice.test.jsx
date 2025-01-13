import { setupApiStore } from '../setup/utils';
import { describe, it, expect } from 'vitest';
import {
  useGetInstitutionsQuery,
  useGetInstitutionQuery,
  useGetCareersQuery,
  useGetCareerQuery,
  useGetSubjectQuery,
  useCreateSubjectMutation,
  useUpdateSubjectMutation,
  useGetSubjectsNotInCareerQuery,
  useGetSubjectsByCareerQuery,
  useLinkSubjectCareerMutation,
  useGetSubjectCareersQuery,
  useGetCareerSubjectsByYearQuery,
  institutionsApiSlice,
} from '../../store/slices/institutionsApiSlice';
import { useWithWrapper } from '../setup/wrapper.jsx';
import { waitFor } from '@testing-library/react';
import {
  someInstitutionId,
  someCareerId,
  someSubjectId,
  emptyNameMsg,
  invalidYearMsg,
  invalidName,
} from '../mocks/institutionsApiMocks';

// Set up the API store for institutions
function setupInstitutionsApiStore() {
  return setupApiStore(institutionsApiSlice);
}

const store = setupInstitutionsApiStore();

describe('institutionsApiSlice', () => {
  it('should fetch institutions successfully', async () => {
    const { result } = useWithWrapper(() => useGetInstitutionsQuery(), store);
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const institutions = result.current.data;
    expect(institutions).toEqual(expect.any(Array));
  });

  it('should fetch a single institution by ID successfully', async () => {
    const { result } = useWithWrapper(
      () => useGetInstitutionQuery({ institutionId: someInstitutionId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const institution = result.current.data;
    expect(institution.id).toBe(someInstitutionId);
  });

  it('should fetch careers by institution successfully', async () => {
    const { result } = useWithWrapper(
      () => useGetCareersQuery({ institutionId: someInstitutionId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const careers = result.current.data;
    expect(careers).toEqual(expect.any(Array));
  });

  it('should fetch a career by ID successfully', async () => {
    const { result } = useWithWrapper(
      () =>
        useGetCareerQuery({
          institutionId: someInstitutionId,
          careerId: someCareerId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const career = result.current.data;
    expect(career.id).toBe(someCareerId);
  });

  it('should fetch a subject by ID successfully', async () => {
    const subjectId = 'someSubjectId';
    const { result } = useWithWrapper(
      () => useGetSubjectQuery({ subjectId: someSubjectId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const subject = result.current.data;
    expect(subject.id).toBe(someSubjectId);
  });

  it('should create a subject successfully', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useCreateSubjectMutation(),
      store,
    );
    const [createSubject] = await wrapperResult.current;
    const result = await createSubject({
      name: 'New Subject',
      year: 1,
      institutionId: someInstitutionId,
      careerId: someCareerId,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to create subject due to empty name, should relay errors', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useCreateSubjectMutation(),
      store,
    );
    const [createSubject] = await wrapperResult.current;
    const result = await createSubject({
      name: '',
      year: 1,
      institutionId: someInstitutionId,
      careerId: someCareerId,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(emptyNameMsg);
  });

  it('should fail to create subject due to invalid year, should relay errors', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useCreateSubjectMutation(),
      store,
    );
    const [createSubject] = await wrapperResult.current;
    const result = await createSubject({
      name: 'New Subject',
      year: -5,
      institutionId: someInstitutionId,
      careerId: someCareerId,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(invalidYearMsg);
  });

  it('should modify a subject successfully', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useUpdateSubjectMutation(),
      store,
    );
    const [updateSubject] = await wrapperResult.current;
    const result = await updateSubject({
      name: 'New Subject',
      year: 2025,
      institutionId: someInstitutionId,
      careerId: someCareerId,
      subjectId: someSubjectId,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to modify subject due to empty name, should relay errors', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useUpdateSubjectMutation(),
      store,
    );
    const [updateSubject] = await wrapperResult.current;
    const result = await updateSubject({
      name: invalidName,
      year: 1,
      institutionId: someInstitutionId,
      careerId: someCareerId,
      subjectId: someSubjectId,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(emptyNameMsg);
  });

  it('should fail to modify subject due to invalid year, should relay errors', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useUpdateSubjectMutation(),
      store,
    );
    const [updateSubject] = await wrapperResult.current;
    const result = await updateSubject({
      name: 'New Subject',
      year: -5,
      institutionId: someInstitutionId,
      careerId: someCareerId,
      subjectId: someSubjectId,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(invalidYearMsg);
  });

  it('should get subjects not in career', async () => {
    const { result } = useWithWrapper(
      () => useGetSubjectsNotInCareerQuery({ careerId: someCareerId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const subjects = result.current.data;
    expect(subjects).not.empty;
  });

  it('should get subjects by career', async () => {
    const { result } = useWithWrapper(
      () => useGetSubjectsByCareerQuery({ careerId: someCareerId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const subjects = result.current.data;
    expect(subjects).not.empty;
  });

  it('should link subject with career successfully', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useLinkSubjectCareerMutation(),
      store,
    );
    const [linkSubject] = await wrapperResult.current;
    const result = await linkSubject({
      year: 1,
      institutionId: someInstitutionId,
      careerId: someCareerId,
      subjectId: someSubjectId,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to link subject with career due to invalid year, should relay errors', async () => {
    const { result: wrapperResult } = useWithWrapper(
      () => useLinkSubjectCareerMutation(),
      store,
    );
    const [linkSubject] = await wrapperResult.current;
    const result = await linkSubject({
      year: -5,
      institutionId: someInstitutionId,
      careerId: someCareerId,
      subjectId: someSubjectId,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(invalidYearMsg);
  });

  it("should get subject's careers", async () => {
    const { result } = useWithWrapper(
      () =>
        useGetSubjectCareersQuery({
          institutionId: someInstitutionId,
          careerId: someCareerId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const subjectCareers = result.current.data;
    expect(subjectCareers).not.empty;
  });

  it("should get career's subjects by year", async () => {
    const { result } = useWithWrapper(
      () =>
        useGetCareerSubjectsByYearQuery({ careerId: someCareerId, year: 1 }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const subjects = result.current.data;
    expect(subjects).not.empty;
  });
});

//--useGetInstitutionsQuery,
//--useGetInstitutionQuery,
//--useGetCareersQuery,
//--useGetCareerQuery,
//--useGetSubjectQuery,
//--useCreateSubjectMutation,
//--useUpdateSubjectMutation,
//--useGetSubjectsNotInCareerQuery,
//--useGetSubjectsByCareerQuery,
//--useLinkSubjectCareerMutation,
//  useUnlinkSubjectCareerMutation,
//--useGetSubjectCareersQuery,
//--useGetCareerSubjectsByYearQuery,
