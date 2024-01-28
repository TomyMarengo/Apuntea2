import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../../store/slices/authSlice';
import { useForm, useLogin } from '../../hooks/index';
import { loginInputs } from '../../constants/forms';
import { Input, Button } from '../index';
import { LoginSchema } from '../../constants/schemas';

const LoginForm = () => {
  const { getSession } = useLogin();
  const { t } = useTranslation();

  const { handleChange, handleSubmit, errors } = useForm({
    submitCallback: getSession,
    dispatchCallback: setCredentials,
    redirectUrl: '/',
    schema: LoginSchema,
  });

  return (
    <div className="flex flex-col gap-10">
      <h1 className="text-3xl">{t('pages.login.title')}</h1>
      <div className="flex flex-col gap-7 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          <Input
            {...loginInputs.find((input) => input.name === 'email')}
            onChange={handleChange}
            errors={errors?.email}
            className="w-full"
          />
          <Input
            {...loginInputs.find((input) => input.name === 'password')}
            onChange={handleChange}
            errors={errors?.password}
            className="w-full"
          />
          <Button type="submit">Log In</Button>
        </form>
        <span>
          {t('pages.login.dontHaveAccount')}
          <NavLink to="/register" className="link">
            {t('pages.login.register')}
          </NavLink>
        </span>
      </div>
    </div>
  );
};

export default LoginForm;
