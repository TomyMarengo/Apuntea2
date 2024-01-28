import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { useForm, useRegister } from '../../hooks/index';
import { setCredentials } from '../../store/slices/authSlice';
import { registerInputs } from '../../constants/forms';
import { Input, Button, InstitutionDataInputs } from '../index';
import { RegisterSchema } from '../../constants/schemas';

const RegisterForm = () => {
  const { registerUser } = useRegister();
  const { t } = useTranslation();

  const { handleChange, handleSubmit, errors } = useForm({
    submitCallback: registerUser,
    dispatchCallback: setCredentials,
    redirectUrl: '/',
    schema: RegisterSchema,
  });

  return (
    <div className="flex flex-col gap-10">
      <h1 className="text-3xl">{t('pages.register.title')}</h1>
      <div className="flex flex-col gap-7 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          <Input
            {...registerInputs.find((input) => input.name === 'email')}
            onChange={handleChange}
            className="w-full"
            errors={errors?.email}
          />
          <Input
            {...registerInputs.find((input) => input.name === 'password')}
            onChange={handleChange}
            className="w-full"
            errors={errors?.password}
          />
          <InstitutionDataInputs onChange={handleChange} noSubject skipSubjects className="w-full" errors={errors} />
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
