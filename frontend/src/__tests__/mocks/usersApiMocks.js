import { http, HttpResponse } from 'msw';

import {
  USER_COLLECTION_CONTENT_TYPE,
  USER_CONTENT_TYPE,
  USER_EMAIL_COLLECTION_CONTENT_TYPE,
  USER_REQUEST_PASSWORD_CHANGE_CONTENT_TYPE,
  USER_PASSWORD_CONTENT_TYPE,
  USER_STATUS_REASON_CONTENT_TYPE,
} from '../../contentTypes';
import {
  apiUrl,
  CREATED_RESPONSE,
  NO_CONTENT_RESPONSE,
  NOT_ACCEPTABLE_RESPONSE,
  NOT_FOUND_RESPONSE,
  UNAUTHORIZED_RESPONSE,
  UNSUPPORTED_MEDIA_TYPE_RESPONSE,
} from '../setup/utils';

const users = [
  {
    career: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
    directoryFavorites: apiUrl(
      '/directories?favBy=409f7319-b248-4d38-a450-4305ef0f094b',
    ),
    followedBy: apiUrl(
      '/users?followedBy=409f7319-b248-4d38-a450-4305ef0f094b',
    ),
    following: apiUrl('/users?following=409f7319-b248-4d38-a450-4305ef0f094b'),
    id: '409f7319-b248-4d38-a450-4305ef0f094b',
    institution: apiUrl('/institutions/4212733e-b8b8-473a-967b-944148bb2f60'),
    locale: 'es',
    noteFavorites: apiUrl('/notes?favBy=409f7319-b248-4d38-a450-4305ef0f094b'),
    notes: apiUrl('/notes?userId=409f7319-b248-4d38-a450-4305ef0f094b'),
    notificationsEnabled: true,
    reviewsReceived: apiUrl(
      '/reviews?targetUser=409f7319-b248-4d38-a450-4305ef0f094b',
    ),
    self: apiUrl('/users/409f7319-b248-4d38-a450-4305ef0f094b'),
    status: 'ACTIVE',
    subjectFavorites: apiUrl(
      '/directories?favBy=409f7319-b248-4d38-a450-4305ef0f094b&rdir=true',
    ),
    subjects: apiUrl('/subjects?userId=409f7319-b248-4d38-a450-4305ef0f094b'),
    username: 'aluinfo',
    email: 'aluinfo@itba.edu.ar',
  },
  {
    career: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/73396ae1-d40b-48c0-9121-21f9b25c51eb',
    ),
    directoryFavorites: apiUrl(
      '/directories?favBy=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6',
    ),
    followedBy: apiUrl(
      '/users?followedBy=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6',
    ),
    following: apiUrl('/users?following=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6'),
    id: '10dda953-9e81-4ba0-aaa5-b1e4574ca4c6',
    institution: apiUrl('/institutions/4212733e-b8b8-473a-967b-944148bb2f60'),
    locale: 'es',
    noteFavorites: apiUrl('/notes?favBy=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6'),
    notes: apiUrl('/notes?userId=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6'),
    notificationsEnabled: true,
    reviewsReceived: apiUrl(
      '/reviews?targetUser=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6',
    ),
    self: apiUrl('/users/10dda953-9e81-4ba0-aaa5-b1e4574ca4c6'),
    status: 'ACTIVE',
    subjectFavorites: apiUrl(
      '/directories?favBy=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6&rdir=true',
    ),
    subjects: apiUrl('/subjects?userId=10dda953-9e81-4ba0-aaa5-b1e4574ca4c6'),
    username: 'alumeca',
    email: 'alumeca@itba.edu.ar',
  },
];

export const someUserId = users[0].id;
export const existingEmail = users[0].email;
export const nonExistingEmail = 'nonExistingEmail@mail';
export const existingUsername = users[0].username;
export const nonExistingUsername = 'nonExistingName';

export const followerUserId = users[1].id;

export const passwordChangeCode = '1234';

export const usedUsernameMsg = 'The username is already in use';
export const usedMailMsg = 'The email is already in use';
export const newId = '00000000-0000-0000-0000-000000000000';

export const usersHandlers = [
  http.get(apiUrl('/users'), ({ request }) => {
    if (request.headers.get('Accept') === USER_EMAIL_COLLECTION_CONTENT_TYPE) {
      return new HttpResponse(JSON.stringify(users), {
        headers: {
          'X-Total-Pages': 1,
          'X-Total-Count': users.length,
        },
        status: 200,
      });
    } else if (request.headers.get('Accept') === USER_COLLECTION_CONTENT_TYPE) {
      const usersWithOutMail = users.map(({ email, ...user }) => user);
      return new HttpResponse(JSON.stringify(usersWithOutMail), {
        headers: {
          'X-Total-Pages': 1,
          'X-Total-Count': users.length,
        },
        status: 200,
      });
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.get(apiUrl('/users/:id'), ({ params }) => {
    const user = users.find((user) => user.id === params.id);
    return HttpResponse.json(user);
  }),
  http.post(apiUrl('/users'), async ({ request }) => {
    if (request.headers.get('Content-Type') === USER_CONTENT_TYPE) {
      const user = await request.json();
      if (users.find((u) => u.email === user.email)) {
        return new HttpResponse(undefined, { status: 400 });
      }
      return CREATED_RESPONSE(apiUrl(`/users/${newId}`));
    } else if (
      request.headers.get('Content-Type') ===
      USER_REQUEST_PASSWORD_CHANGE_CONTENT_TYPE
    ) {
      const body = await request.json();
      if (users.find((u) => u.email === body.email)) {
        return NO_CONTENT_RESPONSE();
      }
      return NOT_FOUND_RESPONSE();
    } else if (
      request.headers.get('Content-Type') === USER_PASSWORD_CONTENT_TYPE
    ) {
      const auth = request.headers.get('Authorization');
      const [email, code] = atob(auth.split(' ')[1]).split(':');
      if (users.find((u) => u.email === email && code === passwordChangeCode)) {
        return NO_CONTENT_RESPONSE();
      }
      return UNAUTHORIZED_RESPONSE();
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),
  http.post(apiUrl('/users/:id'), async ({ request, params }) => {
    if (
      request.headers.get('Content-Type') === USER_STATUS_REASON_CONTENT_TYPE
    ) {
      if (users.find((u) => u.id === params.id)) {
        return NO_CONTENT_RESPONSE();
      } else {
        return NOT_FOUND_RESPONSE();
      }
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),
  http.patch(apiUrl('/users/:id'), async ({ request, params }) => {
    if (request.headers.get('Content-Type') === USER_CONTENT_TYPE) {
      const user = await request.json();
      if (users.find((u) => u.id === params.id)) {
        if (user.username && users.find((u) => u.username === user.username)) {
          return new HttpResponse(
            JSON.stringify({ message: usedUsernameMsg }),
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
  http.get(apiUrl('/users/:userId/followers/:followerId'), ({ params }) => {
    if (params.userId === someUserId && params.followerId === followerUserId) {
      return NO_CONTENT_RESPONSE();
    } else {
      return NOT_FOUND_RESPONSE();
    }
  }),
];
