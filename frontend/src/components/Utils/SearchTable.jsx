import { useTranslation } from 'react-i18next';
import { NavLink } from 'react-router-dom';

import { FormattedDate } from '../index';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';

const SearchTable = ({ notes }) => {
  const { t } = useTranslation();

  return (
    <table>
      <thead>
        <tr className="text-left border-b border-text/20">
          <th className="px-6 py-3">{t('data.name')}</th>
          <th className="px-6 py-3">{t('data.subject')}</th>
          <th className="px-6 py-3">{t('data.owner')}</th>
          <th className="px-6 py-3">{t('data.lastModifiedAt')}</th>
          <th className="px-6 py-3">{t('data.score')}</th>
        </tr>
      </thead>
      <tbody className="text-left bg-transparent">
        {notes?.map((note) => (
          <TableRow key={note.id} note={note} t={t} />
        ))}
      </tbody>
    </table>
  );
};

const TableRow = ({ note, t }) => {
  const { data: owner, isLoading: isLoadingOwner, error: isErrorOwner } = useGetUserQuery({ url: note.owner });
  const {
    data: subject,
    isLoading: isLoadingSubject,
    error: isErrorSubject,
  } = useGetSubjectQuery({ url: note.subject });

  return (
    <tr key={note.id} className="border-b border-text/20 hover:bg-dark-sec/20">
      <td className="px-6 py-3">
        <NavLink className="link" to={`/notes/${note.id}`}>
          {note.name}
        </NavLink>
      </td>
      <td className="px-6 py-3">
        {isLoadingSubject ? <SkeletonLoader t={t} /> : subject ? subject.name : t('data.unknown')}
      </td>
      <td className="px-6 py-3">
        {isLoadingOwner ? <SkeletonLoader t={t} /> : owner?.username ? owner.username : t('data.unknown')}
      </td>

      <td className="px-6 py-3">
        <FormattedDate date={note.lastModifiedAt} />
      </td>
      <td className="px-6 py-3">{note.avgScore}</td>
    </tr>
  );
};

const SkeletonLoader = ({ t }) => {
  return (
    <div className="skeleton-text h-4 w-24">{t('actions.loading')}</div> // Puedes ajustar el estilo según tu diseño
  );
};

export default SearchTable;
