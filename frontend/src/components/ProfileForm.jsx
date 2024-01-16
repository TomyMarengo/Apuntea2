import { useTranslation } from 'react-i18next';
import { isUuid } from '../functions/validation';
import { Input, Button } from './index';
import { profileInputs } from '../constants/forms';
import { useForm, useInstitutionData } from '../hooks/index';
import { selectCurrentUser, setCredentials } from '../store/slices/authSlice';
import EditableImage from './EditableImage';
import { useSelector } from 'react-redux';
import { useUpdateUserMutation } from '../store/slices/usersApiSlice';

const ProfileForm = () => {
  const [updateUser, { isLoading }] = useUpdateUserMutation();
  const { t } = useTranslation();
  const user = useSelector(selectCurrentUser);

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      userId: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      careerId: user.career.id,
      // profilePicture: user.profilePicture,
    },
    submitCallback: updateUser,
    dispatchCallback: setCredentials,
  });

  const { careers, setCareerId } = useInstitutionData({
    skipInstitution: true,
    initialInstitutionId: user.institution.id,
  });

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.profile.title')}</h1>
      <form onSubmit={handleSubmit} className="gap-5 w-full profile-form">
        <EditableImage className="profile-picture" />
        <div className="profile-inputs">
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.name')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'firstName')}
              value={form.firstName}
              onChange={handleChange}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.lastName')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'lastName')}
              value={form.lastName}
              onChange={handleChange}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.username')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'username')}
              value={form.username}
              onChange={handleChange}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.institution')}</span>
            <span>{user.institution.name}</span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.career')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'careerId')}
              value={user.career.name} //career name
              hiddenValue={form.careerId} //career id
              onChange={(e) => {
                handleChange(e);
                if (isUuid(e.target.value) || e.target.value === '') setCareerId(e.target.value);
              }}
              list={careers}
              autoComplete="off"
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.email')}</span>
            <span>{user.email}</span>
          </div>
        </div>
        <Button type="submit" className="profile-footer">
          Save
        </Button>
      </form>
    </div>
  );
};

export default ProfileForm;
