// src/components/Row/RowNote.tsx

import Delete from '@mui/icons-material/Delete';
import DownloadIcon from '@mui/icons-material/Download';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import LinkIcon from '@mui/icons-material/Link';
import MoreVertIcon from '@mui/icons-material/MoreVert';
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
import { saveAs } from 'file-saver';
import React, { useState, JSX } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import DeleteNoteDialog from '../../pages/Notes/dialogs/DeleteNoteDialog';
import EditNoteDialog from '../../pages/Notes/dialogs/EditNoteDialog';
import {
  selectCurrentUser,
  selectCurrentToken,
} from '../../store/slices/authSlice';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';
import {
  useAddFavoriteNoteMutation,
  useRemoveFavoriteNoteMutation,
  useGetIsFavoriteNoteQuery,
} from '../../store/slices/notesApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { Note, Column } from '../../types';
import NoteFileIcon from '../NoteFileIcon';

interface RowNoteProps {
  note: Note;
  columnsToShow: Column[];
  isDirectoryPage: boolean;
}

const RowNote: React.FC<RowNoteProps> = ({
  note,
  columnsToShow,
  isDirectoryPage,
}) => {
  const { t } = useTranslation('rowNote');
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const token = useSelector(selectCurrentToken);

  const [openEditDialog, setOpenEditDialog] = useState(false);

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
      if (isFavorite?.success) {
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

  const handleOpenParent = () => {
    if (note.parentId) {
      navigate(`/directories/${note.parentId}/`);
    }
    handleMenuClose();
  };

  const handleSubjectDirectories = () => {
    const subjectDirectoryId = subjectData?.rootDirectoryId;
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

  const handleEditClick = () => {
    setOpenEditDialog(true);
  };

  const handleCloseEditDialog = () => {
    setOpenEditDialog(false);
  };

  const isOwner = user?.selfUrl === note.ownerUrl;
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN') || false;

  const renderCell = (column: Column) => {
    switch (column.id) {
      case 'name':
        return (
          <TableCell key={column.id}>
            <Box display="flex" alignItems="center">
              <NoteFileIcon fileType={note.fileType} size={24} />

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
        );
      case 'subject':
        return (
          <TableCell key={column.id}>
            <MuiLink
              component={Link}
              to={`/directories/${subjectData?.rootDirectoryId}`}
              underline="hover"
            >
              {subjectName}
            </MuiLink>
          </TableCell>
        );
      case 'owner':
        return (
          <TableCell key={column.id}>
            <MuiLink
              component={Link}
              to={`/users/${ownerData?.id}`}
              underline="hover"
            >
              {ownerName}
            </MuiLink>
          </TableCell>
        );
      case 'lastModifiedAt':
        return (
          <TableCell key={column.id}>
            {new Date(note.lastModifiedAt).toLocaleString()}
          </TableCell>
        );
      case 'score':
        return (
          <TableCell key={column.id}>
            {note.avgScore && note.avgScore > 0
              ? note.avgScore.toFixed(2)
              : t('noScore')}
          </TableCell>
        );
      case 'actions':
        return (
          <TableCell
            key={column.id}
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
                      ? isFavorite?.success
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
                      {isFavorite?.success ? (
                        <FavoriteIcon color="error" />
                      ) : (
                        <FavoriteBorderIcon />
                      )}
                    </IconButton>
                  </span>
                </Tooltip>
                {/* Edit Button */}
                {isOwner && (
                  <Tooltip title={t('edit')}>
                    <IconButton onClick={handleEditClick} size="small">
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                )}
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
                {!isDirectoryPage && (
                  <Tooltip title={t('more')}>
                    <IconButton onClick={handleMenuClick} size="small">
                      <MoreVertIcon />
                    </IconButton>
                  </Tooltip>
                )}

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
        );
      default:
        return <TableCell key={column.id}>-</TableCell>;
    }
  };

  return (
    <>
      <TableRow hover>{columnsToShow.map(renderCell)}</TableRow>
      {/* DeleteNoteDialog */}
      <DeleteNoteDialog
        open={openDelete}
        onClose={handleCloseDelete}
        note={note}
        shouldShowReason={isAdmin && !isOwner}
      />
      {/* EditNoteDialog */}
      <EditNoteDialog
        open={openEditDialog}
        onClose={handleCloseEditDialog}
        note={note}
      />
    </>
  );
};

export default RowNote;
