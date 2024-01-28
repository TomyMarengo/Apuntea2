import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import { useLazySearchNotesQuery } from '../../store/slices/searchApiSlice';
import { Button, Input, InstitutionDataInputs } from '../index';
import { serializeFormQuery } from '../../functions/utils';
import { searchInputs } from '../../constants/forms';
import { useForm } from '../../hooks/index';
import { useEffect } from 'react';
import { SearchSchema } from '../../constants/schemas';

const SearchForm = ({ params, institution, career, subject }) => {
  const { t } = useTranslation();
  const [searchNotes] = useLazySearchNotesQuery();
  const navigate = useNavigate();

  const { form, handleChange } = useForm({
    initialValues: {
      institutionId: params['institutionId'] || institution?.id || '',
      careerId: params['careerId'] || career?.id || '',
      subjectId: params['subject'] || '',
      word: params['word'] || '',
      asc: params['asc'] || false,
      sortBy: params['sortBy'] || 'modified',
      page: params['page'],
      pageSize: params['pageSize'],
    },
    submitCallback: searchNotes,
    schema: SearchSchema,
  });

  /* Detect changes in params from NavSearchButton and update form */
  useEffect(() => {
    if (params['word']) {
      handleChange({ target: { name: 'word', value: params['word'] } });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [params['word']]);

  const handleSearch = (event) => {
    event.preventDefault();
    let params = serializeFormQuery(form);
    navigate(`/search?${params}`);
  };

  return (
    <form onSubmit={handleSearch} className="flex flex-col items-center gap-6">
      <div className="grid grid-flow-col lg:gap-4 lg:grid-cols-4 lg:grid-rows-1 gap-y-6 gap-x-8 grid-cols-2 grid-rows-2">
        <InstitutionDataInputs
          initialInstitution={institution}
          initialCareer={career}
          initialSubject={subject}
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
