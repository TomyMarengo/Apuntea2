import { useTranslation } from 'react-i18next';

import { useForm, useUserData } from '../../hooks/index';
import { useState } from 'react';

const OtherConfigurationsForm = ({ user }) => {
  const { t } = useTranslation();
  const { updateUser } = useUserData();
  const [notificationsEnabled, setNotificationsEnabled] = useState(user.notificationsEnabled);

  const { handleChange, handleSubmit } = useForm({
    submitCallback: updateUser,
    args: {
      userId: user.id,
    },
  });

  const handleAll = async (e) => {
    setNotificationsEnabled(e.target.checked);
    handleChange({ target: { name: e.target.name, value: e.target.checked }});
    await handleSubmit(new Event('submit')); // Hack
  }

  // TODO: Add toggle instead of checkbox

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.settings.otherConfigurations.title')}</h1>
      <form onSubmit={handleSubmit} className="gap-5 w-full settings-form">
        <div className="settings-input-row w-full">
          <span className="font-bold">{t('pages.settings.otherConfigurations.receiveMails')}</span>
            <input type='checkbox' name="notificationsEnabled" onChange={handleAll} checked={notificationsEnabled}/>
        </div>
      </form>
    </div>

  );
};

export default OtherConfigurationsForm;
