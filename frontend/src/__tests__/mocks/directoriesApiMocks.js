import { http, HttpResponse } from 'msw';

import {
  DIRECTORY_CONTENT_TYPE,
  DIRECTORY_COLLECTION_CONTENT_TYPE,
  DIRECTORY_CREATE_CONTENT_TYPE,
  DIRECTORY_UPDATE_CONTENT_TYPE,
} from '../../contentTypes';
import {
  apiUrl,
  CREATED_RESPONSE,
  NO_CONTENT_RESPONSE,
  NOT_ACCEPTABLE_RESPONSE,
  NOT_FOUND_RESPONSE,
  UNSUPPORTED_MEDIA_TYPE_RESPONSE,
} from '../setup/utils';

const directories = [
  {
    createdAt: '2023-10-30T02:12:08.326',
    iconColor: 'BBBBBB',
    id: 'd69cf972-0004-4adf-8e56-8a024e722b35',
    lastModifiedAt: '2023-10-30T02:12:08.326',
    name: 'aaaa',
    owner:
      'http://localhost:8080/paw-2023b-12/api/users/a064c84b-b47a-4b25-b663-28e157c531d9',
    parent:
      'http://localhost:8080/paw-2023b-12/api/directories/7a2f0b06-21a8-4d26-bda1-4ab2af663716',
    self: 'http://localhost:8080/paw-2023b-12/api/directories/d69cf972-0004-4adf-8e56-8a024e722b35',
    subject:
      'http://localhost:8080/paw-2023b-12/api/subjects/09236392-978a-4c4a-913a-524c102f2ab0',
    visible: true,
  },
  {
    createdAt: '2023-10-02T11:56:40.756282',
    iconColor: '4986E7',
    id: 'cb2cc2d7-3ebe-4968-8654-1a8d6b74eabd',
    lastModifiedAt: '2023-10-02T11:56:40.756282',
    name: 'Apuntes Logica Computacional',
    owner:
      'http://localhost:8080/paw-2023b-12/api/users/a4187437-724d-4a18-81d9-d751270942e2',
    parent:
      'http://localhost:8080/paw-2023b-12/api/directories/e85dbab4-e159-40b9-b08b-c4b34a5f6ac7',
    self: 'http://localhost:8080/paw-2023b-12/api/directories/cb2cc2d7-3ebe-4968-8654-1a8d6b74eabd',
    subject:
      'http://localhost:8080/paw-2023b-12/api/subjects/db895f9d-b5ad-49ac-9ecc-9715f51871e1',
    visible: true,
  },
];

export const favDirectoryId = directories[0].id;
export const favUserId = 'a064c84b-b47a-4b25-b663-28e157c531d9';
export const existingDirectoryId = directories[0].id;
export const existingDirectoryName = directories[0].name;
export const nonExistingDirectoryName = 'nonExistingName';
export const nonFavDirectoryId = directories[1].id;
export const otherExistingDirectoryName = directories[1].name;

export const fileErrorMsg = 'The file name is already being used';

export const directoriesHandlers = [
  http.get(apiUrl('/directories/:id'), ({ request, params }) => {
    if (request.headers.get('Accept') === DIRECTORY_CONTENT_TYPE) {
      const directory = directories.find(
        (directory) => directory.id === params.id,
      );
      return HttpResponse.json(directory);
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.post(apiUrl('/directories'), async ({ request }) => {
    if (request.headers.get('Content-Type') === DIRECTORY_CREATE_CONTENT_TYPE) {
      const name = (await request.json()).name;
      if (directories.find((n) => n.name === name)) {
        return new HttpResponse(JSON.stringify({ message: fileErrorMsg }), {
          status: 400,
        });
      }
      return CREATED_RESPONSE();
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),

  http.patch(apiUrl('/directories/:id'), async ({ request, params }) => {
    if (request.headers.get('Content-Type') === DIRECTORY_UPDATE_CONTENT_TYPE) {
      const name = (await request.json()).name;
      if (directories.find((n) => n.id === params.id)) {
        if (
          name &&
          directories.find((n) => n.name === name && n.id !== params.id)
        ) {
          return new HttpResponse(JSON.stringify({ message: fileErrorMsg }), {
            status: 400,
          });
        }
        return NO_CONTENT_RESPONSE();
      } else {
        return NOT_FOUND_RESPONSE();
      }
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),

  http.get(
    apiUrl('/directories/:directoryId/favorites/:userId'),
    ({ params }) => {
      if (
        params.userId === favUserId &&
        params.directoryId === favDirectoryId
      ) {
        return NO_CONTENT_RESPONSE();
      } else {
        return NOT_FOUND_RESPONSE();
      }
    },
  ),
  http.get(apiUrl('/directories'), ({ request }) => {
    if (request.headers.get('Accept') === DIRECTORY_COLLECTION_CONTENT_TYPE) {
      return new HttpResponse(JSON.stringify(directories), {
        headers: {
          'X-Total-Pages': 1,
          'X-Total-Count': directories.length,
        },
        status: 200,
      });
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
];
