import { useSelector } from 'react-redux';

import { useGetUserQuery, useUpdateUserMutation } from '../store/slices/usersApiSlice';
import { useGetCareerQuery, useGetInstitutionQuery } from '../store/slices/institutionsApiSlice';
import { selectCurrentUserId } from '../store/slices/authSlice';
import { useTranslation } from 'react-i18next';

const useUserData = () => {
  const userId = useSelector(selectCurrentUserId);
  const { t } = useTranslation(); 

  const { data: user, isLoading: isLoadingUser } = useGetUserQuery({ userId }, { skip: !userId });

  const { data: career, isLoading: isLoadingInstitution } = useGetInstitutionQuery(
    { url: user?.career },
    { skip: !user || !userId }
  );

  const { data: institution, isLoading: isLoadingCareer } = useGetCareerQuery(
    { url: user?.institution },
    { skip: !user || !userId }
  );

  const isLoading = isLoadingUser || isLoadingInstitution || isLoadingCareer;

  const [update, { isLoading: isLoadingUpdate }] = useUpdateUserMutation();

  
  const updateUser = async (userInfo) => {
    try {
      await update(userInfo).unwrap();
    } catch (error) {
      if (error.message === 'InvalidPassword') {
        throw new Error(t('errors.invalidPassword'));
      }
      return;
      // throw new Error();
    }
  };


  return { user, career, institution, isLoadingUser, isLoadingInstitution, isLoadingCareer, isLoading, updateUser, isLoadingUpdate };
}

export default useUserData;