import { BottomNavbar, TabbedCard, CareerSubjectsByYear } from '../components';
import { useTranslation } from 'react-i18next';
import useCareerData from '../hooks/useCareerData';

const Career = () => {
  const { t } = useTranslation();
  const { career, isLoadingCareer } = useCareerData();
  return (
    <section className="max-container center gap-20">
      <BottomNavbar title={t('pages.myCareer.title')} to={'/career'} />
      {isLoadingCareer ? (
        <p>Loading...</p>
      ) : (
        <TabbedCard
          tabs={[1, 2, 3, 4, 5].map((year) => ({
            name: t(`ordinal.${year}`) + ' ' + t('data.year'),
            content: <CareerSubjectsByYear careerId={career.id} year={year} />,
          }))}
        />
      )}
    </section>
  );
};

export default Career;
