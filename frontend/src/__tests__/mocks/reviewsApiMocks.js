import { http, HttpResponse } from "msw";
import {
    apiUrl,
    CREATED_RESPONSE,
    NO_CONTENT_RESPONSE,
    NOT_ACCEPTABLE_RESPONSE, NOT_FOUND_RESPONSE,
    UNSUPPORTED_MEDIA_TYPE_RESPONSE
} from "../setup/utils";
import {
    REVIEW_COLLECTION_CONTENT_TYPE,
    REVIEW_CREATE_CONTENT_TYPE,
    REVIEW_UPDATE_CONTENT_TYPE
} from "../../contentTypes";

const reviews = [
    {
        "content": "hola",
        "createdAt": "2023-11-27T12:46:53.127",
        "note": "http://localhost:8080/paw-2023b-12/api/notes/0d5a1e12-fb4d-4bb7-9efe-07f3f3352739",
        "noteId": "0d5a1e12-fb4d-4bb7-9efe-07f3f3352739",
        "score": 5,
        "self": "http://localhost:8080/paw-2023b-12/api/reviews/0d5a1e12-fb4d-4bb7-9efe-07f3f3352739_a4187437-724d-4a18-81d9-d751270942e2",
        "user": "http://localhost:8080/paw-2023b-12/api/users/a4187437-724d-4a18-81d9-d751270942e2",
        "userId": "a4187437-724d-4a18-81d9-d751270942e2"
    },
    {
        "content": "ta largo",
        "createdAt": "2023-11-18T11:47:22.311",
        "note": "http://localhost:8080/paw-2023b-12/api/notes/5d90e0a3-e8f2-4792-b676-50199c6a64b5",
        "noteId": "5d90e0a3-e8f2-4792-b676-50199c6a64b5",
        "score": 5,
        "self": "http://localhost:8080/paw-2023b-12/api/reviews/5d90e0a3-e8f2-4792-b676-50199c6a64b5_a4187437-724d-4a18-81d9-d751270942e2",
        "user": "http://localhost:8080/paw-2023b-12/api/users/a4187437-724d-4a18-81d9-d751270942e2",
        "userId": "a4187437-724d-4a18-81d9-d751270942e2"
    },
    {
        "content": "",
        "createdAt": "2023-10-30T12:58:54.177",
        "note": "http://localhost:8080/paw-2023b-12/api/notes/243fb1a3-9750-4972-9bdd-8ef80e26ab01",
        "noteId": "243fb1a3-9750-4972-9bdd-8ef80e26ab01",
        "score": 1,
        "self": "http://localhost:8080/paw-2023b-12/api/reviews/243fb1a3-9750-4972-9bdd-8ef80e26ab01_d92450df-9f2d-4e56-b11d-291bfa85b88b",
        "user": "http://localhost:8080/paw-2023b-12/api/users/d92450df-9f2d-4e56-b11d-291bfa85b88b",
        "userId": "d92450df-9f2d-4e56-b11d-291bfa85b88b"
    },
]

export const someUserId = reviews[0].userId;
export const someNoteId = reviews[0].noteId;

export const invalidScoreMsg = "The Score is invalid";
export const reviewsHandles = [
    http.get(apiUrl("/reviews"), ({request, params}) => {
        if (request.headers.get("Accept") === REVIEW_COLLECTION_CONTENT_TYPE) {
            let noteId = params.noteId;
            let userId = params.userId;
            return new HttpResponse(JSON.stringify(reviews.filter(review => (!noteId || review.noteId === noteId) && (!userId || review.userId === userId))), {
                headers: {
                    "X-Total-Pages": 1,
                    "X-Total-Count": reviews.length
                }, status: 200
            });
        } else {
            return NOT_ACCEPTABLE_RESPONSE();
        }
    }),
    http.get(apiUrl("/reviews/:noteIdUserId"), ({request, params}) => {
        const [noteId, userId] = params.noteIdUserId.split("_");
        if (request.headers.get("Accept") !== REVIEW_COLLECTION_CONTENT_TYPE) {
            let review = reviews.find(review => review.noteId === noteId && review.userId === userId);
            if (review) {
                return new HttpResponse(JSON.stringify(review), {status: 200});
            } else {
                return NOT_FOUND_RESPONSE();
            }
        } else {
            return NOT_ACCEPTABLE_RESPONSE();
        }
    }),
    http.post(apiUrl("/reviews"), async ({request}) => {
        if (request.headers.get("Content-Type") === REVIEW_CREATE_CONTENT_TYPE) {
            let review = await request.json();
            if (!review.score || review.score < 1 || review.score > 5) {
                return new HttpResponse(JSON.stringify({message: invalidScoreMsg}), {status: 400});
            }
            return CREATED_RESPONSE();
        } else {
            return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
        }
    }),
    http.patch(apiUrl("/reviews/:noteIdUserId"), async ({request, params}) => {
        if (request.headers.get("Content-Type") === REVIEW_UPDATE_CONTENT_TYPE) {
            const [noteId, userId] = params.noteIdUserId.split("_");
            let review = await request.json();
            if (!review.score || review.score < 1 || review.score > 5) {
                return new HttpResponse(JSON.stringify({message: invalidScoreMsg}), {status: 400});
            }
            return NO_CONTENT_RESPONSE();
        } else {
            return UNSUPPORTED_MEDIA_TYPE_RESPONSE();
        }
    }),
]