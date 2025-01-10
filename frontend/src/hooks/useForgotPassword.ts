import { useTranslation } from 'react-i18next';

import {
  useRequestPasswordChangeMutation,
  useUpdateUserPasswordMutation,
  useLazyGetUserByEmailQuery,
} from '../store/slices/usersApiSlice';

interface ForgotPasswordRequest {
  email: string;
  code: string;
  newPassword: string;
}

/**
 * Custom hook for handling the forgot password flow:
 * 1) requestPasswordChange => sends a password change request to the server
 * 2) updateUserPassword => updates the user password after verifying the code
 */
export default function useForgotPassword() {
  const [getUserByEmail] = useLazyGetUserByEmailQuery();
  const [requestPasswordChange] = useRequestPasswordChangeMutation();
  const [updateUserPassword] = useUpdateUserPasswordMutation();
  const { t } = useTranslation('forgotPasswordPage');

  async function handleEmailSubmit(email: string): Promise<boolean> {
    try {
      return await requestPasswordChange({
        email,
      }).unwrap();
    } catch (error: any) {
      throw new Error(
        error?.data?.[0]?.message || t('failedToInitiatePassword'),
      );
    }
  }
  async function handleCodeSubmit({
    email,
    code,
    newPassword,
  }: ForgotPasswordRequest): Promise<void> {
    try {
      const result = await getUserByEmail(email).unwrap();
      const user = result?.[0];

      if (!user || !user.id) {
        throw new Error(t('userNotFound'));
      }

      const resultUpdate = await updateUserPassword({
        userId: user.id,
        email,
        code,
        password: newPassword,
      }).unwrap();

      if (!resultUpdate.success) {
        console.log(resultUpdate);
        console.log(resultUpdate.messages);
        throw new Error(resultUpdate.messages[0]);
      }
    } catch (error: any) {
      throw new Error(error?.message || t('failedToResetPassword'));
    }
  }

  return { handleEmailSubmit, handleCodeSubmit };
}
