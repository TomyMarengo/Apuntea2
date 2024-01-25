import { useState } from 'react';
import { FilledHeartIcon, EmptyHeartIcon } from '../Utils/Icons';

const FavoriteButton = ({ addFavorite, removeFavorite, isFavorite }) => {
  const [isFavorited, setIsFavorited] = useState(isFavorite);

  const handleFavorite = () => {
    if (isFavorited) {
      removeFavorite();
    } else {
      addFavorite();
    }
    setIsFavorited(!isFavorited);
  };

  return (
    <button className="icon-button" onClick={handleFavorite}>
      {isFavorited ? <FilledHeartIcon className="icon-s" /> : <EmptyHeartIcon className="icon-s" />}
    </button>
  );
};

export default FavoriteButton;
