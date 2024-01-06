export type User = {
  id: string;
  name: string;
  email: string;
  password: string;
};

export interface Option {
  readonly label: string;
  readonly value: string;
}
