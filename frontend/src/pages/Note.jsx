import { useTranslation } from 'react-i18next';
import { DeleteButton, FavoriteButton, DownloadButton, ReviewsContainer, RequireAuth } from '../components/index';
import { NavLink } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import {
  useGetNoteQuery,
  useGetIsFavoriteNoteQuery,
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
} from '../store/slices/notesApiSlice';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUserId } from '../store/slices/authSlice';
import { BottomNavbar } from '../components/index';

const Note = () => {
  const { t } = useTranslation();

  const userId = useSelector(selectCurrentUserId);
  const { noteId } = useParams();
  const { data: note, isLoading: isLoadingNote, error: errorNote } = useGetNoteQuery({ noteId });

  console.log(note);
  const {
    data: noteOwner,
    isLoading: isLoadingNoteOwner,
    error: errorNoteOwner,
  } = useGetUserQuery({ url: note?.owner }, { skip: !note });

  const { isLoading: isLoadingIsFavorite, error: errorIsFavorite } = useGetIsFavoriteNoteQuery(
    { noteId, userId },
    { skip: !userId || !noteId }
  );

  const [addFavorite, { isLoading: isLoadingAdd }] = useAddFavoriteNoteMutation();
  const [removeFavorite, { isLoading: isLoadingRemove }] = useRemoveFavoriteNoteMutation();

  const addFavoriteHandler = () => {
    console.log('add');
    addFavorite({ noteId });
  };

  const removeFavoriteHandler = () => {
    console.log('remove');
    removeFavorite({ noteId, userId });
  };

  return (
    <section className="note-info">
      {isLoadingNote || isLoadingNoteOwner ? (
        <span>... </span>
      ) : (
        <>
          <BottomNavbar title={note.name} to={'/notes/' + noteId} />
          <div className="note-header flex justify-between items-center">
            <div className="flex flex-row items-center gap-2">
              <img
                className="user-profile-picture"
                src={noteOwner.profilePicture || '/profile-picture.jpeg'}
                alt={t('data.profilePicture')}
              />
              <div className="flex flex-col">
                <NavLink className="font-bold text-xl" to={noteOwner.self + '/noteboard'}>
                  {noteOwner.username}
                </NavLink>
                <span>
                  {t('data.views')}: {note.interactions}
                </span>
              </div>
            </div>
            <div className="flex">
              <RequireAuth>
                {isLoadingIsFavorite ? (
                  <span>...</span>
                ) : (
                  <FavoriteButton
                    isFavorite={!errorIsFavorite && userId}
                    addFavorite={addFavoriteHandler}
                    removeFavorite={removeFavoriteHandler}
                  />
                )}
              </RequireAuth>
              <DownloadButton link={note.file} />
              {userId === note.owner.id && <DeleteButton />}
            </div>
          </div>
          <div className="note-frame">
            {note.fileType === 'jpeg' ||
              note.fileType === 'jpg' ||
              (note.fileType === 'png' && (
                <div className="container-img-note">
                  <img src={note.file} alt={note.name} />
                </div>
              ))}

            {note.fileType === 'mp3' && (
              <audio controls className="w-100">
                <source src={note.file} type="audio/mp3" />
              </audio>
            )}

            {note.fileType === 'pdf' && <iframe className="w-full h-full" src={note.file}></iframe>}

            {note.fileType === 'mp4' && (
              <video controls className="w-100">
                <source src={note.file} type="video/mp4" />
              </video>
            )}
          </div>
          <ReviewsContainer score={note.avgScore.toFixed(1)} note={note} />
          {/* CHANGE TO REVIEW URL FROM NOTE */}
        </>
      )}
    </section>
  );
};

export default Note;
