import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../store/slices/authSlice';
import { Input, Button } from './index';
import { registerInputs } from '../constants/forms';
import { useForm, useAuth, useInstitutionData } from '../hooks/index';
import { useRegisterMutation } from '../store/slices/authApiSlice';

const RegisterForm = () => {
  const [register, { isLoading }] = useRegisterMutation();
  const { t } = useTranslation();

  const { form, error, handleChange, handleSubmit } = useForm(
    {
      email: '',
      password: '',
      institutionId: '',
      careerId: '',
    },
    register,
    setCredentials,
    '/'
  );
  const { institutions, setInstitutions } = useInstitutionData();

  return (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">{t('register.title')}</h1>
      <div className="flex flex-col gap-5 items-center w-full">
        <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
          <p className={error ? 'errmsg' : 'offscreen'} aria-live="assertive">
            {error}
          </p>
          <Input
            {...registerInputs.find((input) => input.name === 'email')}
            value={form.email}
            onChange={handleChange}
          />
          <Input
            {...registerInputs.find((input) => input.name === 'password')}
            value={form.password}
            onChange={handleChange}
          />
          <Input
            {...registerInputs.find((input) => input.name === 'institutionId')}
            value={form.institutionId}
            onChange={handleChange}
            list={institutions}
          />
          <Input
            {...registerInputs.find((input) => input.name === 'careerId')}
            value={form.careerId}
            onChange={handleChange}
          />
          <Button type="submit">Log In</Button>
        </form>
        <span>
          {t('register.haveAccount')}
          <NavLink to="/register" className="link">
            {t('register.login')}
          </NavLink>
        </span>
      </div>
    </div>
  );
};

export default RegisterForm;
