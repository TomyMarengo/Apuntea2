import { useEffect, useState } from 'react';
import { FilledHeartIcon, EmptyHeartIcon } from './Icons';

const FavoriteButton = () => {
  const [favorite, setFavorite] = useState(false);

  useEffect(() => {
    if (favorite) {
      // TODO: add favorite
    } else {
      // TODO: remove favorite
    }
  }, [favorite]);

  const toggleTheme = () => {
    setFavorite((prevState) => (prevState === true ? false : true));
  };

  return (
    <button className="icon-button" onClick={toggleTheme}>
      {favorite ? <FilledHeartIcon className="icon-s" /> : <EmptyHeartIcon className="icon-s" />}
    </button>
  );
};

export default FavoriteButton;
