import { useTranslation } from 'react-i18next';
import { DeleteButton, FavoriteButton, DownloadButton, ReviewsContainer } from '../components/index';
import { NavLink } from 'react-router-dom';
import { useParams } from 'react-router-dom';
import { useGetNoteQuery } from '../store/slices/notesApiSlice';
import { useGetUserQuery, useGetUserPictureQuery } from '../store/slices/usersApiSlice';

const Note = () => {
  const { t } = useTranslation();

  const { noteId } = useParams();
  const { data: note, isLoading: isLoadingNote, error: errorNote } = useGetNoteQuery({ noteId });
  const {
    data: owner,
    isLoading: isLoadingOwner,
    error: errorOwner,
  } = useGetUserQuery({ url: note?.owner }, { skip: !note, refetchOnMountOrArgChange: true });

  return (
    <section className="note-info">
      {isLoadingNote || isLoadingOwner ? (
        <span>... </span>
      ) : (
        <>
          <div className="note-header flex justify-between items-center">
            <div className="flex flex-row items-center gap-2">
              <img
                className="user-profile-picture"
                src={owner.profilePicture || '/profile-picture.jpeg'}
                alt={t('data.profilePicture')}
              />
              <div className="flex flex-col">
                <NavLink className="font-bold text-xl" to={owner.self + '/noteboard'}>
                  {owner.username}
                </NavLink>
                <span>
                  {t('data.views')}: {note.interactions}
                </span>
              </div>
            </div>
            <div className="flex">
              <FavoriteButton />
              <DownloadButton link={note.file} />
              <DeleteButton />
            </div>
          </div>
          <div className="note-frame">
            <iframe src={note.file}></iframe>
          </div>
          <ReviewsContainer score={note.avgScore.toFixed(1)} noteId={note.id} />
          {/* CHANGE TO REVIEW URL FROM NOTE */}
        </>
      )}
    </section>
  );
};

export default Note;
