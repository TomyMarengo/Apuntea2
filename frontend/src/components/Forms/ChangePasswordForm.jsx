import { useTranslation } from 'react-i18next';

// import { useUpdateUserMutation } from '../../store/slices/usersApiSlice';
import { useForm, useUserData } from '../../hooks/index';
import { Input, Button } from '../index';
import { settingsInputs } from '../../constants/forms';
import { ChangePasswordSchema } from '../../constants/schemas';

const ChangePasswordForm = ({ user }) => {
  // const [updateUser, { isLoading: isLoadingUpdate }] = useUpdateUserMutation();
  const { t } = useTranslation();
  const { updateUser, isLoadingUpdate } = useUserData();

  const { handleChange, handleSubmit, errors } = useForm({
    submitCallback: updateUser,
    args: {
      userId: user.id,
      email: user.email
    },
    schema: ChangePasswordSchema,
  });

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.settings.changePassword.title')}</h1>
      <form onSubmit={handleSubmit} className="gap-5 w-full settings-form">
        <div className="settings-inputs">
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('pages.settings.changePassword.oldPassword')}</span>
            <Input
              {...settingsInputs.find((input) => input.name === 'oldPassword')}
              onChange={handleChange}
              errors={errors?.oldPassword}
            />
          </div>
          <div className="flex flex-col gap-1">
            <span className="font-bold">{t('pages.settings.changePassword.newPassword')}</span>
            <Input
              {...settingsInputs.find((input) => input.name === 'password')}
              onChange={handleChange}
              errors={errors?.password}
            />
          </div>
        </div>
        <Button type="submit" className="settings-footer">
          {isLoadingUpdate ? t('actions.loading') : t('actions.update')}
        </Button>
      </form>
    </div>

  );
};

export default ChangePasswordForm;
