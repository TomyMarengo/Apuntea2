// src/components/SeasonalModeIconButton.tsx

import FavoriteIcon from '@mui/icons-material/Favorite';
import HeartBrokenIcon from '@mui/icons-material/HeartBroken';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import { useTranslation } from 'react-i18next';
import { useSelector, useDispatch } from 'react-redux';

import { toggleSeasonalMode } from '../store/slices/themeSlice';
import { RootState } from '../store/store';

export default function SeasonalModeToggle() {
  const dispatch = useDispatch();
  const { t } = useTranslation('common');
  const isSeasonalMode = useSelector(
    (state: RootState) => state.theme.isSeasonalMode,
  );

  const handleClick = () => {
    dispatch(toggleSeasonalMode());
  };

  return (
    <Tooltip
      title={
        isSeasonalMode ? t('disableSeasonalTheme') : t('enableSeasonalTheme')
      }
    >
      <IconButton onClick={handleClick}>
        {isSeasonalMode ? (
          <HeartBrokenIcon sx={{ color: 'primary.main' }} />
        ) : (
          <FavoriteIcon sx={{ color: 'primary.main' }} />
        )}
      </IconButton>
    </Tooltip>
  );
}
