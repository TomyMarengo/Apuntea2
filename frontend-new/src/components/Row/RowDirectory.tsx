// src/components/Row/RowDirectory.tsx

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
  useAddFavoriteDirectoryMutation,
  useRemoveFavoriteDirectoryMutation,
  useGetIsFavoriteDirectoryQuery,
} from '../../store/slices/directoriesApiSlice';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { Directory, Subject } from '../../types';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import FavoriteIcon from '@mui/icons-material/Favorite';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import LinkIcon from '@mui/icons-material/Link';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import { Column } from '../ResultsTable';

// Define and export ColumnDirectory
export const ColumnDirectory: Column[] = [
  { id: 'name', label: 'Name' },
  { id: 'subject', label: 'Subject' },
  { id: 'owner', label: 'Owner' },
  { id: 'lastModifiedAt', label: 'Last Modified' },
  { id: 'actions', label: 'Actions', align: 'right' },
];

interface RowDirectoryProps {
  directory: Directory;
}

const RowDirectory: React.FC<RowDirectoryProps> = ({ directory }) => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const user = useSelector(selectCurrentUser);

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
  const {
    data: isFavorite,
    refetch: refetchFavorite,
    isLoading: favoriteLoading,
  } = useGetIsFavoriteDirectoryQuery(
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
      if (isFavorite) {
        await removeFavoriteDirectory({
          directoryId: directory.id,
          userId: user.id,
        }).unwrap();
        toast.success(t('rowDirectory.favoriteRemoved'));
      } else {
        await addFavoriteDirectory({
          directoryId: directory.id,
        }).unwrap();
        toast.success(t('rowDirectory.favoriteAdded'));
      }
      refetchFavorite();
    } catch (error: any) {
      if (error.status === 409) {
        toast.error(t('rowDirectory.alreadyDirectoryFavorited'));
      } else {
        toast.error(t('rowDirectory.favoriteActionFailed'));
      }
      console.error('Favorite action failed:', error);
    }
  };

  // Handle copy link
  const handleCopyLinkClick = async () => {
    try {
      const link = `${window.location.origin}/directories/${directory.id}`;
      await navigator.clipboard.writeText(link);
      toast.success(t('rowDirectory.copySuccess'));
    } catch (error) {
      console.error('Copy failed:', error);
      toast.error(t('rowDirectory.copyFailed'));
    }
  };

  // Menu action handlers
  const handleOwnerNotes = () => {
    const ownerId = directory.ownerUrl?.split('/').pop();
    if (ownerId) {
      navigate(`/users/${ownerId}/notes`);
    }
    handleMenuClose();
  };

  const handleOpenParent = () => {
    const parentId = directory.parentUrl?.split('/').pop();
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
  let subjectName: string | JSX.Element = t('rowDirectory.dataUnknown');
  if (subjectLoading) subjectName = <Skeleton width={80} />;
  else if (subjectError) subjectName = t('rowDirectory.dataUnknown');
  else if (subjectData?.name) subjectName = subjectData.name;

  // Determine owner name
  let ownerName: string | JSX.Element = t('rowDirectory.dataUnknown');
  if (ownerLoading) ownerName = <Skeleton width={80} />;
  else if (ownerError) ownerName = t('rowDirectory.dataUnknown');
  else if (ownerData?.username) ownerName = ownerData.username;

  return (
    <TableRow hover>
      <TableCell>
        <MuiLink
          component={Link}
          to={`/directories/${directory.id}`}
          underline="hover"
        >
          {directory.name}
        </MuiLink>
      </TableCell>
      <TableCell>{subjectName}</TableCell>
      <TableCell>{ownerName}</TableCell>
      <TableCell>
        {new Date(directory.lastModifiedAt).toLocaleString()}
      </TableCell>
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
                    ? t('rowDirectory.unfavorite')
                    : t('rowDirectory.favorite')
                  : t('rowDirectory.loginToFavorite')
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

            {/* Copy Link Button */}
            <Tooltip title={t('rowDirectory.copyLink')}>
              <IconButton onClick={handleCopyLinkClick} size="small">
                <LinkIcon />
              </IconButton>
            </Tooltip>

            {/* More Menu */}
            <Tooltip title={t('rowDirectory.more')}>
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
                {t('rowDirectory.openOwnersNotes')}
              </MenuItem>
              <MenuItem onClick={handleOpenParent}>
                {t('rowDirectory.openParentDirectory')}
              </MenuItem>
              <MenuItem onClick={handleSubjectDirectories}>
                {t('rowDirectory.openSubjectsDirectories')}
              </MenuItem>
            </Menu>
          </Box>
        </Box>
      </TableCell>
    </TableRow>
  );
};

export default RowDirectory;
