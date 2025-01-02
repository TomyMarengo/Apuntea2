// src/pages/Search/searchSchema.ts

import { z } from 'zod';

export const searchSchema = z.object({
  institutionId: z.string().optional(),
  careerId: z.string().optional(),
  subjectId: z.string().optional(),
  word: z.string().optional(),
  category: z.string(),
  sortBy: z.string(),
  asc: z.string(),
  page: z.string(),
  pageSize: z.string(),
});

export type SearchFormValues = z.infer<typeof searchSchema>;
