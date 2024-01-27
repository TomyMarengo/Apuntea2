import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { useForm, useRegister } from '../../hooks/index';
import { setCredentials } from '../../store/slices/authSlice';
import { registerInputs } from '../../constants/forms';
import { isUuid } from '../../functions/utils';
import { Input, Button, InstitutionDataInputs } from '../index';

const RegisterForm = () => {
  const { registerUser } = useRegister();
  const { t } = useTranslation();

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      email: '',
      password: '',
      institutionId: '',
      careerId: '',
    },
    submitCallback: registerUser,
    dispatchCallback: setCredentials,
    redirectUrl: '/',
  });

  return (
    <div className="flex flex-col gap-10">
      <h1 className="text-3xl">{t('pages.register.title')}</h1>
      <div className="flex flex-col gap-7 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          <Input
            {...registerInputs.find((input) => input.name === 'email')}
            value={form.email}
            onChange={handleChange}
            className="w-full"
          />
          <Input
            {...registerInputs.find((input) => input.name === 'password')}
            value={form.password}
            onChange={handleChange}
            className="w-full"
          />
          <InstitutionDataInputs onChange={handleChange} noSubject skipSubjects className="w-full" />
          <Button type="submit">Register</Button>
        </form>
        <span>
          {t('pages.register.haveAccount')}
          <NavLink to="/login" className="link">
            {t('pages.register.login')}
          </NavLink>
        </span>
      </div>
    </div>
  );
};

export default RegisterForm;
