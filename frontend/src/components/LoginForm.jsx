import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../store/slices/authSlice';
import { Input, Button } from '.';
import { loginInputs } from '../constants/forms';
import { useForm, useAuth } from '../hooks/index';

const LoginForm = () => {
  const { getSession } = useAuth();
  const { t } = useTranslation();

  const { form, error, handleChange, handleSubmit } = useForm(
    {
      email: '',
      password: '',
    },
    getSession,
    setCredentials,
    '/'
  );

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('loginForm.title')}</h1>
      <div className="flex flex-col gap-5 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          <p className={error ? 'errmsg' : 'offscreen'} aria-live="assertive">
            {error}
          </p>
          <Input {...loginInputs.find((input) => input.name === 'email')} value={form.email} onChange={handleChange} />
          <Input
            password
            {...loginInputs.find((input) => input.name === 'password')}
            value={form.password}
            onChange={handleChange}
          />
          <Button type="submit">Log In</Button>
        </form>
        <span>
          ¿No tienes una cuenta?{' '}
          <NavLink to="/register" className="link">
            Crear una
          </NavLink>
        </span>
      </div>
    </div>
  );
};

export default LoginForm;
