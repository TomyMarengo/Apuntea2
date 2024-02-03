import { useSearchNotesQuery, useSearchDirectoriesQuery } from '../store/slices/searchApiSlice';
import { useParams, useUserData } from '../hooks/index';
import { useGetCareerQuery, useGetInstitutionQuery, useGetSubjectQuery } from '../store/slices/institutionsApiSlice';
import { isUuid } from '../functions/utils';

const DEFAULT_PAGE_SIZE = 12;
const DEFAULT_PAGE = 1;

const useSearch = () => {
  const { params } = useParams();
  const currentPage = Number(params['page']) || DEFAULT_PAGE;
  params['page'] = currentPage;
  const pageSize = Number(params['pageSize']) || DEFAULT_PAGE_SIZE;
  params['pageSize'] = pageSize;
  params['asc'] = params['asc'] || 'true';
  params['sortBy'] = params['sortBy'] || 'modified';
  params['category'] = params['category'] || 'note';
  params['word'] = params['word'] || '';

  const { data: dataNotes, isLoading: isLoadingSearchNotes } = useSearchNotesQuery(params, {
    skip: params['category'] === 'directory',
  });
  const { totalCount: totalCountNotes, totalPages: totalPagesNotes, notes } = dataNotes || {};

  const { data: dataDirectories, isLoading: isLoadingSearchDirectories } = useSearchDirectoriesQuery(params, {
    skip: params['category'] !== 'directory',
  });

  const { totalCount: totalCountDirectories, totalPages: totalPagesDirectories, directories } = dataDirectories || {};

  const { data: institution, isLoading: isLoadingInstitution } = useGetInstitutionQuery(
    { institutionId: params['institutionId'] },
    {
      skip: !params['institutionId'] || !isUuid(params['institutionId']),
    }
  );
  const { data: career, isLoading: isLoadingCareer } = useGetCareerQuery(
    { careerId: params['careerId'], institutionId: params['institutionId'] },
    {
      skip:
        !params['careerId'] ||
        !params['institutionId'] ||
        !isUuid(params['careerId']) ||
        !isUuid(params['institutionId']),
    }
  );
  const { data: subject, isLoading: isLoadingSubject } = useGetSubjectQuery(
    { subjectId: params['subjectId'] },
    {
      skip: !params['subjectId'] || !isUuid(params['subjectId']),
    }
  );
  const { institution: institutionUser, career: careerUser, isLoading: isLoadingUserData } = useUserData();

  params['institutionId'] = institution ? institution.id : institutionUser ? institutionUser.id : '';
  params['careerId'] = career ? career.id : careerUser ? careerUser.id : '';
  params['subjectId'] = subject ? subject.id : '';

  return {
    isLoadingInputs: isLoadingInstitution || isLoadingCareer || isLoadingSubject || isLoadingUserData,
    isLoadingData: isLoadingSearchNotes || isLoadingSearchDirectories,
    notes: notes || [],
    directories: directories || [],
    totalCountNotes: Number(totalCountNotes) || 0,
    totalPagesNotes: Number(totalPagesNotes) || 0,
    totalCountDirectories: Number(totalCountDirectories) || 0,
    totalPagesDirectories: Number(totalPagesDirectories) || 0,
    institution: institution || career ? institution : institutionUser,
    career: institution || career ? career : careerUser,
    subject,
    params
  };
}

export default useSearch;