import { z } from 'zod';

export const LoginSchema = z.object({
  email: z.string().email({ message: 'errors.email' }),
  password: z.string().regex(new RegExp('^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}$'), 'errors.password.regex').max(50, { message: 'errors.password.length' }),
});

export const RegisterSchema = LoginSchema.extend({
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),
});

export const UserSchema = z.object({
  firstName: z.string().regex(new RegExp('^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]{4,20}$'), 'errors.firstName'),
  lastName: z.string().regex(new RegExp('^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]{4,20}$'), 'errors.firstName'),
  username: z.string().regex(new RegExp('^(?![\\d-_.]{4,30}$)[a-zA-Z\\d-_.]{4,30}$'), 'errors.username'),
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),

});

export const PatchUserSchema = UserSchema.partial();

const MIN_PAGE_SIZE = 4;
const MAX_PAGE_SIZE = 36;

export const SearchSchema = z.object({
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),
  subjectId: z.string().uuid({ message: 'errors.subjectId' }),
  word: z.string().min(2).max(50).regex(new RegExp('/^(?!([ ,\\-_.]{2,50})$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]{2,50}$'), 'errors.word'),
  asc: z.boolean(),
  sortBy: z.enum(['modified', 'name', 'date'], { message: 'errors.sortBy' }),
  page: z.number().int().positive().refine((val) => val >= 1, { message: 'errors.page' }),
  pageSize: z.number().int().positive().refine((val) => val >= MIN_PAGE_SIZE && val <= MAX_PAGE_SIZE, { message: 'errors.pageSize' }),
});