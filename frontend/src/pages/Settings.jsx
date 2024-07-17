import { Card, ChangePasswordForm } from '../components/index';
import { useUserData } from '../hooks/index';

const Settings = () => {
  const { user, isLoading } = useUserData();

  return (
    <section className="max-container center">
      {isLoading ? (
        <span>...</span>
      ) : (
        <div className='settings-cards'>
          <Card>
            <ChangePasswordForm user={user}/>
          </Card>
          <Card>

          </Card>
        </div>
      )}
    </section>
  );
};

export default Settings;
