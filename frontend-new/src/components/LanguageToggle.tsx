import { useTranslation } from 'react-i18next';
import { IconButton, Menu, MenuItem } from '@mui/material';
import LanguageIcon from '@mui/icons-material/Language';
import { useState } from 'react';

export default function LanguageToggle() {
  const { i18n } = useTranslation();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const handleOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = (lang?: string) => {
    if (lang) {
      i18n.changeLanguage(lang);
    }
    setAnchorEl(null);
  };

  return (
    <>
      <IconButton onClick={handleOpen}>
        <LanguageIcon sx={{ color: 'primary.main' }} />
      </IconButton>
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => handleClose()}
      >
        <MenuItem onClick={() => handleClose('es')}>Español</MenuItem>
        <MenuItem onClick={() => handleClose('en')}>English</MenuItem>
      </Menu>
    </>
  );
}
