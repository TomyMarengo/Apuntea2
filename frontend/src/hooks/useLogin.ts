// src/hooks/useLogin.ts

import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';

import { useLazyLoginQuery } from '../store/slices/authApiSlice';
import { setCredentials } from '../store/slices/authSlice';
import { useLazyGetLoggedUserQuery } from '../store/slices/usersApiSlice';
import { decode } from '../utils/helpers';

interface Credentials {
  email: string;
  password: string;
}

/**
 * Represents the response received after a login attempt.
 *
 * @interface LoginResponse
 *
 * @property {any} [token] - The authentication token returned by the server. Consider specifying a more precise type.
 * @property {any} [refreshToken] - The refresh token used to obtain a new authentication token. Consider specifying a more precise type.
 * @property {any} [user] - The user information returned by the server. Consider specifying a more precise type.
 */
interface LoginResponse {
  token?: any; // or a more specific type
  refreshToken?: any; // or a more specific type
  user?: any; // or a more specific type
}

/**
 * Hook that authenticates the user, retrieves additional user data,
 * decodes the token, and dispatches setCredentials to the Redux store.
 */
export default function useLogin() {
  const { t } = useTranslation('loginPage');
  const [login] = useLazyLoginQuery();
  const [getLoggedUser] = useLazyGetLoggedUserQuery();
  const dispatch = useDispatch();

  async function loginUser(credentials: Credentials) {
    try {
      // Perform authentication
      const result = await login(credentials).unwrap();
      let { token, refreshToken } = result as LoginResponse;

      token = decode(token);

      //Verify that the user was obtained
      if (!token || !token.payload || !token.payload.userId) {
        throw new Error('User not found in login response');
      }

      // Retrieve complete user data
      await getLoggedUser({ userId: token?.payload?.userId }).unwrap();

      // Update the state with the complete user data
      dispatch(setCredentials({ token, refreshToken }));
    } catch (error: any) {
      console.error('Error during login:', error);
      throw new Error(error?.data?.[0]?.message || t('loginError'));
    }
  }

  return { loginUser };
}
