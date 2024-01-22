import { useSelector } from 'react-redux';

import { Card, ProfileForm } from '../components/index';
import { selectCurrentUserId } from '../store/slices/authSlice';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useGetCareerQuery, useGetInstitutionQuery } from '../store/slices/institutionsApiSlice';

const Profile = () => {
  const userId = useSelector(selectCurrentUserId);

  const { data: user, isLoading: isLoadingUser } = useGetUserQuery(userId);
  const { data: career, isLoading: isLoadingInstitution } = useGetInstitutionQuery(
    { url: user?.career },
    { skip: !user }
  );
  const { data: institution, isLoading: isLoadingCareer } = useGetCareerQuery(
    { url: user?.institution },
    { skip: !user }
  );

  return (
    <section className="max-container center">
      {isLoadingUser || isLoadingInstitution || isLoadingCareer ? (
        <span>...</span>
      ) : (
        <Card>
          <ProfileForm user={user} institution={institution} career={career} />
        </Card>
      )}
    </section>
  );
};

export default Profile;
