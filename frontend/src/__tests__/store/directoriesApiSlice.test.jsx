import { expectToBePagedContent, setupApiStore } from '../setup/utils';
import {
  directoriesApiSlice,
  useCreateDirectoryMutation,
  useGetIsFavoriteDirectoryQuery,
  useGetDirectoryQuery,
  useGetUserDirectoriesFavoritesQuery,
  useUpdateDirectoryMutation,
} from '../../store/slices/directoriesApiSlice';
import { describe, expect, it } from 'vitest';
import {
  existingDirectoryId,
  existingDirectoryName,
  favUserId,
  fileErrorMsg,
  nonExistingDirectoryName,
  nonFavDirectoryId,
  otherExistingDirectoryName,
} from '../mocks/directoriesApiMocks';
import { useWithWrapper } from '../setup/wrapper.jsx';
import { waitFor } from '@testing-library/react';

function setupDirectoriesApiStore() {
  return setupApiStore(directoriesApiSlice);
}
const store = setupDirectoriesApiStore();
describe('directoriesApiSlice', () => {
  it('should fetch a directory successfully', async () => {
    let directoryId = existingDirectoryId;
    const { result } = useWithWrapper(
      () => useGetDirectoryQuery({ directoryId }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const directory = result.current.data;
    expect(directory).toBeTruthy();
    expect(directory.id).toBe(directoryId);
  });

  it('should create a directory successfully', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateDirectoryMutation(),
      store,
    );
    const [createDirectory, createDirectoryResult] =
      await wrapperResult.current;
    const result = await createDirectory({
      name: nonExistingDirectoryName,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to create a directory and relay error messages', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useCreateDirectoryMutation(),
      store,
    );
    const [createDirectory, createDirectoryResult] =
      await wrapperResult.current;
    const result = await createDirectory({
      name: existingDirectoryName,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(fileErrorMsg);
  });

  it('should modify directory successfully with nonExistingDirectoryName', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateDirectoryMutation(),
      store,
    );
    const [updateDirectory] = await wrapperResult.current;
    const result = await updateDirectory({
      directoryId: existingDirectoryId,
      name: nonExistingDirectoryName,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should modify directory successfully with no name change', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateDirectoryMutation(),
      store,
    );
    const [updateDirectory] = await wrapperResult.current;
    const result = await updateDirectory({
      directoryId: existingDirectoryId,
      visible: false,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should modify directory successfully with the same old name', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateDirectoryMutation(),
      store,
    );
    const [updateDirectory] = await wrapperResult.current;
    const result = await updateDirectory({
      directoryId: existingDirectoryId,
      name: existingDirectoryName,
    }).unwrap();
    expect(result.success).toBe(true);
    expect(result.messages).empty;
  });

  it('should fail to modify name and relay error messages', async () => {
    let { result: wrapperResult } = useWithWrapper(
      async () => useUpdateDirectoryMutation(),
      store,
    );
    const [updateDirectory] = await wrapperResult.current;
    const result = await updateDirectory({
      directoryId: existingDirectoryId,
      name: otherExistingDirectoryName,
    }).unwrap();
    expect(result.success).toBe(false);
    expect(result.messages).contains(fileErrorMsg);
  });

  it('should get the directory as favorite', async () => {
    const { result } = useWithWrapper(
      () =>
        useGetIsFavoriteDirectoryQuery({
          directoryId: existingDirectoryId,
          userId: favUserId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const isFavorite = result.current.data.success;
    expect(isFavorite).toBe(true);
  });

  it('should get the directory as not favorite', async () => {
    const { result } = useWithWrapper(
      () =>
        useGetIsFavoriteDirectoryQuery({
          directoryId: nonFavDirectoryId,
          userId: favUserId,
        }),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const isFavorite = result.current.data.success;
    expect(isFavorite).toBe(false);
  });

  it('should fetch favorite directories succesfully', async () => {
    let directoryId = existingDirectoryId;
    const { result } = useWithWrapper(
      () => useGetUserDirectoriesFavoritesQuery({}),
      store,
    );
    await waitFor(() => expect(result.current.isSuccess).toBe(true));
    const { directories, totalCount, totalPages } = result.current.data;
    expectToBePagedContent(directories, totalCount, totalPages);
  });
});