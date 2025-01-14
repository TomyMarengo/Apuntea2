// src/pages/NotFound/NotFoundPage.tsx

import { Box, Button, Typography } from '@mui/material';
import { Helmet } from 'react-helmet-async';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

import pageNotFoundImage from '../../assets/page_not_found.png';

export default function NotFoundPage() {
  const { t } = useTranslation('notFoundPage');

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>

      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          height: '100%',
          padding: 4,
          backgroundColor: 'background.paper',
        }}
      >
        {/* Imagen */}
        <Box
          component="img"
          src={pageNotFoundImage}
          alt={t('imageAlt')}
          sx={{
            width: { xs: '100%', sm: '50%', md: '25%' },
            maxHeight: '400px',
            marginBottom: 4,
          }}
        />

        {/* Mensaje */}
        <Typography
          variant="h3"
          gutterBottom
          sx={{
            fontWeight: 'bold',
            color: 'primary.text',
          }}
        >
          {t('title')}
        </Typography>
        <Typography
          variant="body1"
          sx={{
            color: 'secondary.text',
            marginBottom: 4,
          }}
        >
          {t('description')}
        </Typography>

        {/* Botón para regresar */}
        <Button
          variant="contained"
          color="primary"
          component={Link}
          to="/"
          sx={{
            paddingX: 4,
            paddingY: 1.5,
            fontSize: '1.2rem',
          }}
        >
          {t('goHome')}
        </Button>
      </Box>
    </>
  );
}
