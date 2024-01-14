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
    pattern: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,}$', // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and a special character '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$'
    password: true,

  },
]

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
    pattern: '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9@$!%*?&]{8,}$', // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and a special character '^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$'
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
]

export { loginInputs, registerInputs }