import { useSelector } from 'react-redux';

import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useGetCareerQuery, useGetInstitutionQuery } from '../store/slices/institutionsApiSlice';
import { selectCurrentUserId } from '../store/slices/authSlice';

const useUserData = () => {
  const userId = useSelector(selectCurrentUserId);

  const { data: user, isLoading: isLoadingUser } = useGetUserQuery({ userId }, { skip: !userId });

  const { data: career, isLoading: isLoadingInstitution } = useGetInstitutionQuery(
    { url: user?.career },
    { skip: !user }
  );

  const { data: institution, isLoading: isLoadingCareer } = useGetCareerQuery(
    { url: user?.institution },
    { skip: !user }
  );

  const isLoading = isLoadingUser || isLoadingInstitution || isLoadingCareer;

  return { user, career, institution, isLoadingUser, isLoadingInstitution, isLoadingCareer, isLoading};
}

export default useUserData;