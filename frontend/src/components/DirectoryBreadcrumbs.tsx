// src/components/DirectoryBreadcrumbs.tsx

import MoreHorizIcon from '@mui/icons-material/MoreHoriz';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import {
  Box,
  Typography,
  CircularProgress,
  Alert,
  Link as MuiLink,
  Breadcrumbs,
  IconButton,
  Menu,
  MenuItem,
} from '@mui/material';
import React from 'react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link as RouterLink, useNavigate } from 'react-router-dom';

import useDirectoryBreadcrumb from '../hooks/useDirectoryBreadcrumb';
import { Directory, Note } from '../types';

interface DirectoryBreadcrumbsProps {
  currentDirectory?: Directory;
  note?: Note;
}

const DirectoryBreadcrumbs: React.FC<DirectoryBreadcrumbsProps> = ({
  currentDirectory,
  note,
}) => {
  const { t } = useTranslation('directoryBreadcrumbs');

  const navigate = useNavigate();

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const {
    breadcrumb,
    loading: isLoading,
    error: isError,
  } = useDirectoryBreadcrumb({
    currentDirectory,
  });

  if (isLoading) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <CircularProgress />
      </Box>
    );
  }

  if (isError) {
    return (
      <Box display="flex" justifyContent="center" mt={5}>
        <Alert severity="error">{t('errorFetchingDirectory')}</Alert>
      </Box>
    );
  }

  return (
    <Box>
      {breadcrumb.length === 0 && note && (
        <Typography variant="h6" color="text.primary">
          {note.name}
        </Typography>
      )}

      {breadcrumb.length === 1 && !note && (
        <Typography variant="h6" color="text.primary">
          {t('isRoot')}
        </Typography>
      )}
      {breadcrumb.length === 1 && note && (
        <Breadcrumbs
          separator={<NavigateNextIcon fontSize="small" />}
          aria-label="breadcrumb"
          sx={{ display: 'flex', alignItems: 'center' }}
        >
          <MuiLink
            variant="h6"
            component={RouterLink}
            to={`/directories/${breadcrumb[0].id}`}
            underline="hover"
            color="inherit"
          >
            {breadcrumb[0].name}
          </MuiLink>
          <Typography variant="h6" color="text.primary">
            {note.name}
          </Typography>
        </Breadcrumbs>
      )}
      {breadcrumb.length === 2 && (
        <Breadcrumbs
          separator={<NavigateNextIcon fontSize="small" />}
          aria-label="breadcrumb"
          sx={{ display: 'flex', alignItems: 'center' }}
        >
          {/* Root Ancestor */}
          <MuiLink
            variant="h6"
            component={RouterLink}
            to={`/directories/${breadcrumb[0].id}`}
            underline="hover"
            color="inherit"
          >
            {breadcrumb[0].name}
          </MuiLink>

          {/* Current Directory */}
          {note ? (
            <MuiLink
              variant="h6"
              component={RouterLink}
              to={`/directories/${currentDirectory?.id}`}
              underline="hover"
              color="inherit"
            >
              <Typography variant="h6">{currentDirectory?.name}</Typography>
            </MuiLink>
          ) : (
            <Typography variant="h6" color="text.primary">
              {currentDirectory?.name}
            </Typography>
          )}

          {note && (
            <Typography variant="h6" color="text.primary">
              {note.name}
            </Typography>
          )}
        </Breadcrumbs>
      )}

      {breadcrumb.length > 2 && (
        <Breadcrumbs
          separator={<NavigateNextIcon fontSize="small" />}
          aria-label="breadcrumb"
          sx={{ display: 'flex', alignItems: 'center' }}
        >
          {/* Root Ancestor */}
          <MuiLink
            variant="h6"
            component={RouterLink}
            to={`/directories/${breadcrumb[0].id}`}
            underline="hover"
            color="inherit"
          >
            {breadcrumb[0].name}
          </MuiLink>
          {/* Ellipsis */}
          {breadcrumb.length > 3 && (
            <>
              <IconButton
                size="small"
                sx={{ p: 0 }}
                onClick={handleMenuOpen}
                aria-controls={anchorEl ? 'breadcrumb-menu' : undefined}
                aria-haspopup="true"
                aria-expanded={anchorEl ? 'true' : undefined}
              >
                <MoreHorizIcon />
              </IconButton>

              <Menu
                id="breadcrumb-menu"
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                {breadcrumb.slice(1, -1).map((dir) => (
                  <MenuItem
                    key={dir.id}
                    onClick={() => {
                      navigate(`/directories/${dir.id}`);
                      handleMenuClose();
                    }}
                  >
                    {dir.name}
                  </MenuItem>
                ))}
              </Menu>
            </>
          )}
          {/* Immediate Parent */}
          <MuiLink
            variant="h6"
            component={RouterLink}
            to={`/directories/${breadcrumb[breadcrumb.length - 2].id}`}
            underline="hover"
            color="inherit"
          >
            {breadcrumb[breadcrumb.length - 2].name}
          </MuiLink>
          {/* Current Directory */}
          {note ? (
            <MuiLink
              variant="h6"
              component={RouterLink}
              to={`/directories/${currentDirectory?.id}`}
              underline="hover"
              color="inherit"
            >
              <Typography variant="h6">{currentDirectory?.name}</Typography>
            </MuiLink>
          ) : (
            <Typography variant="h6" color="text.primary">
              {currentDirectory?.name}
            </Typography>
          )}
          {note && (
            <Typography variant="h6" color="text.primary">
              {note.name}
            </Typography>
          )}
        </Breadcrumbs>
      )}
    </Box>
  );
};

export default DirectoryBreadcrumbs;
