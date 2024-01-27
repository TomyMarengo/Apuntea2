const loginInputs = [
  {
    id: 'email',
    name: 'email',
    type: 'email',
    placeholder: 'placeholders.email',
    required: true,
    autoFocus: true,
  },
  {
    id: 'password',
    name: 'password',
    placeholder: 'placeholders.password',
    password: true,
  },
];

const registerInputs = [
  {
    id: 'email',
    name: 'email',
    type: 'email',
    placeholder: 'placeholders.email',
    required: true,
    autoFocus: true,
  },
  {
    id: 'password',
    name: 'password',
    placeholder: 'placeholders.password',
    password: true,
  },
  {
    id: 'institutionId',
    name: 'institutionId',
    placeholder: 'placeholders.institutionId',
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
  },
];

const profileInputs = [
  {
    id: 'firstName',
    name: 'firstName',
    placeholder: 'placeholders.firstName',
    autoFocus: true,
  },
  {
    id: 'lastName',
    name: 'lastName',
    placeholder: 'placeholders.lastName',
  },
  {
    id: 'username',
    name: 'username',
    placeholder: 'placeholders.username',
    required: true,
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
  },
  {
    id: 'profilePicture',
    name: 'profilePicture',
  },
];

const institutionInputs = [
  {
    id: 'institutionId',
    name: 'institutionId',
    placeholder: 'placeholders.institutionId',
  },
  {
    id: 'careerId',
    name: 'careerId',
    placeholder: 'placeholders.careerId',
  },
  {
    id: 'subjectId',
    name: 'subjectId',
    placeholder: 'placeholders.subjectId',
  },
]

const searchInputs = [
  {
    id: 'word',
    name: 'word',
    placeholder: 'placeholders.word',
  }
];

const reviewInputs = [
  {
    id: 'content',
    name: 'content',
    placeholder: 'placeholders.content',
  },
  {
    id: 'score',
    name: 'score',
  },
];
export { loginInputs, registerInputs, profileInputs, institutionInputs, searchInputs, reviewInputs };

