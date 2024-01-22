const loginInputs = [
  {
    id: 'email',
    name: 'email',
    type: 'email',
    placeholder: 'placeholders.email',
    errorMessage: 'errors.email',
    required: true,
    autoFocus: true,
  },
  {
    id: 'password',
    name: 'password',
    placeholder: 'placeholders.password',
    errorMessage: 'errors.password',
    pattern: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{4,}$',
    password: true,
  },
];

const registerInputs = [
  {
    id: 'email',
    name: 'email',
    type: 'email',
    placeholder: 'placeholders.email',
    errorMessage: 'errors.email',
    required: true,
    autoFocus: true,
  },
  {
    id: 'password',
    name: 'password',
    placeholder: 'placeholders.password',
    errorMessage: 'errors.password',
    pattern: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{4,}$', // Minimum four characters, at least one uppercase letter, one lowercase letter, one number and a special character '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$'
    password: true,
  },
  {
    id: 'institutionId',
    name: 'institutionId',
    placeholder: 'placeholders.institutionId',
    errorMessage: 'errors.institutionId',
    pattern: '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$',
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
    errorMessage: 'errors.careerId',
    pattern: '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$',
  },
];

const profileInputs = [
  {
    id: 'firstName',
    name: 'firstName',
    placeholder: 'placeholders.firstName',
    errorMessage: 'errors.firstName',
    pattern: '^(?![0-9\\s]+$)[\\p{L}\\s]{1,25}$',
    autoFocus: true,
  },
  {
    id: 'lastName',
    name: 'lastName',
    placeholder: 'placeholders.lastName',
    errorMessage: 'errors.lastName',
    pattern: '^(?![0-9\\s]+$)[\\p{L}\\s]{1,25}$',
  },
  {
    id: 'username',
    name: 'username',
    placeholder: 'placeholders.username',
    errorMessage: 'errors.username',
    pattern: '^(?![0-9._-]+$)[a-zA-Z0-9._-]{1,25}$',
    required: true,
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
    errorMessage: 'errors.careerId',
    pattern: '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}$',
  },
  {
    id: 'profilePicture',
    name: 'profilePicture',
    errorMessage: 'errors.profilePicture',
  },
];

export { loginInputs, registerInputs, profileInputs };
