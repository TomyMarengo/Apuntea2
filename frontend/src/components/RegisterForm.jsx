import { NavLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../store/slices/authSlice';
import { Input, Button } from './index';
import { registerInputs } from '../constants/forms';
import { useForm, useInstitutionData } from '../hooks/index';
import { useRegisterMutation } from '../store/slices/authApiSlice';
import { isUuid } from '../functions/validation';

const RegisterForm = () => {
  const [register, { isLoading }] = useRegisterMutation();
  const { t } = useTranslation();

  const { institutions, setInstitutionId, careers, setCareerId } = useInstitutionData({ skipSubjects: true });

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
            onChange={(e) => {
              handleChange(e);
              if (isUuid(e.target.value) || e.target.value === '') setInstitutionId(e.target.value);
            }}
            list={institutions}
            autoComplete="off"
          />
          <Input
            {...registerInputs.find((input) => input.name === 'careerId')}
            value={form.careerId}
            onChange={(e) => {
              handleChange(e);
              if (isUuid(e.target.value) || e.target.value === '') setCareerId(e.target.value);
            }}
            list={careers}
            autoComplete="off"
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
