import { apiSlice } from "./apiSlice";

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    login: builder.query({
      query: credentials => ({
        url: '/users?pageSize=4',
        headers: {
          Authorization: `Basic ${btoa(`${credentials.email}:${credentials.password}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const token = meta.response.headers.get('Access-Token').split(' ')[1]
        const refreshToken = meta.response.headers.get('Refresh-Token').split(' ')[1]
        return { token, refreshToken }
      }
    }),
    register: builder.query({
      query: ({ credentials, userId, url }) => ({
        url: url || `/users/${userId}`,
        headers: {
          Authorization: `Basic ${btoa(`${credentials.email}:${credentials.password}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const user = await response;
        const token = meta.response.headers.get('Access-Token').split(' ')[1]
        const refreshToken = meta.response.headers.get('Refresh-Token').split(' ')[1]
        return { user, token, refreshToken }
      }
    })
  })
})

export const {
  useLazyLoginQuery,
  useLazyRegisterQuery,
} = authApiSlice