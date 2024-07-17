import { Card, ChangePasswordForm, OtherConfigurationsForm } from '../components/index';
import { useUserData } from '../hooks/index';

const Settings = () => {
  const { user, isLoading } = useUserData();

  return (
    <section className="max-container center">
      {isLoading ? (
        <span>...</span>
      ) : (
        <section className='settings-cards'>
          <Card>
            <ChangePasswordForm user={user}/>
          </Card>
          <Card>
            <OtherConfigurationsForm user={user}/>
          </Card>
        </section>
      )}
    </section>
  );
};

export default Settings;
