import { useTranslation } from 'react-i18next';

import { useUserData } from '../../hooks/index';
import { useState } from 'react';

const OtherConfigurationsForm = ({ user }) => {
  const { t } = useTranslation();
  const { updateUser } = useUserData();
  const [notificationsEnabled, setNotificationsEnabled] = useState(user.notificationsEnabled || false);

  // const { handleChange, handleSubmit } = useForm({
  //   submitCallback: updateUser,
  //   args: {
  //     userId: user.id,
  //   },
  // });

  const handleAll = async (e) => {
    setNotificationsEnabled(e.target.checked);
    updateUser({ userId: user.id, notificationsEnabled: e.target.checked });
  }

  // TODO: Add toggle instead of checkbox

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.settings.otherConfigurations.title')}</h1>
      <form className="gap-5 w-full settings-form">
        <div className="settings-input-row w-full">
          <span className="font-bold">{t('pages.settings.otherConfigurations.receiveMails')}</span>
            <input type='checkbox' name="notificationsEnabled" onChange={handleAll} checked={notificationsEnabled}/>
        </div>
      </form>
    </div>

  );
};

export default OtherConfigurationsForm;
