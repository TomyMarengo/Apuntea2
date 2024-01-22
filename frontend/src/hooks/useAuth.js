import { useLoginMutation } from '../store/slices/authApiSlice';
import { useLazyGetUserQuery } from '../store/slices/usersApiSlice';
import { decode } from '../functions/jwt';
import { useLazyGetInstitutionQuery, useLazyGetCareerQuery } from '../store/slices/institutionsApiSlice';

const useAuth = () => {
  const [login] = useLoginMutation();
  const [getUser] = useLazyGetUserQuery();
  const [getInstitution] = useLazyGetInstitutionQuery();
  const [getCareer] = useLazyGetCareerQuery();

  const getSession = async (credentials) => {
    try {
      let { token } = await login(credentials).unwrap();
      const rawToken = token.replace(/^Bearer\s+/i, '');
      token = decode(rawToken);
      const {
        payload: { userId },
      } = token;

      const user = await getUser(userId).unwrap();
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
