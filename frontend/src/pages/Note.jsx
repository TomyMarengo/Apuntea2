import { useTranslation } from 'react-i18next';
import { DeleteButton, FavoriteButton, DownloadButton, ReviewsContainer } from '../components/index';
import { NavLink } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { useGetNoteQuery, useGetIsFavoriteQuery } from '../store/slices/notesApiSlice';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUserId } from '../store/slices/authSlice';

const Note = () => {
  const { t } = useTranslation();

  const userId = useSelector(selectCurrentUserId);
  const { noteId } = useParams();
  const { data: note, isLoading: isLoadingNote, error: errorNote } = useGetNoteQuery({ noteId });

  const {
    data: noteOwner,
    isLoading: isLoadingNoteOwner,
    error: errorNoteOwner,
  } = useGetUserQuery({ url: note?.owner }, { skip: !note, refetchOnMountOrArgChange: true });

  const {
    data: isFavorite,
    isLoading: isLoadingIsFavorite,
    error: errorIsFavorite,
  } = useGetIsFavoriteQuery({ noteId, userId }, { skip: !userId || !noteId, refetchOnMountOrArgChange: true });

  return (
    <section className="note-info">
      {isLoadingNote || isLoadingNoteOwner ? (
        <span>... </span>
      ) : (
        <>
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
              {isLoadingIsFavorite ? (
                <span>...</span>
              ) : (
                <FavoriteButton noteId={noteId} isFavorite={!errorIsFavorite} />
              )}
              <DownloadButton link={note.file} />
              {userId === note.owner.id && <DeleteButton />}
            </div>
          </div>
          <div className="note-frame">
            <iframe src={note.file}></iframe>
          </div>
          <ReviewsContainer score={note.avgScore.toFixed(1)} note={note} />
          {/* CHANGE TO REVIEW URL FROM NOTE */}
        </>
      )}
    </section>
  );
};

export default Note;
