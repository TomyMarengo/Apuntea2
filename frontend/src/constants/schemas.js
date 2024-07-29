import { z } from 'zod';

export const LoginSchema = z.object({
  email: z.string().email({ message: 'errors.email' }),
  password: z
    .string()
    // .regex(new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$'), 'errors.password.regex')
    .max(50, { message: 'errors.password.length' }),
});

export const RegisterSchema = LoginSchema.extend({
  password: z
    .string()
    .regex(new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$'), 'errors.password.regex')
    .max(50, { message: 'errors.password.length' }),
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),
});

export const UserSchema = z.object({
  firstName: z.string().regex(new RegExp('^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]{2,20}$'), 'errors.firstName'),
  lastName: z.string().regex(new RegExp('^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]{2,20}$'), 'errors.lastName'),
  username: z.string().regex(new RegExp('^(?![\\d-_.]{4,30}$)[a-zA-Z\\d-_.]{4,30}$'), 'errors.username'),
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),
});

export const PatchUserSchema = UserSchema.partial();

export const ChangePasswordSchema = z.object({
  oldPassword: z.string().max(50, { message: 'errors.password.length' }),
  password: z
    .string()
    .regex(new RegExp('^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$'), 'errors.password.regex')
    .max(50, { message: 'errors.password.length' }),
});

const MIN_PAGE_SIZE = 4;
const MAX_PAGE_SIZE = 36;

export const SearchSchema = z.object({
  institutionId: z.string().uuid({ message: 'errors.institutionId' }),
  careerId: z.string().uuid({ message: 'errors.careerId' }),
  subjectId: z.string().uuid({ message: 'errors.subjectId' }),
  word: z
    .string()
    .min(2)
    .max(50)
    .regex(new RegExp('/^(?!([ ,\\-_.]{2,50})$)[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ .,\\-_]{2,50}$'), 'errors.word'),
  asc: z.enum(['true', 'false'], { message: 'errors.asc' }),
  sortBy: z.enum(['modified', 'name', 'date', 'score'], { message: 'errors.sortBy' }),
  page: z
    .number()
    .int()
    .positive()
    .refine((val) => val >= 1, { message: 'errors.page' }),
  pageSize: z
    .number()
    .int()
    .positive()
    .refine((val) => val >= MIN_PAGE_SIZE && val <= MAX_PAGE_SIZE, { message: 'errors.pageSize' }),
});
// Asc: boolean
export const SearchSchemaPartial = SearchSchema.partial();
export const PostReviewSchema = z.object({
  noteId: z.string().uuid({ message: 'errors.noteId' }),
  score: z.number().int().min(1).max(5, { message: 'errors.score' }),
  content: z.string().min(0).max(255, { message: 'errors.content' }),
});

export const PatchReviewSchema = PostReviewSchema.omit({ noteId: true }).partial();

export const PostNoteSchema = z.object({
  name: z.string().min(4).max(50, { message: 'errors.name' }),
  file: z.string().min(4).max(50, { message: 'errors.file' }),
  category: z.string().regex(new RegExp('^(THEORY|PRACTICE|EXAM|OTHER)$'), 'errors.category'),
  visible: z.string().regex(new RegExp('^(true|false)$'), 'errors.visible'),
});

export const PatchNoteSchema = PostNoteSchema.omit({ file: true }).partial();
