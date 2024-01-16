import { useLoginMutation } from '../store/slices/authApiSlice';
import { useGetUserMutation } from '../store/slices/usersApiSlice';
import { decode } from '../functions/jwt';
import { useGetInstitutionMutation, useGetCareerMutation } from '../store/slices/institutionsApiSlice';

const useAuth = () => {
  const [login] = useLoginMutation();
  const [getUser] = useGetUserMutation();
  const [getInstitution] = useGetInstitutionMutation();
  const [getCareer] = useGetCareerMutation();

  const getSession = async (credentials) => {
    try {
      const { token } = await login(credentials).unwrap();
      const rawToken = token.replace(/^Bearer\s+/i, '');
      const tokenDecoded = decode(rawToken);
      const {
        payload: { userId },
      } = tokenDecoded;
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
