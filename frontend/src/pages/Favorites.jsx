import { BottomNavbar } from '../components';
import { useTranslation } from 'react-i18next';
import { selectCurrentUser } from '../store/slices/authSlice';
import { useSelector } from 'react-redux';

const Favorites = () => {
  const user = useSelector(selectCurrentUser);

  const { t } = useTranslation();
  return (
    <section className="max-container center gap-20">
      <BottomNavbar title={t('data.favorites')} to={'/favorites'} />
    </section>
  );
};
export default Favorites;
