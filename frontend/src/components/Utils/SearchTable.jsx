import { useTranslation } from 'react-i18next';
import { NavLink } from 'react-router-dom';

import { FormattedDate } from '../index';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';

const SearchTable = ({ params, notes, directories }) => {
  const { t } = useTranslation();

  const showNotes = params['category'] !== 'directory';
  const showDirectories = params['category'] === 'directory';

  return (
    <table>
      <thead>
        <tr className="text-left border-b border-text/20">
          <th className="px-6 py-3">{t('data.name')}</th>
          <th className="px-6 py-3">{t('data.subject')}</th>
          <th className="px-6 py-3">{t('data.owner')}</th>
          <th className="px-6 py-3">{t('data.lastModifiedAt')}</th>
          {!showDirectories && <th className="px-6 py-3">{t('data.score')}</th>}
        </tr>
      </thead>
      <tbody className="text-left bg-transparent">
        {showNotes && notes?.map((note) => <TableRow key={note.id} data={note} t={t} isNote={true} />)}
        {showDirectories &&
          directories?.map((directory) => <TableRow key={directory.id} data={directory} t={t} isNote={false} />)}
      </tbody>
    </table>
  );
};

const TableRow = ({ data, t, isNote }) => {
  const { data: owner, isLoading: isLoadingOwner } = useGetUserQuery({ url: data.owner });
  const { data: subject, isLoading: isLoadingSubject } = useGetSubjectQuery({ url: data.subject });

  return (
    <tr key={data.id} className="border-b border-text/20 hover:bg-dark-sec/20">
      <td className="px-6 py-3">
        {
          <NavLink className="link" to={isNote ? `/notes/${data.id}` : `/directories/${data.id}`}>
            {data.name}
          </NavLink>
        }
      </td>
      <td className="px-6 py-3">
        {isLoadingSubject ? <SkeletonLoader t={t} /> : subject ? subject.name : t('data.unknown')}
      </td>
      <td className="px-6 py-3">
        {isLoadingOwner ? <SkeletonLoader t={t} /> : owner?.username ? owner.username : t('data.unknown')}
      </td>

      <td className="px-6 py-3">
        <FormattedDate date={data.lastModifiedAt} />
      </td>
      {isNote && <td className="px-6 py-3">{data.avgScore}</td>}
    </tr>
  );
};

const SkeletonLoader = ({ t }) => {
  return (
    <div className="skeleton-text h-4 w-24">{t('actions.loading')}</div> // Puedes ajustar el estilo según tu diseño
  );
};

export default SearchTable;
