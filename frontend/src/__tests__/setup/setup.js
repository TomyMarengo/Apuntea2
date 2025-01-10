import { setupServer } from "msw/node";
import { beforeAll, beforeEach, afterEach, afterAll } from "vitest";
import {notesHandlers} from "../mocks/notesApiMocks.js";
import {institutionsHandlers} from "../mocks/institutionsApiMocks.js";
import {usersHandlers} from "../mocks/usersApiMocks.js";
import {directoriesHandlers} from "../mocks/directoriesApiMocks.js";

const server = setupServer(...[
    ...notesHandlers,
    ...institutionsHandlers,
    ...usersHandlers,
    ...directoriesHandlers
    ]
);

beforeAll(() => server.listen());

beforeEach( (context) => context.server = server);

afterEach(() => server.resetHandlers());

afterAll(() => server.close());

export {server};