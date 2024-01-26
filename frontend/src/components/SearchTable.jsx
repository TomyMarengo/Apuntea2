import { useTranslation } from 'react-i18next';
import { NavLink } from 'react-router-dom';

import { FormattedDate } from './index';
import { useGetUserQuery } from '../store/slices/usersApiSlice';
import { useGetSubjectQuery } from '../store/slices/institutionsApiSlice';

const SearchTable = ({ notes }) => {
  const { t } = useTranslation();

  return (
    <table>
      <thead>
        <tr className="text-sm font-medium tracking-wider text-left text-gray-500 uppercase border-b border-gray-200">
          <th className="px-6 py-3">{t('data.name')}</th>
          <th className="px-6 py-3">{t('data.subject')}</th>
          <th className="px-6 py-3">{t('data.owner')}</th>
          <th className="px-6 py-3">{t('data.lastModifiedAt')}</th>
          <th className="px-6 py-3">{t('data.score')}</th>
        </tr>
      </thead>
      <tbody className="bg-white divide-y divide-gray-200">
        {notes?.map((note) => (
          <TableRow key={note.id} note={note} />
        ))}
      </tbody>
    </table>
  );
};

const TableRow = ({ note }) => {
  console.log(note);
  const { data: owner, isLoading: isLoadingOwner, error: isErrorOwner } = useGetUserQuery({ url: note.owner });
  const {
    data: subject,
    isLoading: isLoadingSubject,
    error: isErrorSubject,
  } = useGetSubjectQuery({ url: note.subject });

  return (
    <tr key={note.id} className="text-sm text-gray-500">
      <td className="px-6 py-3">
        <NavLink to={`/notes/${note.id}`} className="text-blue-500 hover:text-blue-600">
          {note.name}
        </NavLink>
      </td>
      <td className="px-6 py-3">{isLoadingSubject ? <SkeletonLoader /> : subject ? subject.name : 'Unknown'}</td>
      <td className="px-6 py-3">{isLoadingOwner ? <SkeletonLoader /> : owner ? owner.username : 'Unknown'}</td>
      <td className="px-6 py-3">
        <FormattedDate date={note.lastModifiedAt} />
      </td>
      <td className="px-6 py-3">{note.avgScore}</td>
    </tr>
  );
};

const SkeletonLoader = () => {
  return (
    <div className="animate-pulse bg-gray-200 h-4 w-24 rounded-md"></div> // Puedes ajustar el estilo según tu diseño
  );
};

export default SearchTable;
