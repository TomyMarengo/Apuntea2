import { useTranslation } from 'react-i18next';
import { isUuid } from '../functions/validation';
import { Input, Button } from './index';
import { profileInputs } from '../constants/forms';
import { useForm, useInstitutionData } from '../hooks/index';
import EditableImage from './EditableImage';
import { useUpdateUserMutation } from '../store/slices/usersApiSlice';

const ProfileForm = ({ user, institution, career }) => {
  const [updateUser, { isLoading: isLoadingUpdate }] = useUpdateUserMutation();
  const { t } = useTranslation();

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      userId: user.id,
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      careerId: career.id,
    },
    submitCallback: updateUser,
  });

  const { careers, setCareerId } = useInstitutionData({
    skipInstitution: true,
    initialInstitutionId: institution.id,
  });

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.profile.title')}</h1>
      <form onSubmit={handleSubmit} className="gap-5 w-full profile-form">
        <EditableImage className="profile-picture" profilePictureUrl={user.profilePicture} onChange={handleChange} />
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
            <span>{institution.name}</span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.career')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'careerId')}
              value={career.name}
              hiddenValue={form.careerId}
              onChange={(e) => {
                handleChange(e);
                if (isUuid(e.target.value) || e.target.value === '') setCareerId(e.target.value);
              }}
              list={careers}
              autoComplete="off"
            />
          </div>
        </div>
        <Button type="submit" className="profile-footer">
          {isLoadingUpdate ? t('actions.loading') : t('actions.save')}
        </Button>
      </form>
    </div>
  );
};

export default ProfileForm;
