import { http, HttpResponse } from 'msw';

import {
  NOTE_COLLECTION_CONTENT_TYPE,
  NOTE_CONTENT_TYPE,
  MULTIPART_FORM_DATA_CONTENT_TYPE,
} from '../../contentTypes';
import {
  apiUrl,
  CREATED_RESPONSE,
  NO_CONTENT_RESPONSE,
  NOT_FOUND_RESPONSE,
  NOT_ACCEPTABLE_RESPONSE,
  UNSUPPORTED_MEDIA_TYPE_RESPONSE,
} from '../setup/utils';

const notes = [
  {
    avgScore: 0.0,
    category: 'THEORY',
    createdAt: '2024-02-02T19:03:53.496',
    file: apiUrl('/notes/30f1c039-7714-44ec-a10d-7c8039d16335/file'),
    fileType: 'mp4',
    id: '30f1c039-7714-44ec-a10d-7c8039d16335',
    interactions: 0,
    interactionsUri: apiUrl(
      '/notes/30f1c039-7714-44ec-a10d-7c8039d16335/interactions',
    ),
    lastModifiedAt: '2024-02-02T19:03:53.496',
    name: 'formaa',
    owner: apiUrl('/users/a064c84b-b47a-4b25-b663-28e157c531d9'),
    parent: apiUrl('/directories/fd25786e-030c-4597-bc1e-0e8f4c41e0c1'),
    reviews: apiUrl('/reviews?noteId=30f1c039-7714-44ec-a10d-7c8039d16335'),
    self: apiUrl('/notes/30f1c039-7714-44ec-a10d-7c8039d16335'),
    subject: apiUrl('/subjects/99e70441-65f0-4aed-9f31-c681ca07ae52'),
    visible: true,
  },
  {
    avgScore: 0.0,
    category: 'THEORY',
    createdAt: '2023-09-18T11:16:44.506421',
    file: apiUrl('/notes/db3d94d2-c646-4d55-9655-785dd39341cc/file'),
    fileType: 'pdf',
    id: 'db3d94d2-c646-4d55-9655-785dd39341cc',
    interactions: 0,
    interactionsUri: apiUrl(
      '/notes/db3d94d2-c646-4d55-9655-785dd39341cc/interactions',
    ),
    lastModifiedAt: '2023-10-02T10:24:05.169806',
    name: 'Numerical Methods by John Mathews',
    owner: apiUrl('/users/a4187437-724d-4a18-81d9-d751270942e2'),
    parent: apiUrl('/directories/d187d207-e92c-45d6-9c88-fd3f50c8d87d'),
    reviews: apiUrl('/reviews?noteId=db3d94d2-c646-4d55-9655-785dd39341cc'),
    self: apiUrl('/notes/db3d94d2-c646-4d55-9655-785dd39341cc'),
    subject: apiUrl('/subjects/221f8752-463e-4c29-8667-31097219152f'),
    visible: true,
  },
];
export const existingNoteId = notes[0].id;
export const existingNoteName = notes[0].name;
export const otherExistingNoteName = notes[1].name;
export const nonExistingNoteName = 'nonExistingNoteName';

export const favUserId = 'a064c84b-b47a-4b25-b663-28e157c531d9';

export const favNoteId = notes[0].id;
export const nonFavNoteId = notes[1].id;
export const fileErrorMsg = 'The file name is already being used';
export const newId = '00000000-0000-0000-0000-000000000000';

export const notesHandlers = [
  http.get(apiUrl('/notes/:id'), ({ request, params }) => {
    if (request.headers.get('Accept') === NOTE_CONTENT_TYPE) {
      const note = notes.find((note) => note.id === params.id);
      return HttpResponse.json(note);
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.post(apiUrl('/notes'), async ({ request }) => {
    if (
      request.headers
        .get('Content-Type')
        .includes(MULTIPART_FORM_DATA_CONTENT_TYPE)
    ) {
      const name = (await request.formData())?.get('name');
      if (notes.find((n) => n.name === name)) {
        return new HttpResponse(
          JSON.stringify({ message: 'The file name is already being used' }),
          { status: 400 },
        );
      }
      return CREATED_RESPONSE(apiUrl(`/notes/${newId}`));
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),

  http.patch(apiUrl('/notes/:id'), async ({ request, params }) => {
    if (request.headers.get('Content-Type') === NOTE_CONTENT_TYPE) {
      const name = (await request.json()).name;
      if (notes.find((n) => n.id === params.id)) {
        if (name && notes.find((n) => n.name === name && n.id !== params.id)) {
          return new HttpResponse(
            JSON.stringify({ message: 'The file name is already being used' }),
            { status: 400 },
          );
        }
        return NO_CONTENT_RESPONSE();
      } else {
        return NOT_FOUND_RESPONSE();
      }
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),

  http.get(apiUrl('/notes/:noteId/favorites/:userId'), ({ params }) => {
    if (params.userId === favUserId && params.noteId === favNoteId) {
      return NO_CONTENT_RESPONSE();
    } else {
      return NOT_FOUND_RESPONSE();
    }
  }),
  http.get(apiUrl('/notes'), ({ request }) => {
    if (request.headers.get('Accept') === NOTE_COLLECTION_CONTENT_TYPE) {
      return new HttpResponse(JSON.stringify(notes), {
        headers: {
          'X-Total-Pages': 1,
          'X-Total-Count': notes.length,
        },
        status: 200,
      });
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
];
