import { http, HttpResponse } from "msw";
import {apiUrl} from "../setup/utils";

export const notesHandlers = [
    http.get(apiUrl("/notes/:id"), ({request, params}) => {
        return HttpResponse.json({
            "avgScore": 0.0,
            "category": "THEORY",
            "createdAt": "2024-02-02T19:03:53.496",
            "file": "http://localhost:8080/paw-2023b-12/api/notes/30f1c039-7714-44ec-a10d-7c8039d16335/file",
            "fileType": "mp4",
            "id": params.id,
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
        });
    })
];