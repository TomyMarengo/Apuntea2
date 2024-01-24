import { useEffect, useState } from 'react';
import { FilledHeartIcon, EmptyHeartIcon } from './Icons';
import { useAddFavoriteMutation, useRemoveFavoriteMutation } from '../store/slices/notesApiSlice';
import { useGetIsFavoriteQuery } from '../store/slices/notesApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUserId } from '../store/slices/authSlice';

const FavoriteButton = ({ noteId }) => {
  const userId = useSelector(selectCurrentUserId);
  console.log('userid', userId);
  const {
    isFavorite,
    isLoading: isLoadingIsFavorite,
    error: errorIsFavorite,
  } = useGetIsFavoriteQuery({ noteId, userId }, { skip: !userId || !noteId, refetchOnMountOrArgChange: true });

  console.log('errorisfavorite', errorIsFavorite);
  const [addFavorite, { isLoading: isLoadingAdd }] = useAddFavoriteMutation();
  const [removeFavorite, { isLoading: isLoadingRemove }] = useRemoveFavoriteMutation();

  const [isFavorited, setIsFavorited] = useState(isFavorite === undefined ? false : true);

  useEffect(() => {
    setIsFavorited(isFavorite);
  }, [isFavorite]);

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
