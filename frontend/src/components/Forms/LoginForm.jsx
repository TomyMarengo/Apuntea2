import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../../store/slices/authSlice';
import { useForm, useLogin } from '../../hooks/index';
import { loginInputs } from '../../constants/forms';
import { Input, Button } from '../index';

const LoginForm = () => {
  const { getSession } = useLogin();
  const { t } = useTranslation();

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      email: '',
      password: '',
    },
    submitCallback: getSession,
    dispatchCallback: setCredentials,
    redirectUrl: '/',
  });

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('pages.login.title')}</h1>
      <div className="flex flex-col gap-5 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          {loginInputs.map((input) => (
            <Input key={input.name} {...input} value={form[input.name]} onChange={handleChange} />
          ))}
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
