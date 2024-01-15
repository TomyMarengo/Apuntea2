import { NavLink } from 'react-router-dom';
import { SearchIcon } from '../components/Icons';
import { useTranslation } from 'react-i18next';

const NavSearchButton = () => {
  const { t } = useTranslation();

  //TODO: Terminar cuando este hecho el search

  return (
    <div class="search-container">
      <NavLink to="/search" className="icon-button search-icon">
        <SearchIcon className="icon fill-dark-pri" />
      </NavLink>
      <input type="text" className="search-input" placeholder={t('search.word.placeholder')} />
    </div>
  );
};

export default NavSearchButton;
