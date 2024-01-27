import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { SearchIcon } from '../Utils/Icons';

const NavSearchButton = () => {
  const { t } = useTranslation();
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  const handleKeyDown = (event) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      navigateToSearch();
    }
  };

  const navigateToSearch = () => {
    if (searchTerm.trim() !== '') {
      navigate(`/search?word=${encodeURIComponent(searchTerm.trim())}`);
    } else {
      navigate('/search');
    }
  };

  return (
    <div className="search-container">
      <div className="icon-button search-icon" onClick={navigateToSearch}>
        <SearchIcon className="icon-s fill-dark-pri" />
      </div>
      <input
        type="text"
        className="search-input"
        placeholder={t('placeholders.word')}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        onKeyDown={handleKeyDown}
      />
    </div>
  );
};

export default NavSearchButton;
