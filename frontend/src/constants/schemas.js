import { z } from 'zod';

export const UserSchema = z.object({
  firstName: z.string().max(7, { invalid_type_error: 'Max 7 characters' }),
  lastName: z.string(),
  username: z.string(),
  institutionId: z.string().uuid(),
  careerId: z.string().uuid(),
});

export const PatchUserSchema = UserSchema.partial();