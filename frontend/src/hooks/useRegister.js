import { decode } from "../functions/utils";
import { useLazyRegisterQuery } from "../store/slices/authApiSlice";
import { useCreateUserMutation } from "../store/slices/usersApiSlice";

const useRegister = () => {
  const [createUser] = useCreateUserMutation();
  const [register] = useLazyRegisterQuery();

  const registerUser = async (userInfo) => {
    try {
      const { location } = await createUser(userInfo).unwrap();
      let { user, token, refreshToken } = await register({ credentials: userInfo, url: location }).unwrap();
      token = decode(token);

      return { user, token, refreshToken };
    } catch (error) {
      console.error('Error during register:', error);
      throw new Error('Failed to register');
    }
  };

  return { registerUser };
}

export default useRegister;