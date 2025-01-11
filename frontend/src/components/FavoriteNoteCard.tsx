// src/components/FavoriteNoteCard.tsx
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { Box, Tooltip, IconButton, Typography } from '@mui/material';
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink } from 'react-router-dom';
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
    if (typeof isFavApi?.success === 'boolean') {
      setIsFavorite(isFavApi.success);
    }
  }, [isFavApi]);

  const handleToggleFavorite = async (
    event: React.MouseEvent<HTMLButtonElement>,
  ) => {
    event.preventDefault();
    event.stopPropagation();
    try {
      if (isFavorite) {
        // call remove
        const result = await removeFavoriteNote({
          noteId: note.id,
          userId,
        }).unwrap();
        if (result.success) {
          toast.success(t('unfavorited'));
        }
      } else {
        // call add
        const result = await addFavoriteNote({ noteId: note.id }).unwrap();
        if (result.success) {
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
      component={RouterLink}
      to={`/notes/${note.id}`}
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
      <Box sx={{ position: 'relative', display: 'inline-flex' }}>
        <NoteFileIcon fileType={note.fileType} size={48} />

        {/* Heart icon top-right */}
        <Tooltip title={isFavorite ? t('removeFavorite')! : t('addFavorite')!}>
          <IconButton
            onClick={handleToggleFavorite}
            size="small"
            sx={{
              position: 'absolute',
              top: -10,
              right: -30,
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
      </Box>

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
