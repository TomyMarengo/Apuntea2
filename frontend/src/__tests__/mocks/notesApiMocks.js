import { http, HttpResponse } from "msw";
import {
    apiUrl,
    CREATED_RESPONSE,
    NO_CONTENT_RESPONSE,
    NOT_FOUND_RESPONSE,
    NOT_ACCEPTABLE_RESPONSE,
    UNSUPPORTED_MEDIA_TYPE_RESPONSE
} from "../setup/utils";
import {
    NOTE_COLLECTION_CONTENT_TYPE,
    NOTE_CONTENT_TYPE,
    NOTE_CREATE_CONTENT_TYPE,
    NOTE_UPDATE_CONTENT_TYPE
} from "../../contentTypes";

const notes = [
    {
        "avgScore": 0.0,
        "category": "THEORY",
        "createdAt": "2024-02-02T19:03:53.496",
        "file": "http://localhost:8080/paw-2023b-12/api/notes/30f1c039-7714-44ec-a10d-7c8039d16335/file",
        "fileType": "mp4",
        "id": "30f1c039-7714-44ec-a10d-7c8039d16335",
        "interactions": 0,
        "interactionsUri": "http://localhost:8080/paw-2023b-12/api/notes/30f1c039-7714-44ec-a10d-7c8039d16335/interactions",
        "lastModifiedAt": "2024-02-02T19:03:53.496",
        "name": "formaa",
        "owner": "http://localhost:8080/paw-2023b-12/api/users/a064c84b-b47a-4b25-b663-28e157c531d9",
        "parent": "http://localhost:8080/paw-2023b-12/api/directories/fd25786e-030c-4597-bc1e-0e8f4c41e0c1",
        "reviews": "http://localhost:8080/paw-2023b-12/api/reviews?noteId=30f1c039-7714-44ec-a10d-7c8039d16335",
        "self": "http://localhost:8080/paw-2023b-12/api/notes/30f1c039-7714-44ec-a10d-7c8039d16335",
        "subject": "http://localhost:8080/paw-2023b-12/api/subjects/99e70441-65f0-4aed-9f31-c681ca07ae52",
        "visible": true
    },
    {
        "avgScore": 0.0,
        "category": "THEORY",
        "createdAt": "2023-09-18T11:16:44.506421",
        "file": "http://localhost:8080/paw-2023b-12/api/notes/db3d94d2-c646-4d55-9655-785dd39341cc/file",
        "fileType": "pdf",
        "id": "db3d94d2-c646-4d55-9655-785dd39341cc",
        "interactions": 0,
        "interactionsUri": "http://localhost:8080/paw-2023b-12/api/notes/db3d94d2-c646-4d55-9655-785dd39341cc/interactions",
        "lastModifiedAt": "2023-10-02T10:24:05.169806",
        "name": "Numerical Methods by John Mathews",
        "owner": "http://localhost:8080/paw-2023b-12/api/users/a4187437-724d-4a18-81d9-d751270942e2",
        "parent": "http://localhost:8080/paw-2023b-12/api/directories/d187d207-e92c-45d6-9c88-fd3f50c8d87d",
        "reviews": "http://localhost:8080/paw-2023b-12/api/reviews?noteId=db3d94d2-c646-4d55-9655-785dd39341cc",
        "self": "http://localhost:8080/paw-2023b-12/api/notes/db3d94d2-c646-4d55-9655-785dd39341cc",
        "subject": "http://localhost:8080/paw-2023b-12/api/subjects/221f8752-463e-4c29-8667-31097219152f",
        "visible": true
    },
]
export const existingNoteId = notes[0].id;
export const existingNoteName = notes[0].name;
export const otherExistingNoteName = notes[1].name;
export const nonExistingNoteName = "nonExistingNoteName";

export const favUserId = "a064c84b-b47a-4b25-b663-28e157c531d9";

export const favNoteId = notes[0].id;
export const nonFavNoteId = notes[1].id;
export const fileErrorMsg = "The file name is already being used";


export const notesHandlers = [
    http.get(apiUrl("/notes/:id"), ({request, params}) => {
        if (request.headers.get("Accept") === NOTE_CONTENT_TYPE) {
            const note = notes.find(note => note.id === params.id);
            return HttpResponse.json(note);
        } else {
            return NOT_ACCEPTABLE_RESPONSE();
        }
    }),
    http.post(apiUrl("/notes"), async ( {request}) => {
        if(request.headers.get("Content-Type").includes(NOTE_CREATE_CONTENT_TYPE)) {
            const name = (await request.formData()).get("name");
            if (notes.find(n => n.name === name)) {
                return new HttpResponse(JSON.stringify({message: "The file name is already being used"}), {status: 400});
            }
            return CREATED_RESPONSE();
        } else {
            return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
        }
    }),

    http.patch(apiUrl("/notes/:id"), async ({request, params}) => {
        if(request.headers.get("Content-Type") === NOTE_UPDATE_CONTENT_TYPE) {
            const name = (await request.json()).name;
            if (notes.find(n => n.id === params.id)) {
                if (name && notes.find(n => (n.name === name && n.id !== params.id))) {
                    return new HttpResponse(JSON.stringify({message: "The file name is already being used"}), {status: 400});
                }
                return NO_CONTENT_RESPONSE();
            } else {
                return NOT_FOUND_RESPONSE();
            }
        } else {
            return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
        }
    }),

    http.get(apiUrl("/notes/:noteId/favorites/:userId"), ({request, params}) => {
        if (params.userId === favUserId && params.noteId === favNoteId) {
            return NO_CONTENT_RESPONSE();
        } else {
            return NOT_FOUND_RESPONSE();
        }
    }),
    http.get(apiUrl("/notes"), ({request, params}) => {
        if (request.headers.get("Accept") === NOTE_COLLECTION_CONTENT_TYPE) {
            return new HttpResponse(JSON.stringify(notes), {
                headers: {
                    "X-Total-Pages": 1,
                    "X-Total-Count": notes.length
                }, status: 200
            });
        } else {
            return NOT_ACCEPTABLE_RESPONSE();
        }
    }),

];