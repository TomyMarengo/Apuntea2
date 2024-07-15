import { useTranslation } from 'react-i18next';
import { useForm } from '../../hooks/index';
import { useDeleteNoteMutation } from '../../store/slices/notesApiSlice';
import { forwardRef } from 'react';

const DeleteNoteForm = forwardRef(function DeleteNoteForm({ note, parent }, submitRef) {
  const { t } = useTranslation();

  const [deleteNote, { isLoading: isLoadingDelete }] = useDeleteNoteMutation();

  const { handleSubmit } = useForm({
    submitCallback: deleteNote,
    args: {
      noteId: note.id,
    },
    redirectUrl: '/noteboard',
  });

  return (
    <form className="flex flex-col gap-5" onSubmit={handleSubmit} ref={submitRef}>
      <p>{t('pages.notes.deleteForm.confirm')}</p>
    </form>
  );
});

export default DeleteNoteForm;
