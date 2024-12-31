// src/components/FavoriteDirectoryCard.tsx

import React, { useEffect, useState } from 'react';
import { Box, Tooltip, IconButton, Typography } from '@mui/material';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FolderIcon from '@mui/icons-material/Folder';
import { useTranslation } from 'react-i18next';
import {
  useRemoveFavoriteDirectoryMutation,
  useAddFavoriteDirectoryMutation,
  useGetIsFavoriteDirectoryQuery,
} from '../store/slices/directoriesApiSlice';
import { Directory } from '../types';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';

interface FavoriteDirectoryCardProps {
  directory: Directory;
  userId: string;
}

const FavoriteDirectoryCard: React.FC<FavoriteDirectoryCardProps> = ({
  directory,
  userId,
}) => {
  const { t } = useTranslation('favoriteDirectoryCard');
  const [removeFavoriteDirectory] = useRemoveFavoriteDirectoryMutation();
  const [addFavoriteDirectory] = useAddFavoriteDirectoryMutation();
  const { data: isFavApi, refetch } = useGetIsFavoriteDirectoryQuery(
    { directoryId: directory.id, userId },
    { skip: !userId || !directory.id },
  );

  const [isFavorite, setIsFavorite] = useState<boolean>(true);

  useEffect(() => {
    if (typeof isFavApi === 'boolean') {
      setIsFavorite(isFavApi);
    }
  }, [isFavApi]);

  const handleToggleFavorite = async () => {
    try {
      if (isFavorite) {
        const result = await removeFavoriteDirectory({
          directoryId: directory.id,
          userId,
        }).unwrap();
        if (result) {
          toast.success(t('unfavorited'));
        }
      } else {
        const result = await addFavoriteDirectory({
          directoryId: directory.id,
        }).unwrap();
        if (result) {
          toast.success(t('favorited'));
        }
      }
      refetch();
    } catch (error) {
      toast.error(t('errorUnfavorite'));
      console.error('Toggle favorite directory failed:', error);
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
      {/* Link for directory icon */}
      <Link
        to={`/directories/${directory.id}`}
        style={{ textDecoration: 'none', color: 'inherit' }}
      >
        <Box sx={{ mb: 0.5, position: 'relative' }}>
          <FolderIcon
            sx={{
              fontSize: 48,
              color: `#${directory.iconColor}` || 'primary.main',
            }}
          />
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
        {directory.name}
      </Typography>
    </Box>
  );
};

export default FavoriteDirectoryCard;
