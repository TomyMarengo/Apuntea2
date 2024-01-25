import { useLazyLoginQuery } from '../store/slices/authApiSlice';
import { useLazyGetUserQuery } from '../store/slices/usersApiSlice';
import { decode } from '../functions/utils';
import { setCredentials } from '../store/slices/authSlice';
import { useDispatch } from 'react-redux';

const useLogin = () => {
  const [login] = useLazyLoginQuery();
  const [getUser] = useLazyGetUserQuery();
  const dispatch = useDispatch();

  const getSession = async (credentials) => {
    try {
      let { token, refreshToken } = await login(credentials).unwrap();
      const rawToken = token.replace(/^Bearer\s+/i, '');
      token = decode(rawToken);
      const {
        payload: { userId },
      } = token;

      dispatch(setCredentials({ token, refreshToken }));

      const user = await getUser({ userId }).unwrap();

      return { token, refreshToken, user };
    } catch (error) {
      console.error('Error during login:', error);
      throw new Error('Failed to login');
    }
  };

  return { getSession };
};

export default useLogin;
