import { apiSlice } from "./apiSlice";

export const institutionsApiSlice = apiSlice.injectEndpoints({
  endpoints: builder => ({
    getInstitutions: builder.query({
      query: () => '/institutions',
      keepUnusedDataFor: 5 * 60 * 60 * 1000 // 5 hours
    }),
    getInstitution: builder.query({
      query: id => `/institutions/${id}`
    }),
  })
})

export const {
  useGetInstitutionsQuery,
  useGetInstitutionQuery
} = institutionsApiSlice