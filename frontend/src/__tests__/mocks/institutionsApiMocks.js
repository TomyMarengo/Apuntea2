import { http, HttpResponse } from "msw";
import {apiUrl} from "../setup/utils";

const someInstitutionId = "4212733e-b8b8-473a-967b-944148bb2f60";
const someCareerId = "4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d";

const institutions = [
    {
        "careers": "http://localhost:8080/paw-2023b-12/api/institutions/de0d4b3e-3561-472c-8218-67ea590ed40e/careers",
        "id": "de0d4b3e-3561-472c-8218-67ea590ed40e",
        "name": "FIUBA",
        "self": "http://localhost:8080/paw-2023b-12/api/institutions/de0d4b3e-3561-472c-8218-67ea590ed40e"
    },
    {
        "careers": "http://localhost:8080/paw-2023b-12/api/institutions/4212733e-b8b8-473a-967b-944148bb2f60/careers",
        "id": "4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "ITBA",
        "self": "http://localhost:8080/paw-2023b-12/api/institutions/4212733e-b8b8-473a-967b-944148bb2f60"
    },
    {
        "careers": "http://localhost:8080/paw-2023b-12/api/institutions/e207affd-ab9d-4f7f-ae83-7cdc0c76955d/careers",
        "id": "e207affd-ab9d-4f7f-ae83-7cdc0c76955d",
        "name": "UTN",
        "self": "http://localhost:8080/paw-2023b-12/api/institutions/e207affd-ab9d-4f7f-ae83-7cdc0c76955d"
    }
]

const careers = [
    {
        "id": "b15f32b8-5256-4a63-8298-5e52299a589d",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Bioingeniería",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b15f32b8-5256-4a63-8298-5e52299a589d",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b15f32b8-5256-4a63-8298-5e52299a589d/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=b15f32b8-5256-4a63-8298-5e52299a589d",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=b15f32b8-5256-4a63-8298-5e52299a589d"
    },
    {
        "id": "b19783c2-7de8-4b72-b9f7-5f2a6058cd75",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería Electrónica",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b19783c2-7de8-4b72-b9f7-5f2a6058cd75",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/b19783c2-7de8-4b72-b9f7-5f2a6058cd75/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=b19783c2-7de8-4b72-b9f7-5f2a6058cd75",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=b19783c2-7de8-4b72-b9f7-5f2a6058cd75"
    },
    {
        "id": "6264c0ca-839f-48cb-ab5a-ed94303e15f6",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería en Petróleo",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/6264c0ca-839f-48cb-ab5a-ed94303e15f6",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/6264c0ca-839f-48cb-ab5a-ed94303e15f6/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=6264c0ca-839f-48cb-ab5a-ed94303e15f6",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=6264c0ca-839f-48cb-ab5a-ed94303e15f6"
    },
    {
        "id": "2ddd6bf0-2293-4a32-b2b0-739c08b99c33",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería Industrial",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/2ddd6bf0-2293-4a32-b2b0-739c08b99c33",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/2ddd6bf0-2293-4a32-b2b0-739c08b99c33/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=2ddd6bf0-2293-4a32-b2b0-739c08b99c33",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=2ddd6bf0-2293-4a32-b2b0-739c08b99c33"
    },
    {
        "id": "4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería Informática",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=4e0e1cef-808b-4b5c-9ef2-c08c90d3c22d"
    },
    {
        "id": "73396ae1-d40b-48c0-9121-21f9b25c51eb",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería Mecánica",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/73396ae1-d40b-48c0-9121-21f9b25c51eb",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/73396ae1-d40b-48c0-9121-21f9b25c51eb/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=73396ae1-d40b-48c0-9121-21f9b25c51eb",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=73396ae1-d40b-48c0-9121-21f9b25c51eb"
    },
    {
        "id": "929c7530-5fef-4b28-8fd7-fc6e06818543",
        "institution": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60",
        "name": "Ingeniería Química",
        "self": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/929c7530-5fef-4b28-8fd7-fc6e06818543",
        "subjectCareers": "http://localhost:8080/paw-2023b-12/api/institution/4212733e-b8b8-473a-967b-944148bb2f60/careers/929c7530-5fef-4b28-8fd7-fc6e06818543/subjectcareers",
        "subjects": "http://localhost:8080/paw-2023b-12/api/subjects?careerId=929c7530-5fef-4b28-8fd7-fc6e06818543",
        "subjectsNotInCareer": "http://localhost:8080/paw-2023b-12/api/subjects?notInCareer=929c7530-5fef-4b28-8fd7-fc6e06818543"
    }
]
export const institutionsHandlers = [
    http.get(apiUrl("/institutions/:id"), ({request, params}) => {
        const institution = institutions.find(institution => institution.id === params.id);
        return HttpResponse.json(institution);
    }),
    http.get(apiUrl("/institutions"), ({request}) => {
        return HttpResponse.json(institutions);
    }),
    http.get(apiUrl("/institutions/:institutionId/careers"), ({request, params}) => {
        if (institutions.find(institution => institution.id === params.institutionId) === undefined) {
            return new HttpResponse(null, {status: 404});
        } else {
            return HttpResponse.json(careers);
        }
    }),
    http.get(apiUrl("/institutions/:institutionId/careers/:careerId"), ({request, params}) => {
        if (institutions.find(institution => institution.id === params.institutionId) === undefined) {
            return new HttpResponse(null, {status: 404});
        } else {
            const career = careers.find(career => career.id === params.careerId);
            if (career === undefined) {
                return new HttpResponse(null, {status: 404});
            } else {
                return HttpResponse.json(career);
            }
        }
    }),



]