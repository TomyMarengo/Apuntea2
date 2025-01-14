import { apiUrl, expectToBePagedContent, setupApiStore } from '../setup/utils';
import { describe, expect, it } from 'vitest';
import {
  useCreateUserMutation,
  useGetFollowersQuery,
  useGetFollowingsQuery,
  useGetLoggedUserQuery,
  useGetUserQuery,
  useGetUsersQuery,
  useIsFollowingUserQuery,
  useRequestPasswordChangeMutation,
  usersApiSlice,
  useUpdateUserMutation,
  useUpdateUserPasswordMutation,
  useUpdateUserStatusMutation,
} from '../../store/slices/usersApiSlice';
import { useWithWrapper } from '../setup/wrapper.jsx';
import { waitFor } from '@testing-library/react';
import {
  existingEmail,
  existingUsername,
  followerUserId,
  nonExistingEmail,
  nonExistingUsername,
  passwordChangeCode,
  someUserId,
  usedUsernameMsg,
} from '../mocks/usersApiMocks';
import {institutionsApiSlice} from "../../store/slices/institutionsApiSlice";

function setupNotesApiStore() {
  return setupApiStore([usersApiSlice, institutionsApiSlice]);
}
const store = setupNotesApiStore();
describe('usersApiSlice', () => {
  it('should fetch a user with an institution and a career successfully', async () => {
    let userId = someUserId;
    const { result } = useWithWrapper(() => useGetUserQuery({ userId }), store);
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const user = result.current.data;
    expect(user).toBeTruthy();
    expect(user.id).toBe(userId);
  });

  it('should fetch a user successfully', async () => {
    let userId = someUserId;
    const { result } = useWithWrapper(
      () => useGetLoggedUserQuery({ userId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const user = result.current.data;
    expect(user).toBeTruthy();
    expect(user.id).toBe(userId);
    expect(user.career).toBeTruthy();
    expect(user.institution).toBeTruthy();
  });

  it('should create a user successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateUserMutation(),
      store,
    );
    const [createUser] = await wrapperResult.current;
    const result = await createUser({ email: nonExistingEmail }).unwrap();
    expect(result).toBeTruthy();
  });

  it('should fail to create a user', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateUserMutation(),
      store,
    );
    const [createUser] = await wrapperResult.current;

    const result = await createUser({ email: existingEmail });
    expect(result.error).toBeTruthy();
  });

  it('should modify a user successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateUserMutation(),
      store,
    );
    const [updateUser] = await wrapperResult.current;
    const result = await updateUser({
      userId: someUserId,
      username: nonExistingUsername,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to modify a user and relay error messages', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateUserMutation(),
      store,
    );
    const [updateUser] = await wrapperResult.current;
    const result = await updateUser({
      userId: someUserId,
      username: existingUsername,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(usedUsernameMsg);
  });

  it('should update user status successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateUserStatusMutation(),
      store,
    );
    const [updateUserStatus] = await wrapperResult.current;
    const result = await updateUserStatus({
      userId: someUserId,
      status: 'ACTIVE',
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fetch users with mail successfully', async () => {
    const { result } = useWithWrapper(() => useGetUsersQuery({}), store);
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const { users, totalCount, totalPages } = result.current.data;
    expectToBePagedContent(users, totalCount, totalPages);
    for (let user of users) {
      expect(user.email).toBeDefined();
    }
  });

  it('should fetch users without mail successfully', async () => {
    const { result } = useWithWrapper(
      () => useGetFollowersQuery({ url: '/users?followedBy=' }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const { users, totalCount, totalPages } = result.current.data;
    expectToBePagedContent(users, totalCount, totalPages);
    for (let user of users) {
      expect(user.email).toBeUndefined();
    }
  });

  it('should fetch users without mail successfully', async () => {
    const { result } = useWithWrapper(
      () => useGetFollowingsQuery({ url: '/users?following=' }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const { users, totalCount, totalPages } = result.current.data;
    expectToBePagedContent(users, totalCount, totalPages);
    for (let user of users) {
      expect(user.email).toBeUndefined();
    }
  });

  it('should get the user as followed', async () => {
    const { result } = useWithWrapper(
      () =>
        useIsFollowingUserQuery({
          userId: someUserId,
          followerId: followerUserId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const isFavorite = result.current.data.success;
    expect(isFavorite).toBe(true);
  });

  it('should get the user as not followed', async () => {
    const { result } = useWithWrapper(
      () =>
        useIsFollowingUserQuery({
          userId: followerUserId,
          followerId: someUserId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const isFavorite = result.current.data.success;
    expect(isFavorite).toBe(false);
  });

  it('should request password change successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useRequestPasswordChangeMutation(),
      store,
    );
    const [requestPasswordChange] = await wrapperResult.current;
    const result = await requestPasswordChange({
      email: existingEmail,
    }).unwrap();
    expect(result).toBe(true);
  });

  it('should fail request password change due to non existing mail', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useRequestPasswordChangeMutation(),
      store,
    );
    const [requestPasswordChange] = await wrapperResult.current;
    const result = await requestPasswordChange({
      email: nonExistingEmail,
    }).unwrap();
    expect(result).toBe(false);
  });

  it('should change the password successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateUserPasswordMutation(),
      store,
    );
    const [requestPasswordChange] = await wrapperResult.current;
    const result = await requestPasswordChange({
      userId: someUserId,
      password: '1234',
      email: existingEmail,
      code: passwordChangeCode,
    }).unwrap();
    expect(result.success).toBe(true);
  });

  it('should change the password successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateUserPasswordMutation(),
      store,
    );
    const [requestPasswordChange] = await wrapperResult.current;
    const result = await requestPasswordChange({
      userId: someUserId,
      password: '1234',
      email: existingEmail,
      code: '',
    }).unwrap();
    expect(result.success).toBe(false);
  });
});