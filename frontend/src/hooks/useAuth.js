import { useLoginMutation } from '../store/slices/authApiSlice';
import { useGetUserMutation } from '../store/slices/usersApiSlice';
import { decode } from '../functions/jwt';
import { setCredentials } from '../store/slices/authSlice';
import { useDispatch } from 'react-redux';

const useAuth = () => {
  const [login] = useLoginMutation();
  const [getUser] = useGetUserMutation();
  const dispatch = useDispatch();

  const getSession = async (credentials) => {
    try {
      const { token } = await login(credentials).unwrap();
      dispatch(setCredentials({ token }));
      const rawToken = token.replace(/^Bearer\s+/i, '');
      const tokenDecoded = decode(rawToken);
      const { payload: { userId } } = tokenDecoded;
      const user = await getUser(userId).unwrap();

      return { token, user }
    } catch (error) {
      console.error('Error during login:', error);
      throw new Error('Failed to login');
    }
  }

  return { getSession };
}

export default useAuth;