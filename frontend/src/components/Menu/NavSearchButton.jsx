import { NavLink } from 'react-router-dom';
import { SearchIcon } from '../Utils/Icons';
import { useTranslation } from 'react-i18next';

const NavSearchButton = () => {
  const { t } = useTranslation();

  //TODO: Terminar cuando este hecho el search

  return (
    <div className="search-container">
      <NavLink to="/search" className="icon-button search-icon">
        <SearchIcon className="icon-s fill-dark-pri" />
      </NavLink>
      <input type="text" className="search-input" placeholder={t('placeholders.word')} />
    </div>
  );
};

export default NavSearchButton;
