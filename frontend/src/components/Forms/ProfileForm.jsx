import { useTranslation } from 'react-i18next';

import { useUpdateUserMutation, useUpdatePictureMutation } from '../../store/slices/usersApiSlice';
import { useForm } from '../../hooks/index';
import { Input, Button, EditableImage, InstitutionDataInputs } from '../index';
import { profileInputs } from '../../constants/forms';
import { PatchUserSchema } from '../../constants/schemas';

const ProfileForm = ({ user, institution, career }) => {
  const [updateUser, { isLoading: isLoadingUpdate }] = useUpdateUserMutation();
  const [updatePicture, { isLoading: isLoadingPicture }] = useUpdatePictureMutation();
  const { t } = useTranslation();

  const update = ({ userId, profilePicture, ...args }) => {
    if (Object.keys(args).length > 0) updateUser({userId, ...args});
    if (profilePicture) updatePicture({ profilePicture });
  };

  const { handleChange, handleSubmit, errors } = useForm({
    submitCallback: update,
    args: {
      userId: user.id,
    },
    schema: PatchUserSchema,
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
              defaultValue={user.firstName}
              onChange={handleChange}
              errors={errors?.firstName}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.lastName')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'lastName')}
              defaultValue={user.lastName}
              onChange={handleChange}
              errors={errors?.lastName}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.username')}</span>
            <Input
              {...profileInputs.find((input) => input.name === 'username')}
              defaultValue={user.username}
              onChange={handleChange}
              errors={errors?.username}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.institution')}</span>
            <span>{institution.name}</span>
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('data.career')}</span>
            <InstitutionDataInputs
              initialInstitution={institution}
              initialCareer={career}
              onChange={handleChange}
              errors={errors}
              noInstitution
              noSubject
              skipSubjects
            />
          </div>
        </div>
        <Button type="submit" className="profile-footer">
          {isLoadingUpdate || isLoadingPicture ? t('actions.loading') : t('actions.save')}
        </Button>
      </form>
    </div>
  );
};

export default ProfileForm;
