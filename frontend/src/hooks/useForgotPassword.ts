import { useTranslation } from 'react-i18next';

import {
  useRequestPasswordChangeMutation,
  useUpdateUserPasswordMutation,
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

      const resultUpdate = await updateUserPassword({
        email,
        code,
        password: newPassword,
      }).unwrap();

      if (!resultUpdate.success) {
        throw new Error(resultUpdate.messages[0]);
      }
    } catch (error: any) {
      throw new Error(error?.message || t('failedToResetPassword'));
    }
  }

  return { handleEmailSubmit, handleCodeSubmit };
}
