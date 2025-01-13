import { setupServer } from 'msw/node';
import { beforeAll, beforeEach, afterEach, afterAll } from 'vitest';

import { directoriesHandlers } from '../mocks/directoriesApiMocks.js';
import { institutionsHandlers } from '../mocks/institutionsApiMocks.js';
import { notesHandlers } from '../mocks/notesApiMocks.js';
import { reviewsHandles } from '../mocks/reviewsApiMocks.js';
import { usersHandlers } from '../mocks/usersApiMocks.js';

const server = setupServer(
  ...[
    ...notesHandlers,
    ...institutionsHandlers,
    ...usersHandlers,
    ...directoriesHandlers,
    ...reviewsHandles,
  ],
);

beforeAll(() => server.listen());

beforeEach((context) => (context.server = server));

afterEach(() => server.resetHandlers());

afterAll(() => server.close());

export { server };
