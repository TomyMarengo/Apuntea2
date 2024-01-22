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
        const token = meta.response.headers.get('Access-Token')
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
        const data = await meta.response.json()
        const token = meta.response.headers.get('Access-Token')
        return { ...data, token }
      }
    })
  })
})

export const {
  useLazyLoginQuery,
  useRegisterMutation
} = authApiSlice