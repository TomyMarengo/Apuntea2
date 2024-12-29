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

  async function handleEmailSubmit(email: string) {
    try {
      const result = await getUserByEmail(email).unwrap();
      const user = result?.[0]; // Since getUserByEmail returns an array of users

      if (!user || !user.id) {
        throw new Error('User not found for the provided email');
      }

      await requestPasswordChange({ email }).unwrap();
    } catch (error: any) {
      console.error('Error during email submission:', error);
      throw new Error(
        error?.data?.[0]?.message || 'Failed to initiate password change',
      );
    }
  }

  async function handleCodeSubmit({
    email,
    code,
    newPassword,
  }: ForgotPasswordRequest) {
    try {
      const result = await getUserByEmail(email).unwrap();
      const user = result?.[0];

      if (!user || !user.id) {
        throw new Error('User not found after password reset');
      }

      const success = await updateUserPassword({
        userId: user.id,
        email,
        code,
        password: newPassword,
      }).unwrap();

      if (!success) {
        throw new Error('Password update failed');
      }
    } catch (error: any) {
      console.error('Error during code submission:', error);
      throw new Error(error?.message || 'Failed to reset password');
    }
  }

  return { handleEmailSubmit, handleCodeSubmit };
}
