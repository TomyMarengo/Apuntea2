import { useTranslation } from 'react-i18next';
import { NavLink } from 'react-router-dom';

const SearchTable = ({ notes }) => {
  const { t } = useTranslation();

  return (
    <table className="w-full border-collapse">
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
          <tr key={note.id} className="text-sm text-gray-500">
            <td className="px-6 py-3">
              <NavLink to={`/notes/${note.id}`} className="text-blue-500 hover:text-blue-600">
                {note.name}
              </NavLink>
            </td>
            <td className="px-6 py-3">{note.subject}</td>
            <td className="px-6 py-3">{note.owner}</td>
            <td className="px-6 py-3">{note.lastModifiedAt}</td>
            <td className="px-6 py-3">{note.avgScore}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default SearchTable;
