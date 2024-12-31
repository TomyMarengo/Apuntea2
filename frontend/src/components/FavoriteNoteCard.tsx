// src/components/FavoriteNoteCard.tsx

import React, { useEffect, useState } from 'react';
import { Box, Tooltip, IconButton, Typography } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';
import DescriptionIcon from '@mui/icons-material/Description';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import ImageIcon from '@mui/icons-material/Image';
import MovieIcon from '@mui/icons-material/Movie';
import MusicNoteIcon from '@mui/icons-material/MusicNote';
import { useTranslation } from 'react-i18next';
import {
  useRemoveFavoriteNoteMutation,
  useAddFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
} from '../store/slices/notesApiSlice';
import { Note, FileType } from '../types';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';

function getFileIcon(fileType?: FileType) {
  if (!fileType)
    return <InsertDriveFileIcon sx={{ fontSize: 48 }} color="primary" />;

  switch (fileType.toLowerCase()) {
    case 'pdf':
      return <PictureAsPdfIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'doc':
    case 'docx':
      return <DescriptionIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'ppt':
    case 'pptx':
      return <DescriptionIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'xls':
    case 'xlsx':
      return <DescriptionIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'jpg':
    case 'jpeg':
    case 'png':
    case 'gif':
      return <ImageIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'mp4':
      return <MovieIcon sx={{ fontSize: 48 }} color="primary" />;
    case 'mp3':
      return <MusicNoteIcon sx={{ fontSize: 48 }} color="primary" />;
    default:
      return <InsertDriveFileIcon sx={{ fontSize: 48 }} color="primary" />;
  }
}

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
          {getFileIcon(note.fileType)}
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
