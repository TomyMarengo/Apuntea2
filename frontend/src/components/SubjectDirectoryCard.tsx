// src/components/SubjectDirectoryCard.tsx

import DescriptionIcon from '@mui/icons-material/Description';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FolderIcon from '@mui/icons-material/Folder';
import {
  Box,
  Typography,
  Tooltip,
  CircularProgress,
  IconButton,
} from '@mui/material';
import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { Link as RouterLink } from 'react-router-dom';
import { toast } from 'react-toastify';

import { selectCurrentUser } from '../store/slices/authSlice';
import {
  useGetDirectoryQuery,
  useRemoveFavoriteDirectoryMutation,
  useAddFavoriteDirectoryMutation,
  useGetIsFavoriteDirectoryQuery,
} from '../store/slices/directoriesApiSlice';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';
import { SubjectWithCareer } from '../types';

interface SubjectDirectoryCardProps {
  subject: SubjectWithCareer;
  userId?: string; // Optional userId to show notes count
}

const SubjectDirectoryCard: React.FC<SubjectDirectoryCardProps> = ({
  subject,
  userId,
}) => {
  const { t } = useTranslation('subjectDirectoryCard');
  const loggedUser = useSelector(selectCurrentUser);

  // Fetch the number of notes for this subject and user
  const { data: notesResult, isLoading: notesLoading } = useSearchNotesQuery(
    {
      subjectId: subject.subjectId,
      userId,
      page: 1,
      pageSize: 1,
    },
    {
      skip: !subject.subjectId, //|| !userId,
    },
  );

  const notesCount = notesResult?.totalCount || 0;

  // Fetch the Directory data using the rootDirectoryUrl from the subject
  const { data: directory, isLoading: dirLoading } = useGetDirectoryQuery(
    { url: subject.rootDirectoryUrl },
    {
      skip: !subject.rootDirectoryUrl,
    },
  );

  const [removeFavoriteDirectory] = useRemoveFavoriteDirectoryMutation();
  const [addFavoriteDirectory] = useAddFavoriteDirectoryMutation();
  const {
    data: isFavApi,
    refetch,
    isLoading: isFavLoading,
  } = useGetIsFavoriteDirectoryQuery(
    { directoryId: directory?.id, userId: loggedUser?.id },
    { skip: dirLoading || !loggedUser || !directory?.id },
  );

  const [isFavorite, setIsFavorite] = useState<boolean>(true);

  useEffect(() => {
    setIsFavorite(isFavApi?.success || false);
  }, [isFavApi]);

  const handleToggleFavorite = async (
    event: React.MouseEvent<HTMLButtonElement>,
  ) => {
    event.preventDefault();
    event.stopPropagation();

    try {
      if (isFavorite) {
        const result = await removeFavoriteDirectory({
          directoryId: directory?.id,
          userId: loggedUser?.id,
        }).unwrap();
        if (result) {
          toast.success(t('unfavorited'));
        } else {
          toast.error(t('errorUnfavorite'));
        }
      } else {
        const result = await addFavoriteDirectory({
          directoryId: directory?.id,
        }).unwrap();
        if (result) {
          toast.success(t('favorited'));
        } else {
          toast.error(t('errorFavorite'));
        }
      }
      refetch();
    } catch (error) {
      toast.error(t('errorUnfavorite'));
      console.error('Toggle favorite directory failed:', error);
    }
  };

  // Determine the color for the FolderIcon
  const folderColor = 'primary.dark';

  return (
    <Box
      component={RouterLink}
      to={
        directory
          ? `/directories/${directory.id}${userId ? `?userId=${userId}` : ''}`
          : '/notes'
      }
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
      {/* Subject Icon with Notes Count */}
      <Box sx={{ position: 'relative', display: 'inline-flex' }}>
        <FolderIcon
          sx={{
            fontSize: 48,
            color: folderColor,
            opacity: notesCount > 0 || (!userId && !dirLoading) ? 1 : 0.4,
          }}
        />

        {/* Heart icon top-right */}
        {loggedUser && (
          <Tooltip
            title={isFavorite ? t('removeFavorite')! : t('addFavorite')!}
          >
            {isFavLoading ? (
              <CircularProgress
                size={16}
                sx={{
                  position: 'absolute',
                  top: -2,
                  right: -22,
                  zIndex: 999,
                }}
              />
            ) : (
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
                  <FavoriteBorderIcon
                    sx={{ color: 'error.main', fontSize: 22 }}
                  />
                )}
              </IconButton>
            )}
          </Tooltip>
        )}

        {/* Notes Count */}
        {
          <Tooltip
            placement="right"
            title={
              notesLoading || dirLoading
                ? ''
                : t('notesCount', { count: notesCount })
            }
          >
            <Box
              sx={{
                position: 'absolute',
                top: -8,
                left: -20,
                backgroundColor: 'none',
                width: 24,
                height: 24,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              {notesLoading || dirLoading ? (
                <CircularProgress size={12} />
              ) : (
                <>
                  <Typography variant="subtitle2">{notesCount}</Typography>
                  <DescriptionIcon fontSize="inherit" sx={{ ml: 0.3 }} />
                </>
              )}
            </Box>
          </Tooltip>
        }
      </Box>

      {/* Subject Name and Year */}
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
        {subject.name}
      </Typography>
    </Box>
  );
};

export default SubjectDirectoryCard;
