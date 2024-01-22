import { useLazyLoginQuery } from '../store/slices/authApiSlice';
import { useLazyGetUserQuery } from '../store/slices/usersApiSlice';
import { decode } from '../functions/jwt';
import { useLazyGetInstitutionQuery, useLazyGetCareerQuery } from '../store/slices/institutionsApiSlice';
import { setCredentials } from '../store/slices/authSlice';
import { useDispatch } from 'react-redux';

const useAuth = () => {
  const [login] = useLazyLoginQuery();
  const [getUser] = useLazyGetUserQuery();
  const [getInstitution] = useLazyGetInstitutionQuery();
  const [getCareer] = useLazyGetCareerQuery();
  const dispatch = useDispatch();

  const getSession = async (credentials) => {
    try {
      let { token } = await login(credentials).unwrap();
      const rawToken = token.replace(/^Bearer\s+/i, '');
      token = decode(rawToken);
      const {
        payload: { userId },
      } = token;

      dispatch(setCredentials({ token }));

      const user = await getUser({ userId }).unwrap();
      const institution = await getInstitution({ url: user.institution }).unwrap();
      const career = await getCareer({ url: user.career }).unwrap();

      return { token, user, institution, career };
    } catch (error) {
      console.error('Error during login:', error);
      throw new Error('Failed to login');
    }
  };

  return { getSession };
};

export default useAuth;
