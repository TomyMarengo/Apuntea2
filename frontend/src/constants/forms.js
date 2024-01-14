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
    pattern: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{4,}$', // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and a special character '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$'
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
    pattern: '^[a-zA-ZÀ-ÿ\u00f1\u00d1\u0020]{1,25}(?!s*$)$',
    autoFocus: true,
  },
  {
    id: 'lastName',
    name: 'lastName',
    placeholder: 'placeholders.lastName',
    errorMessage: 'errors.lastName',
    pattern: '^[a-zA-ZÀ-ÿ\u00f1\u00d1\u0020]{1,25}(?!s*$)$',
  },
  {
    id: 'username',
    name: 'username',
    placeholder: 'placeholders.username',
    errorMessage: 'errors.username',
    pattern: '^(?![-_.0-9]*$)[a-zA-Z0-9_.-]{1,25}$',
    required: true,
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
    errorMessage: 'errors.careerId',
    pattern: '^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}$',
  },
];

export { loginInputs, registerInputs, profileInputs };
