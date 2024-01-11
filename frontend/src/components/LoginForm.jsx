import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { useTranslation } from 'react-i18next';

import { setCredentials } from '../store/slices/authSlice';
import { useLoginMutation } from '../store/slices/authApiSlice';
import { Input, InputPassword, Button } from '.';

const LoginForm = () => {
  const [user, setUser] = useState('');
  const [pwd, setPwd] = useState('');
  const [errMsg, setErrMsg] = useState('');
  const navigate = useNavigate();

  const [login, { isLoading }] = useLoginMutation();
  const dispatch = useDispatch();
  const { t } = useTranslation();

  useEffect(() => {
    setErrMsg('');
  }, [user, pwd]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const userData = await login({ user, pwd }).unwrap();
      console.log(userData);
      dispatch(setCredentials({ ...userData, user }));
      setUser('');
      setPwd('');
      navigate('/');
    } catch (err) {
      if (!err?.originalStatus) {
        // isLoading: true until timeout occurs
        setErrMsg('No Server Response');
      } else if (err.originalStatus === 400) {
        setErrMsg('Missing Username or Password');
      } else if (err.originalStatus === 401) {
        setErrMsg('Unauthorized');
      } else {
        setErrMsg('Login Failed');
      }
    }
  };

  return isLoading ? (
    <h1>Loading...</h1>
  ) : (
    <div className="flex flex-col w-full gap-5">
      <h1 className="text-3xl">Iniciar Sesión</h1>
      <form onSubmit={handleSubmit} className="flex flex-col gap-5 items-center w-full">
        <p className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">
          {errMsg}
        </p>
        <Input
          id="email"
          name="email"
          type="text"
          value={user}
          placeholder="apuntea@apuntea.com"
          required
          autoFocus
          onChange={(e) => setUser(e.target.value)}
        />
        <InputPassword
          id="password"
          name="password"
          placeholder="Apuntea1234@"
          required
          onChange={(e) => setPwd(e.target.value)}
        />
        <Button type="submit">Log In</Button>
      </form>
    </div>
  );
};

export default LoginForm;
