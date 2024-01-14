import { apiSlice } from "./apiSlice";

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    login: builder.mutation({
      query: credentials => ({
        url: '/tokens',
        method: 'POST',
        headers: {
          Authorization: `Basic ${btoa(`${credentials.email}:${credentials.password}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const token = meta.response.headers.get('access-token')
        return { token }
      }
    }),
    register: builder.mutation({
      query: userInfo => ({
        url: '/users',
        method: 'POST',
        body: userInfo,
        headers: {
          Authorization: `Basic ${btoa(`${userInfo.email}:${userInfo.password}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const token = meta.response.headers.get('access-token')
        const data = await meta.response.json()
        return { ...data, token }
      }
    })
  })
})

export const {
  useLoginMutation,
  useRegisterMutation
} = authApiSlice