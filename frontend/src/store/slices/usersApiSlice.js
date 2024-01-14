import { apiSlice } from "./apiSlice";

export const usersApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getUsers: builder.query({
      query: () => '/users',
      keepUnusedDataFor: 5 * 60 * 60 * 1000 // 5 hours
    }),
    getUser: builder.mutation({
      query: userId => ({
        url: `/users/${userId}`,
        method: 'GET',
      }),
    }),
  })
})

export const {
  useGetUsersQuery,
  useGetUserMutation
} = usersApiSlice