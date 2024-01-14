import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../store/slices/authSlice';
import { Input, Button } from './index';
import { profileInputs } from '../constants/forms';
import { useForm, useAuth } from '../hooks/index';
import EditableImage from './EditableImage';
// import { useProfileMutation } from '../store/slices/authApiSlice';
const ProfileForm = () => {
  // const [profile, { isLoading }] = useProfileMutation();
  const { t } = useTranslation();

  const { form, error, handleChange, handleSubmit } = useForm(
    {
      firstName: '',
      lastName: '',
      username: '',
      careerId: '',
    },
    // profile,
    setCredentials,
    '/'
  );
  // const { careers, setCareers } = useCareerData();

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('profile.title')}</h1>
      <form className="gap-5 w-full profile-form">
        {/* <p className={error ? 'errmsg' : 'offscreen'} aria-live="assertive">
            {error}
          </p> */}
        <EditableImage className="profile-picture" />
        <div className="profile-inputs">
          <div className="profile-item">
            {t('data.name')}
            <Input
              {...profileInputs.find((input) => input.name === 'firstName')}
              value={form.firstName}
              onChange={handleChange}
            />
          </div>
          <div className="profile-item">
            {t('data.lastName')}
            <Input
              {...profileInputs.find((input) => input.name === 'lastName')}
              value={form.lastName}
              onChange={handleChange}
            />
          </div>
          <div className="profile-item">
            {t('data.username')}
            <Input
              {...profileInputs.find((input) => input.name === 'username')}
              value={form.username}
              onChange={handleChange}
            />
          </div>
          <div className="profile-item">{t('data.institution')}</div>
          <div className="profile-item">
            {t('data.career')}
            <Input
              {...profileInputs.find((input) => input.name === 'careerId')}
              value={form.careerId}
              onChange={handleChange}
            />
          </div>
          <div className="profile-item">{t('data.email')}</div>
        </div>
        <Button type="submit" className="profile-footer">
          Save
        </Button>
      </form>
    </div>
  );
};

export default ProfileForm;
