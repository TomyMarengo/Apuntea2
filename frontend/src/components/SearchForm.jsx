import { useTranslation } from 'react-i18next';

import { Button, Input } from './index';
import { useForm } from '../hooks/index';
import { useLazySearchNotesQuery } from '../store/slices/searchApiSlice';

const SearchForm = ({ params }) => {
  const { t } = useTranslation();
  const [searchNotes] = useLazySearchNotesQuery();

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      institutionId: params['institutionId'] || '',
      careerId: params['careerId'] || '',
      subjectId: params['subject'] || '',
      word: params['word'] || '',
      page: params['page'] || 1,
      asc: params['asc'] || false,
      sortBy: params['sortBy'] || 'modified',
      category: params['category'] || 'note',
      pageSize: params['pageSize'] || 12,
    },
    submitCallback: searchNotes,
  });

  return (
    <form className="flex flex-col gap-4">
      <div className="flex gap-4">
        <Input type="text" placeholder={t('data.institution')} />
        <Input type="text" placeholder={t('data.career')} />
        <Input type="text" placeholder={t('data.subject')} />
        <Input type="text" placeholder={t('placeholders.word')} />
      </div>
      <Button>{t('actions.search')}</Button>
    </form>
  );
};

export default SearchForm;
