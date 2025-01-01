import React, { useState } from 'react';
import {
  Box,
  IconButton,
  Tooltip,
  Menu,
  MenuItem,
  Skeleton,
  Link as MuiLink,
  TableRow,
  TableCell,
} from '@mui/material';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import {
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
} from '../../store/slices/notesApiSlice';
import { saveAs } from 'file-saver';
import { useSelector } from 'react-redux';
import {
  selectCurrentUser,
  selectCurrentToken,
} from '../../store/slices/authSlice';
import { Note, Subject } from '../../types';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import DownloadIcon from '@mui/icons-material/Download';
import LinkIcon from '@mui/icons-material/Link';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import Delete from '@mui/icons-material/Delete';
import DeleteNoteDialog from '../../pages/Notes/dialogs/DeleteNoteDialog';
import { Column } from '../../types';
import { getFileIcon } from '../../utils/helperFunctions';

export const ColumnNote: Column[] = [
  { id: 'name', label: 'name' },
  { id: 'subject', label: 'subject' },
  { id: 'owner', label: 'owner' },
  { id: 'lastModifiedAt', label: 'lastModifiedAt' },
  { id: 'score', label: 'score' },
  { id: 'actions', label: 'actions', align: 'right' },
];

interface RowNoteProps {
  note: Note;
  led?: boolean;
}

const RowNote: React.FC<RowNoteProps> = ({ note }) => {
  const { t } = useTranslation('rowNote');
  const navigate = useNavigate();
  const location = useLocation(); // Hook para obtener la ubicación actual
  const user = useSelector(selectCurrentUser);
  const token = useSelector(selectCurrentToken);

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
  const { data: isFavorite, refetch: refetchFavorite } =
    useGetIsFavoriteNoteQuery(
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

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
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
        toast.success(t('favoriteRemoved'));
      } else {
        await addFavoriteNote({
          noteId: note.id,
        }).unwrap();
        toast.success(t('favoriteAdded'));
      }
      refetchFavorite();
    } catch (error: any) {
      if (error.status === 409) {
        toast.error(t('alreadyFavorited'));
      } else {
        toast.error(t('favoriteActionFailed'));
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
        toast.success(t('downloadSuccess'));
      } catch (error) {
        console.error('Download failed:', error);
        toast.error(t('downloadFailed'));
      }
    } else {
      toast.error(t('fileNotAvailable'));
    }
  };

  // Handle copy link
  const handleCopyLinkClick = async () => {
    try {
      const link = `${window.location.origin}/notes/${note.id}`;
      await navigator.clipboard.writeText(link);
      toast.success(t('copySuccess'));
    } catch (error) {
      console.error('Copy failed:', error);
      toast.error(t('copyFailed'));
    }
  };

  // Menu action handlers
  const handleOwnerNotes = () => {
    const ownerId = note.ownerUrl?.split('/').pop();
    if (ownerId) {
      if (user?.id === ownerId) {
        navigate('/notes');
      } else {
        navigate(`/users/${ownerId}`);
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
  let subjectName: string | JSX.Element = t('dataUnknown');
  if (subjectLoading) subjectName = <Skeleton width={80} />;
  else if (subjectError) subjectName = t('dataUnknown');
  else if (subjectData?.name) subjectName = subjectData.name;

  // Determine owner name
  let ownerName: string | JSX.Element = t('dataUnknown');
  if (ownerLoading) ownerName = <Skeleton width={80} />;
  else if (ownerError) ownerName = t('dataUnknown');
  else if (ownerData?.username) ownerName = ownerData.username;

  // Delete modal state
  const [openDelete, setOpenDelete] = useState(false);

  const handleDeleteClick = () => {
    setOpenDelete(true);
  };

  const handleCloseDelete = () => {
    setOpenDelete(false);
  };

  const isOwner = user?.id === note.ownerUrl?.split('/').pop();
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN') || false;

  // Check if the current path corresponds to the subject directory
  const isSameSubject =
    location.pathname ===
    `/directories/${subjectData?.rootDirectoryUrl?.split('/').pop()}`;

  return (
    <TableRow hover>
      <TableCell>
        <Box display="flex" alignItems="center">
          {/* Icono del archivo */}
          {getFileIcon(note.fileType, 20)}

          <MuiLink
            component={Link}
            to={`/notes/${note.id}`}
            underline="hover"
            sx={{ ml: 1 }}
          >
            {note.name}
          </MuiLink>
        </Box>
      </TableCell>
      <TableCell>
        {isSameSubject ? (
          subjectName
        ) : (
          <MuiLink
            component={Link}
            to={`/directories/${subjectData?.rootDirectoryUrl?.split('/').pop()}`}
            underline="hover"
          >
            {subjectName}
          </MuiLink>
        )}
      </TableCell>
      <TableCell>
        <MuiLink
          component={Link}
          to={`/users/${ownerData?.id}`}
          underline="hover"
        >
          {ownerName}
        </MuiLink>
      </TableCell>
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
                    ? t('unfavorite')
                    : t('favorite')
                  : t('loginToFavorite')
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
            <Tooltip title={t('download')}>
              <IconButton onClick={handleDownloadClick} size="small">
                <DownloadIcon />
              </IconButton>
            </Tooltip>

            {/* Copy Link Button */}
            <Tooltip title={t('copyLink')}>
              <IconButton onClick={handleCopyLinkClick} size="small">
                <LinkIcon />
              </IconButton>
            </Tooltip>

            {/* Delete Button */}
            {(isAdmin || isOwner) && (
              <Tooltip title={t('delete')}>
                <IconButton onClick={handleDeleteClick} size="small">
                  <Delete />
                </IconButton>
              </Tooltip>
            )}

            {/* More Menu */}
            <Tooltip title={t('more')}>
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
                {t('openOwnersNotes')}
              </MenuItem>
              <MenuItem onClick={handleOpenParent}>
                {t('openParentDirectory')}
              </MenuItem>
              <MenuItem onClick={handleSubjectDirectories}>
                {t('openSubjectsDirectories')}
              </MenuItem>
            </Menu>
          </Box>
        </Box>
      </TableCell>

      {/* DeleteNoteDialog component */}
      <DeleteNoteDialog
        open={openDelete}
        onClose={handleCloseDelete}
        note={note}
        shouldShowReason={isAdmin && !isOwner}
      />
    </TableRow>
  );
};

export default RowNote;
