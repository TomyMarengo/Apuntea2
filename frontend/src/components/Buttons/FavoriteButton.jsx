import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { useAddFavoriteMutation, useRemoveFavoriteMutation } from '../../store/slices/notesApiSlice';
import { FilledHeartIcon, EmptyHeartIcon } from '../Utils/Icons';
import { selectCurrentUserId } from '../../store/slices/authSlice';

const FavoriteButton = ({ noteId, isFavorite }) => {
  const userId = useSelector(selectCurrentUserId);

  const [addFavorite, { isLoading: isLoadingAdd }] = useAddFavoriteMutation();
  const [removeFavorite, { isLoading: isLoadingRemove }] = useRemoveFavoriteMutation();

  const [isFavorited, setIsFavorited] = useState(isFavorite);

  useEffect(() => {
    setIsFavorited(isFavorite);
  }, [isFavorite]);
  // ??????

  const handleFavorite = () => {
    if (isFavorited) {
      removeFavorite({ noteId, userId });
    } else {
      addFavorite({ noteId });
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
