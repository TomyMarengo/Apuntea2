// src/components/Row/RowNote.tsx

import {
  IconButton,
  Tooltip,
  Menu,
  MenuItem,
  Skeleton,
  Link as MuiLink,
  TableRow,
  TableCell,
  Box,
} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useState, MouseEvent } from 'react';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import {
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
} from '../../store/slices/notesApiSlice';
import { saveAs } from 'file-saver';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { Note, Subject } from '../../types';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import DownloadIcon from '@mui/icons-material/Download';
import LinkIcon from '@mui/icons-material/Link';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { Column } from '../ResultsTable';

// Define and export ColumnNote
export const ColumnNote: Column[] = [
  { id: 'name', label: 'Name' },
  { id: 'subject', label: 'Subject' },
  { id: 'owner', label: 'Owner' },
  { id: 'lastModifiedAt', label: 'Last Modified' },
  { id: 'score', label: 'Score' },
  { id: 'actions', label: 'Actions', align: 'right' },
];

interface RowNoteProps {
  note: Note;
}

const RowNote: React.FC<RowNoteProps> = ({ note }) => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

  // Fetch subject data
  const {
    data: subjectData,
    isLoading: subjectLoading,
    isError: subjectError,
  } = useGetSubjectQuery({ url: note.subjectUrl });

  // Fetch owner data
  const {
    data: ownerData,
    isLoading: ownerLoading,
    isError: ownerError,
  } = useGetUserQuery({ url: note.ownerUrl });

  // Favorite queries and mutations
  const {
    data: isFavorite,
    refetch: refetchFavorite,
  } = useGetIsFavoriteNoteQuery(
    user ? { noteId: note.id, userId: user.id } : {},
    { skip: !user },
  );

  const [addFavoriteNote, { isLoading: addingFavorite }] =
    useAddFavoriteNoteMutation();
  const [removeFavoriteNote, { isLoading: removingFavorite }] =
    useRemoveFavoriteNoteMutation();

  // Menu state
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const openMenu = Boolean(anchorEl);

  const handleMenuClick = (event: MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  // Handle favorite toggle
  const handleFavoriteClick = async () => {
    if (!user) {
      navigate('/loginPage');
      return;
    }

    try {
      if (isFavorite) {
        await removeFavoriteNote({
          noteId: note.id,
          userId: user.id,
        }).unwrap();
        toast.success(t('rowNote.favoriteRemoved'));
      } else {
        await addFavoriteNote({
          noteId: note.id,
        }).unwrap();
        toast.success(t('rowNote.favoriteAdded'));
      }
      refetchFavorite();
    } catch (error: any) {
      if (error.status === 409) {
        toast.error(t('rowNote.alreadyFavorited'));
      } else {
        toast.error(t('rowNote.favoriteActionFailed'));
      }
      console.error('Favorite action failed:', error);
    }
  };

  // Handle download
  const handleDownloadClick = async () => {
    if (note.fileUrl) {
      try {
        const response = await fetch(note.fileUrl, {
          method: 'GET',
        });
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        const blob = await response.blob();
        const fileName = note.fileType
          ? `${note.name}.${note.fileType}`
          : note.name;
        saveAs(blob, fileName);
        toast.success(t('rowNote.downloadSuccess'));
      } catch (error) {
        console.error('Download failed:', error);
        toast.error(t('rowNote.downloadFailed'));
      }
    } else {
      toast.error(t('rowNote.fileNotAvailable'));
    }
  };

  // Handle copy link
  const handleCopyLinkClick = async () => {
    try {
      const link = `${window.location.origin}/notes/${note.id}`;
      await navigator.clipboard.writeText(link);
      toast.success(t('rowNote.copySuccess'));
    } catch (error) {
      console.error('Copy failed:', error);
      toast.error(t('rowNote.copyFailed'));
    }
  };

  // Menu action handlers
  const handleOwnerNotes = () => {
    const ownerId = note.ownerUrl?.split('/').pop();
    if (ownerId) {
      if (user?.id === ownerId) {
        navigate('/notes');
      } else {
        navigate(`/users/${ownerId}/notes`);
      }
    }
    handleMenuClose();
  };

  const handleOpenParent = () => {
    const parentId = note.parentUrl?.split('/').pop();
    if (parentId) {
      navigate(`/directories/${parentId}/`);
    }
    handleMenuClose();
  };

  const handleSubjectDirectories = () => {
    const subjectDirectoryUrl = (subjectData as Subject)?.rootDirectoryUrl;
    const subjectDirectoryId = subjectDirectoryUrl?.split('/').pop();
    if (subjectDirectoryId) {
      navigate(`/directories/${subjectDirectoryId}/`);
    }
    handleMenuClose();
  };

  // Determine subject name
  let subjectName: string | JSX.Element = t('rowNote.dataUnknown');
  if (subjectLoading) subjectName = <Skeleton width={80} />;
  else if (subjectError) subjectName = t('rowNote.dataUnknown');
  else if (subjectData?.name) subjectName = subjectData.name;

  // Determine owner name
  let ownerName: string | JSX.Element = t('rowNote.dataUnknown');
  if (ownerLoading) ownerName = <Skeleton width={80} />;
  else if (ownerError) ownerName = t('rowNote.dataUnknown');
  else if (ownerData?.username) ownerName = ownerData.username;

  return (
    <TableRow hover>
      <TableCell>
        <MuiLink component={Link} to={`/notes/${note.id}`} underline="hover">
          {note.name}
        </MuiLink>
      </TableCell>
      <TableCell>{subjectName}</TableCell>
      <TableCell>{ownerName}</TableCell>
      <TableCell>{new Date(note.lastModifiedAt).toLocaleString()}</TableCell>
      <TableCell>{note.avgScore?.toFixed(2) ?? '-'}</TableCell>
      <TableCell
        align="right"
        sx={{
          transition: 'opacity 0.1s',
          opacity: 0,
          '&:hover': {
            opacity: 1,
          },
        }}
      >
        <Box display="flex" alignItems="center" justifyContent="flex-end">
          <Box display="flex" alignItems="center">
            {/* Favorite Button */}
            <Tooltip
              title={
                user
                  ? isFavorite
                    ? t('rowNote.unfavorite')
                    : t('rowNote.favorite')
                  : t('rowNote.loginToFavorite')
              }
            >
              <span>
                <IconButton
                  onClick={handleFavoriteClick}
                  size="small"
                  disabled={addingFavorite || removingFavorite || !user}
                >
                  {isFavorite ? (
                    <FavoriteIcon color="error" />
                  ) : (
                    <FavoriteBorderIcon />
                  )}
                </IconButton>
              </span>
            </Tooltip>

            {/* Download Button */}
            <Tooltip title={t('rowNote.download')}>
              <IconButton onClick={handleDownloadClick} size="small">
                <DownloadIcon />
              </IconButton>
            </Tooltip>

            {/* Copy Link Button */}
            <Tooltip title={t('rowNote.copyLink')}>
              <IconButton onClick={handleCopyLinkClick} size="small">
                <LinkIcon />
              </IconButton>
            </Tooltip>

            {/* More Menu */}
            <Tooltip title={t('rowNote.more')}>
              <IconButton onClick={handleMenuClick} size="small">
                <MoreVertIcon />
              </IconButton>
            </Tooltip>

            <Menu
              anchorEl={anchorEl}
              open={openMenu}
              onClose={handleMenuClose}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
            >
              <MenuItem onClick={handleOwnerNotes}>
                {t('rowNote.openOwnersNotes')}
              </MenuItem>
              <MenuItem onClick={handleOpenParent}>
                {t('rowNote.openParentDirectory')}
              </MenuItem>
              <MenuItem onClick={handleSubjectDirectories}>
                {t('rowNote.openSubjectsDirectories')}
              </MenuItem>
            </Menu>
          </Box>
        </Box>
      </TableCell>
    </TableRow>
  );
};

export default RowNote;
