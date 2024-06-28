import { useState } from 'react';
import { FilledHeartIcon, EmptyHeartIcon } from '../Utils/Icons';
const FavoriteButton = ({ addFavorite, removeFavorite, isFavorite, className }) => {
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
      {isFavorited ? <FilledHeartIcon className={className} /> : <EmptyHeartIcon className={className} />}
    </button>
  );
};

export default FavoriteButton;
