import { useGetNoteQuery, useGetNoteFileQuery } from "../store/slices/noteApiSlice";
import { useEffect } from "react";
import { useParams } from "react-router-dom";

const Note = () => {
  const { noteId } = useParams();
  const noteQuery = useGetNoteQuery(noteId);
  const { data, isLoading, error } = noteQuery;
  const { note } = data || {};
  
  return (
    <section className="flex flex-col items-center mt-16 gap-8 text-center">
        {isLoading && <p>Loading...</p>}
        {error && <p>{error}</p>}
        {note?.name}
        <iframe class="h-100 w-100" src={`http://localhost:8080/paw-2023b-12/notes/${noteId}/file`}></iframe>
    </section>
  );

};


export default Note;