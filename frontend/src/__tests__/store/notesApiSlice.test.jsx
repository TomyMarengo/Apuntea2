import { expect, describe, it } from 'vitest';
import {expectToBePagedContent, setupApiStore} from '../setup/utils';
import {
    useGetNoteQuery,
    notesApiSlice,
    useCreateNoteMutation,
    useGetNoteFileQuery,
    useGetUserNotesFavoritesQuery,
    useGetIsFavoriteNoteQuery,
    useGetLatestNotesQuery,
    useUpdateNoteMutation
} from '../../store/slices/notesApiSlice';
import { waitFor } from '@testing-library/react';
import {useWithWrapper} from "../setup/wrapper.jsx";
import {
    existingNoteId,
    existingNoteName,
    favUserId,
    nonExistingNoteName,
    nonFavNoteId, otherExistingNoteName
} from "../mocks/notesApiMocks.js";
// Setup API store helper
function setupNotesApiStore() {
    return setupApiStore(notesApiSlice);
}
const store = setupNotesApiStore();
describe('notesApiSlice', () => {
    it('should fetch a note successfully', async () => {
        let noteId = existingNoteId;
        const { result } = useWithWrapper(() => useGetNoteQuery({ noteId }), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const note = result.current.data;
        expect(note).toBeTruthy();
        expect(note.id).toBe(noteId);
    });

    it('should fetch latest notes successfully', async () => {
        let noteId = existingNoteId;
        const { result } = useWithWrapper(() => useGetLatestNotesQuery({}), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const notes = result.current.data;
        expect(notes).toEqual(expect.any(Array));
    });

    it("should create a note successfully", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useCreateNoteMutation(), store);
        const [createNote, createNoteResult] = await wrapperResult.current;
        const result = await createNote({name: nonExistingNoteName}).unwrap();
        expect(result.success).toBe(true);
        expect(result.messages).empty;
    });

    it("should fail to create a note", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useCreateNoteMutation(), store);
        const [createNote, createNoteResult] = await wrapperResult.current;
        const result = await createNote({name: existingNoteName}).unwrap();
        expect(result.success).toBe(false);
        expect(result.messages).not.empty;
    });

    it("should modify note successfully with nonExistingNoteName", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useUpdateNoteMutation(), store);
        const [updateNote] = await wrapperResult.current;
        const result = await updateNote({noteId: existingNoteId, name: nonExistingNoteName}).unwrap();
        expect(result.success).toBe(true);
        expect(result.messages).empty;
    });

    it("should modify note successfully with no name change", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useUpdateNoteMutation(), store);
        const [updateNote] = await wrapperResult.current;
        const result = await updateNote({noteId: existingNoteId, visible: false}).unwrap();
        expect(result.success).toBe(true);
        expect(result.messages).empty;
    });

    it("should modify note successfully with the same old name", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useUpdateNoteMutation(), store);
        const [updateNote] = await wrapperResult.current;
        const result = await updateNote({noteId: existingNoteId, name: existingNoteName}).unwrap();
        expect(result.success).toBe(true);
        expect(result.messages).empty;
    });

    it("should fail to modify name", async () => {
        let { result: wrapperResult} = await useWithWrapper(async ()=> await useUpdateNoteMutation(), store);
        const [updateNote] = await wrapperResult.current;
        const result = await updateNote({noteId: existingNoteId, name: otherExistingNoteName}).unwrap();
        expect(result.success).toBe(false);
        expect(result.messages).not.empty;
    });

    it("should get the note as favorite", async () => {
        const { result } = useWithWrapper(() => useGetIsFavoriteNoteQuery({ noteId: existingNoteId, userId: favUserId }), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const isFavorite = result.current.data.success;
        expect(isFavorite).toBe(true);
    });

    it("should get the note as not favorite", async () => {
        const { result } = useWithWrapper(() => useGetIsFavoriteNoteQuery({ noteId: nonFavNoteId, userId: favUserId }), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const isFavorite = result.current.data.success;
        expect(isFavorite).toBe(false);
    });

    it('should fetch favorite notes succesfully', async () => {
        let noteId = existingNoteId;
        const { result } = useWithWrapper(() => useGetUserNotesFavoritesQuery({}), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const { notes, totalCount, totalPages } = result.current.data;
        expectToBePagedContent(notes, totalCount, totalPages);
    });



});

//--useGetNoteQuery
//  useGetNoteFileQuery,
//--useCreateNoteMutation,
//--useUpdateNoteMutation,
//  useDeleteNoteMutation,
//--useGetUserNotesFavoritesQuery,
//--useGetIsFavoriteNoteQuery,
//  useAddFavoriteNoteMutation,
//  useRemoveFavoriteNoteMutation,
//  useAddInteractionNoteMutation,
//--useGetLatestNotesQuery,