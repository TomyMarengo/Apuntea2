import { useTranslation } from 'react-i18next';
import { Input, Select } from '../index';
import { useForm } from '../../hooks/index';
import { noteInputs } from '../../constants/forms';
import { PatchNoteSchema } from '../../constants/schemas';
import { useUpdateNoteMutation } from '../../store/slices/notesApiSlice';
import { forwardRef } from 'react';

const EditNoteForm = forwardRef(function EditNoteForm({ note }, submitRef) {
  const { t } = useTranslation();

  const categories = [
    { label: t('category.theory'), value: 'THEORY' },
    { label: t('category.practice'), value: 'PRACTICE' },
    { label: t('category.exam'), value: 'EXAM' },
    { label: t('category.other'), value: 'OTHER' },
  ];

  const visibility = [
    { label: t('data.public'), value: 'true' },
    { label: t('data.private'), value: 'false' },
  ];

  const [updateNote, { isLoading: isLoadingUpdate }] = useUpdateNoteMutation();

  const { handleChange, handleSubmit, errors } = useForm({
    submitCallback: updateNote,
    args: {
      noteId: note.id,
    },
    schema: PatchNoteSchema,
  });
  return (
    <form className="flex flex-col gap-5" onSubmit={handleSubmit} ref={submitRef}>
      <Input
        {...noteInputs.find((input) => input.name === 'name')}
        defaultValue={note.name}
        onChange={handleChange}
        errors={errors?.name}
      />
      <Select
        {...noteInputs.find((input) => input.name === 'category')}
        options={categories}
        defaultValue={note.category}
        onChange={handleChange}
        errors={errors?.category}
      />
      <Select
        {...noteInputs.find((input) => input.name === 'visible')}
        options={visibility}
        defaultValue={note.visible ? 'true' : 'false'}
        onChange={handleChange}
        errors={errors?.visible}
      />
    </form>
  );
});

export default EditNoteForm;
