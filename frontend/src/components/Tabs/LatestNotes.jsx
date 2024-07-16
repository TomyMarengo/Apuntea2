import { NoteIconButton } from '../index';
import { useGetLatestNotesQuery } from '../../store/slices/notesApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';

const LatestNotes = () => {
  const user = useSelector(selectCurrentUser);

  const { data: dataLatestNotes, isLoading: isLoadingLatestNotes } = useGetLatestNotesQuery({ userId: user.id });

  return (
    <>
      {isLoadingLatestNotes ? (
        <p>Loading...</p>
      ) : dataLatestNotes.length > 0 ? (
        dataLatestNotes.map((note, index) => (
          <NoteIconButton key={index} name={note.name} noteId={note.id} fileType={note.fileType} />
        ))
      ) : (
        <p>No data available</p>
      )}
    </>
  );
};

export default LatestNotes;
