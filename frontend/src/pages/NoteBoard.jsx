import { BottomNavbar, TabbedCard, LatestNotes } from '../components';
import { useTranslation } from 'react-i18next';

const NoteBoard = () => {
  const { t } = useTranslation();

  return (
    <section className="max-container center gap-20">
      <BottomNavbar title={t('pages.myNoteBoard.title')} to={'/noteboard'} />
      <TabbedCard
        tabs={[
          {
            name: t('data.latestNotes'),
            content: <LatestNotes />,
          },
        ]}
      />
    </section>
  );
};

export default NoteBoard;
