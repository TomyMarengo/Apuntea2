import { useTranslation } from 'react-i18next';
import { useEffect } from 'react';

import { BottomNavbar, Pagination, SearchTable, SearchForm } from '../components/index';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';
import { useParams, useUserData } from '../hooks/index';
import { useGetCareerQuery, useGetInstitutionQuery, useGetSubjectQuery } from '../store/slices/institutionsApiSlice';
import { isUuid } from '../functions/utils';

const DEFAULT_PAGE_SIZE = 12;
const DEFAULT_PAGE = 1;

const Search = () => {
  const { t } = useTranslation();
  const { params } = useParams();
  const currentPage = Number(params['page']) || DEFAULT_PAGE;
  params['page'] = currentPage;
  const pageSize = Number(params['pageSize']) || DEFAULT_PAGE_SIZE;
  params['pageSize'] = pageSize;
  const { data, isLoading: isLoadingSearchNotes } = useSearchNotesQuery(params);
  const { totalCount, totalPages, notes } = data || {};
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

  return (
    <section className="max-container center gap-8">
      <BottomNavbar title={t('pages.search.title')} to="/search" />
      {!isLoadingUserData && !isLoadingInstitution && !isLoadingCareer && !isLoadingSubject && (
        <SearchForm
          params={params}
          institution={institution || career ? institution : institutionUser}
          career={institution || career ? career : careerUser}
          subject={subject}
        />
      )}
      {notes?.length > 0 && (
        <>
          <SearchTable notes={notes} />
          <Pagination
            totalPages={totalPages}
            currentPage={currentPage}
            pageSize={pageSize}
            totalCount={totalCount}
            dataLength={notes.length}
          />
        </>
      )}
    </section>
  );
};

export default Search;
