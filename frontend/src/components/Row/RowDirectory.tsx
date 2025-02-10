// src/components/Follow/RowDirectory.tsx

import Delete from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FolderIcon from '@mui/icons-material/Folder';
import LinkIcon from '@mui/icons-material/Link';
import MoreVertIcon from '@mui/icons-material/MoreVert';
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
import React, { JSX } from 'react';
import { useState, MouseEvent } from 'react';
import { useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import DeleteDirectoryDialog from '../../pages/Directories/dialogs/DeleteDirectoryDialog';
import EditDirectoryDialog from '../../pages/Directories/dialogs/EditDirectoryDialog';
import {
  selectCurrentToken,
  selectCurrentUser,
} from '../../store/slices/authSlice';
import {
  useAddFavoriteDirectoryMutation,
  useRemoveFavoriteDirectoryMutation,
  useGetIsFavoriteDirectoryQuery,
} from '../../store/slices/directoriesApiSlice';
import { useGetSubjectQuery } from '../../store/slices/institutionsApiSlice';
import { useGetUserQuery } from '../../store/slices/usersApiSlice';
import { Directory, Column } from '../../types';

interface RowDirectoryProps {
  directory: Directory;
  columnsToShow: Column[];
  isDirectoryPage: boolean;
}

const RowDirectory: React.FC<RowDirectoryProps> = ({
  directory,
  columnsToShow,
  isDirectoryPage,
}) => {
  const { t } = useTranslation('rowDirectory');
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);
  const searchParams = new URLSearchParams(window.location.search);
  const keptSearchParams = new URLSearchParams();
  keptSearchParams.set('asc', searchParams.get('asc') || '');
  keptSearchParams.set('sortBy', searchParams.get('sortBy') || '');
  keptSearchParams.set('page', '1');
  keptSearchParams.set('userId', searchParams.get('userId') || '');

  // Fetch subject data
  const {
    data: subjectData,
    isLoading: subjectLoading,
    isError: subjectError,
  } = useGetSubjectQuery({ url: directory?.subjectUrl });

  // Fetch owner data
  const {
    data: ownerData,
    isLoading: ownerLoading,
    isError: ownerError,
  } = useGetUserQuery({ url: directory.ownerUrl });

  // Favorite queries and mutations
  const { data: isFavorite, refetch: refetchFavorite } =
    useGetIsFavoriteDirectoryQuery(
      user ? { directoryId: directory.id, userId: user.id } : {},
      { skip: !user },
    );

  const [addFavoriteDirectory, { isLoading: addingFavorite }] =
    useAddFavoriteDirectoryMutation();
  const [removeFavoriteDirectory, { isLoading: removingFavorite }] =
    useRemoveFavoriteDirectoryMutation();

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
      if (isFavorite?.success) {
        await removeFavoriteDirectory({
          directoryId: directory.id,
          userId: user.id,
        }).unwrap();
        toast.success(t('favoriteRemoved'));
      } else {
        await addFavoriteDirectory({
          directoryId: directory.id,
        }).unwrap();
        toast.success(t('favoriteAdded'));
      }
      refetchFavorite();
    } catch (error: any) {
      if (error.status === 409) {
        toast.error(t('alreadyDirectoryFavorited'));
      } else {
        toast.error(t('favoriteActionFailed'));
      }
      console.error('Favorite action failed:', error);
    }
  };

  // Handle copy link
  const handleCopyLinkClick = async () => {
    try {
      const link = `${window.location.origin}/directories/${directory.id}`;
      await navigator.clipboard.writeText(link);
      toast.success(t('copySuccess'));
    } catch (error) {
      console.error('Copy failed:', error);
      toast.error(t('copyFailed'));
    }
  };

  // Delete directory dialog state
  const [openDelete, setOpenDeleteModal] = useState(false);

  const handleDeleteClick = () => {
    setOpenDeleteModal(true);
  };

  const handleCloseDelete = () => {
    setOpenDeleteModal(false);
  };

  // Edit directory dialog state
  const [openEdit, setOpenEditModal] = useState(false);

  const handleEditClick = () => {
    setOpenEditModal(true);
  };

  const handleCloseEdit = () => {
    setOpenEditModal(false);
  };

  const handleOpenParent = () => {
    if (directory.parentId) {
      navigate(`/directories/${directory.parentId}/`);
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

  const token = useSelector(selectCurrentToken);

  const isOwner = user?.selfUrl === directory.ownerUrl;
  const isAdmin = token?.payload?.authorities.includes('ROLE_ADMIN') || false;

  const renderCell = (column: Column) => {
    switch (column.id) {
      case 'name':
        return (
          <TableCell key={column.id}>
            <Box display="flex" alignItems="center">
              <FolderIcon
                sx={{
                  color: directory.iconColor
                    ? `#${directory.iconColor}`
                    : 'primary.main',
                }}
              />
              <MuiLink
                component={Link}
                to={`/directories/${directory.id}?${keptSearchParams.toString()}`}
                underline="hover"
                sx={{ ml: 1 }}
              >
                {directory.name}
              </MuiLink>
            </Box>
          </TableCell>
        );
      case 'subject':
        return (
          <TableCell key={column.id}>
            <MuiLink
              component={Link}
              to={`/directories/${subjectData?.rootDirectoryId}?${keptSearchParams.toString()}`}
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
            {new Date(directory.lastModifiedAt).toLocaleString()}
          </TableCell>
        );
      case 'actions':
        return (
          <TableCell
            key={column.id}
            align={column.align || 'left'}
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
      {/* Delete Directory Dialog */}
      <DeleteDirectoryDialog
        open={openDelete}
        onClose={handleCloseDelete}
        directory={directory}
        shouldShowReason={isAdmin && !isOwner}
      />

      {/* Edit Directory Dialog */}
      <EditDirectoryDialog
        open={openEdit}
        onClose={handleCloseEdit}
        directory={directory}
      />
    </>
  );
};

export default RowDirectory;
