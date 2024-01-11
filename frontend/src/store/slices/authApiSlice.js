import { apiSlice } from "./apiSlice";

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    login: builder.mutation({
      query: credentials => ({
        url: '/tokens',
        method: 'POST',
        headers: {
          Authorization: `Basic ${btoa(`${credentials.user}:${credentials.pwd}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const accessToken = meta.response.headers.get('access-token')
        return { accessToken }
      }
    }),
    register: builder.mutation({
      query: credentials => ({
        url: '/users',
        method: 'POST',
        headers: {
          Authorization: `Basic ${btoa(`${credentials.user}:${credentials.pwd}`)}`
        }
      }),
      transformResponse: async (response, meta) => {
        const accessToken = meta.response.headers.get('access-token')
        return { accessToken }
      }
    })
  })
})

export const {
  useLoginMutation,
  useRegisterMutation
} = authApiSlice