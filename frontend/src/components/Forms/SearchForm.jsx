import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';

import { Button, Input, InstitutionDataInputs, SortBySelect, SearchPill } from '../index';
import { serializeFormQuery } from '../../functions/utils';
import { searchInputs } from '../../constants/forms';
import { useForm } from '../../hooks/index';
import { useEffect } from 'react';
import { SearchSchemaPartial } from '../../constants/schemas';

const SearchForm = ({ params, institution, career, subject }) => {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const { form, handleChange } = useForm({
    initialValues: {
      institutionId: params['institutionId'],
      careerId: params['careerId'],
      subjectId: params['subject'],
      word: params['word'],
      asc: params['asc'],
      sortBy: params['sortBy'],
      page: params['page'],
      pageSize: params['pageSize'],
      category: params['category'],
    },
    schema: SearchSchemaPartial,
  });

  /* Detect changes in params from NavSearchButton and update form */
  useEffect(() => {
    if (params['word']) {
      handleChange({ target: { name: 'word', value: params['word'] } });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [params['word']]);

  useEffect(() => {
    if (params['category'] === 'directory' && params['sortBy'] === 'score') {
      handleChange({ target: { name: 'sortBy', value: 'modified' } });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [params['category'], params['sortBy']]);

  const handleSearch = (event) => {
    event.preventDefault();
    console.log(form);
    const query = serializeFormQuery(form);
    navigate(`/search?${query}`);
  };

  useEffect(() => {
    handleSearch({ preventDefault: () => {} });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [form.category, form.sortBy, form.asc]);

  return (
    <form onSubmit={handleSearch} className="flex flex-col gap-6">
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
      <Button className="self-center" type="submit">
        {t('actions.search')}
      </Button>
      <div className="flex flex-wrap gap-4">
        <SearchPill category={form.category} setCategory={handleChange} />
        <SortBySelect sortBy={form.sortBy} asc={form.asc} setSortBy={handleChange} setAsc={handleChange} />
      </div>
    </form>
  );
};

export default SearchForm;
