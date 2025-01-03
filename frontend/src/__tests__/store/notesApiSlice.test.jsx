import { expect, describe, it } from 'vitest';
import { setupApiStore } from '../setup/utils'; // Custom helper for setting up Redux store with RTK Query
import { useGetNoteQuery, notesApiSlice } from '../../store/slices/notesApiSlice';
import { waitFor } from '@testing-library/react';
import {useWithWrapper} from "../setup/wrapper.jsx";
// Setup API store helper
function setupNotesApiStore() {
    return setupApiStore(notesApiSlice);
}
const store = setupNotesApiStore();
describe('notesApiSlice', () => {

    it('should fetch notes successfully', async () => {
        let noteId = "30f1c039-7714-44ec-a10d-7c8039d16335";
        const { result } = useWithWrapper(() => useGetNoteQuery({ noteId }), store);
        await waitFor(() => expect(result.current.isSuccess).toBe(true));
        const note = result.current.data;
        expect(note).toBeTruthy();
    });

});