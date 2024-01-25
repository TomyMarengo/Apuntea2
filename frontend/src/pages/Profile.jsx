import { Card, ProfileForm } from '../components/index';
import { useUserData } from '../hooks/index';

const Profile = () => {
  const { user, institution, career, isLoading } = useUserData();

  return (
    <section className="max-container center">
      {isLoading ? (
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
