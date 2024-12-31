// src/components/SubjectDirectoryCard.tsx

import React from 'react';
import { Box, Typography, Tooltip, CircularProgress } from '@mui/material';
import DescriptionIcon from '@mui/icons-material/Description';
import FolderIcon from '@mui/icons-material/Folder';
import { useTranslation } from 'react-i18next';
import { useSearchNotesQuery } from '../store/slices/searchApiSlice';
import { useGetDirectoryQuery } from '../store/slices/directoriesApiSlice';
import { Subject } from '../types';
import { useNavigate } from 'react-router-dom';

interface SubjectDirectoryCardProps {
  subject: Subject;
  userId: string;
}

const SubjectDirectoryCard: React.FC<SubjectDirectoryCardProps> = ({
  subject,
  userId,
}) => {
  const { t } = useTranslation('subjectDirectoryCard');
  const navigate = useNavigate();

  // Fetch the number of notes for this subject and user
  const { data: notesResult, isLoading: notesLoading } = useSearchNotesQuery(
    {
      subjectId: subject.id,
      userId,
      page: 1,
      pageSize: 1,
    },
    {
      skip: !subject.id || !userId,
    },
  );

  const notesCount = notesResult?.totalCount || 0;

  // Fetch the Directory data using the rootDirectoryUrl from the subject
  const {
    data: directory,
    isLoading: dirLoading,
    isError: dirError,
  } = useGetDirectoryQuery(
    { url: subject.rootDirectoryUrl },
    {
      skip: !subject.rootDirectoryUrl,
    },
  );

  // Handle navigation to the directory
  const handleNavigate = () => {
    if (directory?.id) {
      navigate(`/directories/${directory.id}?userId=${userId}`);
    }
  };

  // Determine the color for the FolderIcon
  const folderColor = directory?.iconColor
    ? `#${directory.iconColor}`
    : 'primary.main';

  return (
    <Box
      onClick={handleNavigate}
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
          }}
        />
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
              top: -10,
              right: -10,
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
      {/* Handle Directory Fetch Error */}
      {dirError && (
        <Typography variant="caption" color="error">
          {t('errorFetchingDirectory')}
        </Typography>
      )}
    </Box>
  );
};

export default SubjectDirectoryCard;
