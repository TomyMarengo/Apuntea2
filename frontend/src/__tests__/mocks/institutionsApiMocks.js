import { http, HttpResponse } from 'msw';

import {
  CAREER_COLLECTION_CONTENT_TYPE,
  CAREER_CONTENT_TYPE,
  INSTITUTION_COLLECTION_CONTENT_TYPE,
  INSTITUTION_CONTENT_TYPE,
  SUBJECT_CAREER_COLLECTION_CONTENT_TYPE,
  SUBJECT_CAREER_CONTENT_TYPE,
  SUBJECT_COLLECTION_CONTENT_TYPE,
  SUBJECT_CONTENT_TYPE,
} from '../../contentTypes';
import {
  apiUrl,
  CREATED_RESPONSE,
  NO_CONTENT_RESPONSE,
  NOT_ACCEPTABLE_RESPONSE,
  NOT_FOUND_RESPONSE,
  UNSUPPORTED_MEDIA_TYPE_RESPONSE,
} from '../setup/utils';
const institutions = [
  {
    careers: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers',
    ),
    id: '4212733e-b8b8-473a-967b-944148bb2f60',
    name: 'ITBA',
    self: apiUrl('/institutions/4212733e-b8b8-473a-967b-944148bb2f60'),
  },
  {
    careers: apiUrl(
      '/institutions/de0d4b3e-3561-472c-8218-67ea590ed40e/careers',
    ),
    id: 'de0d4b3e-3561-472c-8218-67ea590ed40e',
    name: 'FIUBA',
    self: apiUrl('/institutions/de0d4b3e-3561-472c-8218-67ea590ed40e'),
  },
  {
    careers: apiUrl(
      '/institutions/e207affd-ab9d-4f7f-ae83-7cdc0c76955d/careers',
    ),
    id: 'e207affd-ab9d-4f7f-ae83-7cdc0c76955d',
    name: 'UTN',
    self: apiUrl('/institutions/e207affd-ab9d-4f7f-ae83-7cdc0c76955d'),
  },
];

export const someInstitutionId = institutions[0].id;

const careers = [
  {
    id: 'b15f32b8-5256-4a63-8298-5e52299a589d',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Bioingeniería',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b15f32b8-5256-4a63-8298-5e52299a589d',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b15f32b8-5256-4a63-8298-5e52299a589d/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=b15f32b8-5256-4a63-8298-5e52299a589d'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=b15f32b8-5256-4a63-8298-5e52299a589d',
    ),
  },
  {
    id: 'b19783c2-7de8-4b72-b9f7-5f2a6058cd75',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería Electrónica',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b19783c2-7de8-4b72-b9f7-5f2a6058cd75',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b19783c2-7de8-4b72-b9f7-5f2a6058cd75/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=b19783c2-7de8-4b72-b9f7-5f2a6058cd75'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=b19783c2-7de8-4b72-b9f7-5f2a6058cd75',
    ),
  },
  {
    id: '6264c0ca-839f-48cb-ab5a-ed94303e15f6',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería en Petróleo',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/6264c0ca-839f-48cb-ab5a-ed94303e15f6',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/6264c0ca-839f-48cb-ab5a-ed94303e15f6/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=6264c0ca-839f-48cb-ab5a-ed94303e15f6'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=6264c0ca-839f-48cb-ab5a-ed94303e15f6',
    ),
  },
  {
    id: '2ddd6bf0-2293-4a32-b2b0-739c08b99c33',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería Industrial',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/2ddd6bf0-2293-4a32-b2b0-739c08b99c33',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/2ddd6bf0-2293-4a32-b2b0-739c08b99c33/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=2ddd6bf0-2293-4a32-b2b0-739c08b99c33'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=2ddd6bf0-2293-4a32-b2b0-739c08b99c33',
    ),
  },
  {
    id: '4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería Informática',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
  },
  {
    id: '73396ae1-d40b-48c0-9121-21f9b25c51eb',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería Mecánica',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/73396ae1-d40b-48c0-9121-21f9b25c51eb',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/73396ae1-d40b-48c0-9121-21f9b25c51eb/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=73396ae1-d40b-48c0-9121-21f9b25c51eb'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=73396ae1-d40b-48c0-9121-21f9b25c51eb',
    ),
  },
  {
    id: '929c7530-5fef-4b28-8fd7-fc6e06818543',
    institution: apiUrl('/institution/4212733e-b8b8-473a-967b-944148bb2f60'),
    name: 'Ingeniería Química',
    self: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/929c7530-5fef-4b28-8fd7-fc6e06818543',
    ),
    subjectCareers: apiUrl(
      '/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/929c7530-5fef-4b28-8fd7-fc6e06818543/subjectcareers',
    ),
    subjects: apiUrl('/subjects?careerId=929c7530-5fef-4b28-8fd7-fc6e06818543'),
    subjectsNotInCareer: apiUrl(
      '/subjects?notInCareer=929c7530-5fef-4b28-8fd7-fc6e06818543',
    ),
  },
];

export const someCareerId = careers[0].id;

const subjects = [
  {
    id: '617d8c5a-b2f7-41a2-93b2-d2a02f2e26f3',
    name: '72.03 - Introducción a la Informática',
    rootDirectory: apiUrl('/directories/5f04b1d2-5d55-43d5-8c5b-6fc4d7e4f452'),
    self: apiUrl('/subjects/617d8c5a-b2f7-41a2-93b2-d2a02f2e26f3'),
  },
  {
    id: '80d48f35-0cfd-4713-a760-3631fbfea80d',
    name: '72.07 - Protocolos de Comunicación',
    rootDirectory: apiUrl('/directories/5fde7edf-ec91-48cb-8279-870b0b942d18'),
    self: apiUrl('/subjects/80d48f35-0cfd-4713-a760-3631fbfea80d'),
  },
  {
    id: '37fd865f-19f1-4f8e-80b6-2f82b9e2aa23',
    name: '72.08 - Arquitectura de Computadoras',
    rootDirectory: apiUrl('/directories/6e456ee9-733d-4a17-b757-233cc7e72835'),
    self: apiUrl('/subjects/37fd865f-19f1-4f8e-80b6-2f82b9e2aa23'),
  },
];
export const someSubjectId = subjects[0].id;

export const invalidName = '-';

export const emptyNameMsg = 'The parameter must not be empty';
export const invalidYearMsg = 'The year must be greater than 0';
export const newId = '00000000-0000-0000-0000-000000000000';

const subjectCareers = [
  {
    career: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
    self: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d/subjectcareers/f259d0c3-4a3d-4688-82db-1ccc45f5eaaa',
    ),
    subject: apiUrl('/subjects/f259d0c3-4a3d-4688-82db-1ccc45f5eaaa'),
    year: 3,
  },
  {
    career: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
    self: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d/subjectcareers/ef680eaa-7f29-42ee-a419-7547119c9ce4',
    ),
    subject: apiUrl('/subjects/ef680eaa-7f29-42ee-a419-7547119c9ce4'),
    year: 4,
  },
  {
    career: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d',
    ),
    self: apiUrl(
      '/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d/subjectcareers/ba237f87-4c8e-4882-a009-45820517a55f',
    ),
    subject: apiUrl('/subjects/ba237f87-4c8e-4882-a009-45820517a55f'),
    year: 4,
  },
];

export const institutionsHandlers = [
  http.get(apiUrl('/institutions/:id'), ({ request, params }) => {
    if (request.headers.get('Accept') === INSTITUTION_CONTENT_TYPE) {
      const institution = institutions.find(
        (institution) => institution.id === params.id,
      );
      return HttpResponse.json(institution);
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.get(apiUrl('/institutions'), ({ request }) => {
    if (request.headers.get('Accept') === INSTITUTION_COLLECTION_CONTENT_TYPE) {
      return HttpResponse.json(institutions);
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.get(
    apiUrl('/institutions/:institutionId/careers'),
    ({ request, params }) => {
      if (request.headers.get('Accept') === CAREER_COLLECTION_CONTENT_TYPE) {
        const institution = institutions.find(
          (institution) => institution.id === params.institutionId,
        );
        return institution ? HttpResponse.json(careers) : NOT_FOUND_RESPONSE();
      } else {
        return NOT_ACCEPTABLE_RESPONSE();
      }
    },
  ),
  http.get(
    apiUrl('/institutions/:institutionId/careers/:careerId'),
    ({ request, params }) => {
      if (request.headers.get('Accept') === CAREER_CONTENT_TYPE) {
        if (
          institutions.find(
            (institution) => institution.id === params.institutionId,
          ) === undefined
        ) {
          return NOT_FOUND_RESPONSE();
        } else {
          const career = careers.find(
            (career) => career.id === params.careerId,
          );
          return career ? HttpResponse.json(career) : NOT_FOUND_RESPONSE();
        }
      } else {
        return NOT_ACCEPTABLE_RESPONSE();
      }
    },
  ),
  http.get(apiUrl('/subjects'), ({ request }) => {
    if (request.headers.get('Accept') === SUBJECT_COLLECTION_CONTENT_TYPE) {
      return HttpResponse.json(subjects);
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.get(apiUrl('/subjects/:id'), ({ request, params }) => {
    if (request.headers.get('Accept') === SUBJECT_CONTENT_TYPE) {
      const subject = subjects.find((subject) => subject.id === params.id);
      return subject ? HttpResponse.json(subject) : NOT_FOUND_RESPONSE();
    } else {
      return NOT_ACCEPTABLE_RESPONSE();
    }
  }),
  http.post(apiUrl('/subjects'), async ({ request }) => {
    if (request.headers.get('Content-Type') === SUBJECT_CONTENT_TYPE) {
      const subject = await request.json();
      if (subject.name && subject.name !== '') {
        subject.id = newId;
        subject.rootDirectory = apiUrl(`/directories/${newId}`);
        subject.self = apiUrl(`/subjects/${newId}`);
        subjects.push(subject);
        return CREATED_RESPONSE(subject.self);
      } else {
        return new HttpResponse(JSON.stringify({ message: emptyNameMsg }), {
          status: 400,
        });
      }
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),
  http.put(apiUrl('/subjects/:subjectId'), async ({ request, params }) => {
    if (request.headers.get('Content-Type') === SUBJECT_CONTENT_TYPE) {
      if (
        subjects.find((subject) => subject.id === params.subjectId) ===
        undefined
      ) {
        return NOT_FOUND_RESPONSE();
      }
      const subject = await request.json();
      return subject.name && subject.name !== invalidName
        ? NO_CONTENT_RESPONSE()
        : new HttpResponse(JSON.stringify({ message: emptyNameMsg }), {
            status: 400,
          });
    } else {
      return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
    }
  }),
  http.post(
    apiUrl('/institutions/:institutionId/careers/:careerId/subjectcareers'),
    async ({ request, params }) => {
      if (request.headers.get('Content-Type') === SUBJECT_CAREER_CONTENT_TYPE) {
        if (
          careers.find((career) => career.id === params.careerId) ===
            undefined ||
          institutions.find(
            (institution) => institution.id === params.institutionId,
          ) === undefined
        ) {
          return NOT_FOUND_RESPONSE();
        }
        const subjectCareer = await request.json();
        return subjectCareer.year >= 0
          ? CREATED_RESPONSE(
              apiUrl(
                `/institutions/${params.institutionId}/careers/${params.careerId}/subjectcareers/${subjectCareer.subjectId}`,
              ),
            )
          : new HttpResponse(JSON.stringify({ message: invalidYearMsg }), {
              status: 400,
            });
      } else {
        return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
      }
    },
  ),
  http.put(
    apiUrl(
      '/institutions/:institutionId/careers/:careerId/subjectcareers/:subjectId',
    ),
    async ({ request, params }) => {
      if (request.headers.get('Content-Type') === SUBJECT_CAREER_CONTENT_TYPE) {
        if (
          careers.find((career) => career.id === params.careerId) ===
            undefined ||
          institutions.find(
            (institution) => institution.id === params.institutionId,
          ) === undefined ||
          subjects.find((subject) => subject.id === params.subjectId) ===
            undefined
        ) {
          return NOT_FOUND_RESPONSE();
        }
        const subjectCareer = await request.json();
        return subjectCareer.year >= 0
          ? NO_CONTENT_RESPONSE()
          : new HttpResponse(JSON.stringify({ message: invalidYearMsg }), {
              status: 400,
            });
      } else {
        return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
      }
    },
  ),
  http.get(
    apiUrl('/institutions/:institutionId/careers/:careerId/subjectcareers'),
    ({ request, params }) => {
      if (
        request.headers.get('Accept') === SUBJECT_CAREER_COLLECTION_CONTENT_TYPE
      ) {
        if (
          careers.find((career) => career.id === params.careerId) ===
            undefined ||
          institutions.find(
            (institution) => institution.id === params.institutionId,
          ) === undefined
        ) {
          return NOT_FOUND_RESPONSE();
        }
        return HttpResponse.json(subjectCareers);
      } else {
        return NOT_ACCEPTABLE_RESPONSE();
      }
    },
  ),
];
