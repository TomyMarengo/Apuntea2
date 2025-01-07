// src/hooks/useRegister.ts

import { useTranslation } from 'react-i18next';
import { useDispatch } from 'react-redux';

import { useLazyRegisterQuery } from '../store/slices/authApiSlice';
import { setCredentials } from '../store/slices/authSlice';
import {
  useCreateUserMutation,
  useLazyGetLoggedUserQuery,
} from '../store/slices/usersApiSlice';
import { decode } from '../utils/helpers';

interface RegisterRequest {
  email: string;
  password: string;
  institutionId: string;
  careerId: string;
}

interface RegisterResponse {
  userUrl?: string;
}

interface RegisterSuccess {
  user?: any;
  token?: any;
  refreshToken?: any;
}

/**
 * Custom hook for registering a new user:
 * 1) createUser => gets a Location
 * 2) register => gets the user and tokens from that location
 * 3) gets the complete user data
 * 4) updates the authentication state
 */
export default function useRegister() {
  const [createUser] = useCreateUserMutation();
  const [register] = useLazyRegisterQuery();
  const [getLoggedUser] = useLazyGetLoggedUserQuery();
  const dispatch = useDispatch();
  const { t } = useTranslation('registerPage');

  async function registerUser(userInfo: RegisterRequest) {
    try {
      // Create the user
      const { userUrl } = (await createUser(
        userInfo,
      ).unwrap()) as RegisterResponse;

      // Perform the registration
      let { user, token, refreshToken } = (await register({
        credentials: userInfo,
        url: userUrl,
      }).unwrap()) as RegisterSuccess;

      // Verify that the user was obtained
      if (!user || !user.id) {
        throw new Error('User not found in the registration response');
      }

      // Get the complete user data
      await getLoggedUser({ userData: user }).unwrap();

      // Decode the token
      token = decode(token);

      // Update the state with the complete user data
      dispatch(setCredentials({ token, refreshToken }));
    } catch (error: any) {
      console.error('Error during registration:', error);
      throw new Error(error?.data?.[0]?.message || t('registerError'));
    }
  }

  return { registerUser };
}
