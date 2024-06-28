import { BottomNavbar, TabbedCard } from '../components';
import { useTranslation } from 'react-i18next';
import { selectCurrentUser } from '../store/slices/authSlice';
import { useSelector } from 'react-redux';
import { useGetLatestNotesQuery } from '../store/slices/notesApiSlice';
const NoteBoard = () => {
  const user = useSelector(selectCurrentUser);
  const { data: dataLatestNotes, isLoading: isLoadingLatestNotes } = useGetLatestNotesQuery({ userId: user.id });

  const { t } = useTranslation();
  return (
    <section className="max-container center gap-20">
      <BottomNavbar title={t('pages.myNoteBoard.title')} to={'/noteboard'} />
      {isLoadingLatestNotes ? (
        <p>Loading...</p>
      ) : (
        <TabbedCard tabs={[{ name: t('data.latestNotes'), content: dataLatestNotes }]} />
      )}
    </section>
  );
};
export default NoteBoard;
