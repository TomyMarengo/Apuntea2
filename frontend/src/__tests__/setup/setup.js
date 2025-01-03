import { setupServer } from "msw/node";
import { beforeAll, beforeEach, afterEach, afterAll } from "vitest";
import {notesHandlers} from "../mocks/notesApiMocks.js";

const server = setupServer(...[
    ...notesHandlers
    ]
);

beforeAll(() => server.listen());

beforeEach( (context) => context.server = server);

afterEach(() => server.resetHandlers());

afterAll(() => server.close());

export {server};