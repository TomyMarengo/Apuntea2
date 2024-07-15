import { useTranslation } from 'react-i18next';
import {
  DeleteButton,
  FavoriteButton,
  DownloadButton,
  ReviewsContainer,
  RequireAuth,
  BottomNavbar,
  EditButton,
  Modal,
  EditNoteForm,
  DeleteNoteForm,
} from '../components/index';
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
import { useState, useRef } from 'react';

const Note = () => {
  const { t } = useTranslation();

  const userId = useSelector(selectCurrentUserId);
  const { noteId } = useParams();
  const { data: note, isLoading: isLoadingNote, error: errorNote } = useGetNoteQuery({ noteId });

  console.log('note', note);
  const {
    data: noteOwner,
    isLoading: isLoadingNoteOwner,
    error: errorNoteOwner,
  } = useGetUserQuery({ url: note?.owner }, { skip: !note });

  const { isLoading: isLoadingIsFavorite, error: errorIsFavorite } = useGetIsFavoriteNoteQuery(
    { noteId, userId },
    { skip: !userId || !noteId }
  );

  // Favorite
  const [addFavorite, { isLoading: isLoadingAdd }] = useAddFavoriteNoteMutation();
  const [removeFavorite, { isLoading: isLoadingRemove }] = useRemoveFavoriteNoteMutation();

  const addFavoriteHandler = () => {
    addFavorite({ noteId });
  };

  const removeFavoriteHandler = () => {
    removeFavorite({ noteId, userId });
  };

  // Modal
  const [editModalIsOpen, setEditModalIsOpen] = useState(false);

  const openEditModal = () => {
    setEditModalIsOpen(true);
  };

  const closeEditModal = () => {
    setEditModalIsOpen(false);
  };

  const editNoteFormRef = useRef(null);

  const handleEditNoteFormSubmit = () => {
    if (editNoteFormRef.current) {
      editNoteFormRef.current.requestSubmit();
    }
  };

  const [deleteModalIsOpen, setDeleteModalIsOpen] = useState(false);

  const openDeleteModal = () => {
    setDeleteModalIsOpen(true);
  };

  const closeDeleteModal = () => {
    setDeleteModalIsOpen(false);
  };

  const deleteNoteFormRef = useRef(null);

  const handleDeleteNoteFormSubmit = () => {
    if (deleteNoteFormRef.current) {
      deleteNoteFormRef.current.requestSubmit();
    }
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
                    className="icon-xs fill-dark-text"
                    isFavorite={!errorIsFavorite && userId}
                    addFavorite={addFavoriteHandler}
                    removeFavorite={removeFavoriteHandler}
                  />
                )}
              </RequireAuth>
              <DownloadButton
                link={note.file}
                fileName={note.name}
                fileType={note.fileType}
                className="icon-xs fill-dark-text"
              />

              {userId === noteOwner.id && (
                <>
                  <EditButton className="icon-xs fill-dark-text" onClick={openEditModal} />
                  <Modal
                    isOpen={editModalIsOpen}
                    onClose={closeEditModal}
                    title={t('actions.editNote')}
                    action={t('actions.update')}
                    onSubmit={handleEditNoteFormSubmit}
                  >
                    <EditNoteForm note={note} ref={editNoteFormRef} />
                  </Modal>
                  <DeleteButton className="icon-xs fill-dark-text" onClick={openDeleteModal} />
                  <Modal
                    isOpen={deleteModalIsOpen}
                    onClose={closeDeleteModal}
                    title={t('actions.deleteNote')}
                    action={t('actions.delete')}
                    onSubmit={handleDeleteNoteFormSubmit}
                  >
                    <DeleteNoteForm note={note} ref={deleteNoteFormRef} />
                  </Modal>
                </>
              )}
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
