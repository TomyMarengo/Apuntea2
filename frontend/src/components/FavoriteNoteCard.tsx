// src/components/FavoriteNoteCard.tsx

import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { Box, Tooltip, IconButton, Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';

import {
  useRemoveFavoriteNoteMutation,
  useAddFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
} from '../store/slices/notesApiSlice';
import { Note } from '../types';
import NoteFileIcon from './NoteFileIcon';

interface FavoriteNoteCardProps {
  note: Note;
  userId: string;
}

const FavoriteNoteCard: React.FC<FavoriteNoteCardProps> = ({
  note,
  userId,
}) => {
  const { t } = useTranslation('favoriteNoteCard');
  const [removeFavoriteNote] = useRemoveFavoriteNoteMutation();
  const [addFavoriteNote] = useAddFavoriteNoteMutation();
  const { data: isFavApi, refetch } = useGetIsFavoriteNoteQuery(
    { noteId: note.id, userId },
    { skip: !userId || !note.id },
  );

  // Local isFavorite state
  const [isFavorite, setIsFavorite] = useState<boolean>(true);

  useEffect(() => {
    if (typeof isFavApi === 'boolean') {
      setIsFavorite(isFavApi);
    }
  }, [isFavApi]);

  // Toggle function
  const handleToggleFavorite = async () => {
    try {
      if (isFavorite) {
        // call remove
        const result = await removeFavoriteNote({
          noteId: note.id,
          userId,
        }).unwrap();
        if (result) {
          toast.success(t('unfavorited'));
        }
      } else {
        // call add
        const result = await addFavoriteNote({ noteId: note.id }).unwrap();
        if (result) {
          toast.success(t('favorited'));
        }
      }
      // refetch to confirm
      refetch();
    } catch (error) {
      toast.error(t('errorUnfavorite'));
      console.error('Toggle favorite note failed:', error);
    }
  };

  return (
    <Box
      sx={{
        backgroundColor: 'transparent',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        position: 'relative',
        transition: 'transform 0.2s',
        cursor: 'pointer',
        '&:hover': {
          transform: 'scale(1.05)',
        },
      }}
    >
      {/* Link area for note icon */}
      <Link
        to={`/notes/${note.id}`}
        style={{ textDecoration: 'none', color: 'inherit' }}
      >
        <Box sx={{ mb: 0.5, position: 'relative' }}>
          <NoteFileIcon fileType={note.fileType} size={48} />
        </Box>
      </Link>

      {/* Heart icon top-right */}
      <Tooltip title={isFavorite ? t('removeFavorite')! : t('favorited')!}>
        <IconButton
          onClick={handleToggleFavorite}
          size="small"
          sx={{
            position: 'absolute',
            top: -6,
            right: -6,
            zIndex: 999,
          }}
        >
          {isFavorite ? (
            <FavoriteIcon sx={{ color: 'error.main', fontSize: 22 }} />
          ) : (
            <FavoriteBorderIcon sx={{ color: 'error.main', fontSize: 22 }} />
          )}
        </IconButton>
      </Tooltip>

      {/* Name ellipsized */}
      <Typography
        variant="body2"
        sx={{
          width: '100%',
          textAlign: 'center',
          whiteSpace: 'nowrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
          mt: 0.5,
        }}
      >
        {note.name}
      </Typography>
    </Box>
  );
};

export default FavoriteNoteCard;
