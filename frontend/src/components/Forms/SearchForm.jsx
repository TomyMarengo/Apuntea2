import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import { useLazySearchNotesQuery } from '../../store/slices/searchApiSlice';
import { Button, Input, InstitutionDataInputs } from '../index';
import { serializeFormQuery } from '../../functions/utils';
import { searchInputs } from '../../constants/forms';
import { useForm } from '../../hooks/index';

const SearchForm = ({ params, user, institution, career }) => {
  const { t } = useTranslation();
  const [searchNotes] = useLazySearchNotesQuery();
  const navigate = useNavigate();

  const { form, handleChange, handleSubmit } = useForm({
    initialValues: {
      institutionId: params['institutionId'] || institution?.id || '',
      careerId: params['careerId'] || career?.id || '',
      /* subjectId: params['subject'] || '', */
      word: params['word'] || '',
      asc: params['asc'] || false,
      sortBy: params['sortBy'] || 'modified',
      page: params['page'],
      pageSize: params['pageSize'],
    },
    submitCallback: searchNotes,
  });

  const handleSearch = (event) => {
    event.preventDefault();
    let params = serializeFormQuery(form);
    navigate(`/search?${params}`);

    handleSubmit(event);
  };

  return (
    <form onSubmit={handleSearch} className="flex flex-col gap-4">
      <div className="flex gap-4">
        {/* TODO: Quitar noSubject y skipSubjects */}
        <InstitutionDataInputs
          initialInstitutionId={form.institutionId}
          initialCareerId={form.careerId}
          onChange={handleChange}
        />
        <Input
          {...searchInputs.find((input) => input.name === 'word')}
          value={form.word}
          placeholder={t('placeholders.word')}
          onChange={handleChange}
        />
      </div>
      <Button type="submit">{t('actions.search')}</Button>
    </form>
  );
};

export default SearchForm;
