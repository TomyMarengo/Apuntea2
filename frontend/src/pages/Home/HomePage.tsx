// src/pages/Home/HomePage.tsx

import { Box, Typography, Card, CardContent, Stack } from '@mui/material';
import { Folder, Search, Person } from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { Trans, useTranslation } from 'react-i18next';

export default function HomePage() {
  const { t } = useTranslation();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        textAlign: 'center',
        padding: 4,
        minHeight: '100vh',
        background: 'background.default',
        overflow: 'hidden',
      }}
    >
      <Typography
        variant="h2"
        gutterBottom
        sx={{
          fontWeight: 'bold',
          color: 'primary.text',
          fontSize: '5rem',
          marginBottom: 1,
        }}
      >
        <Trans
          i18nKey="homePage.welcome"
          components={{
            appName: (
              <Box
                component="span"
                sx={{
                  color: 'primary.main',
                }}
              />
            ),
          }}
        />
      </Typography>
      <Typography
        variant="h5"
        gutterBottom
        sx={{
          color: 'secondary.text',
          marginBottom: 8,
          fontSize: '2rem',
          marginTop: 0,
        }}
      >
        {t('homePage.tagline')}
      </Typography>

      <Stack
        direction={{ sm: 'column', md: 'row' }}
        spacing={12}
        justifyContent="center"
        alignItems="center"
        sx={{ maxWidth: '1200px', width: '100%' }}
      >
        {[
          {
            title: t('homePage.cards.organizeNotes.title'),
            description: t('homePage.cards.organizeNotes.description'),
            icon: <Folder sx={{ fontSize: 100 }} />,
            link: '/notes',
          },
          {
            title: t('homePage.cards.findNotes.title'),
            description: t('homePage.cards.findNotes.description'),
            icon: <Search sx={{ fontSize: 100 }} />,
            link: '/search',
          },
          {
            title: t('homePage.cards.editProfile.title'),
            description: t('homePage.cards.editProfile.description'),
            icon: <Person sx={{ fontSize: 100 }} />,
            link: '/profile',
          },
        ].map((card, index) => (
          <Box
            key={index}
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              width: { sm: '90%', md: '27%' },
            }}
          >
            <Card
              component={Link}
              to={card.link}
              sx={{
                textDecoration: 'none',
                boxShadow: 3,
                borderRadius: 12,
                padding: 2,
                height: 350,
                width: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                transition: 'transform 0.3s, box-shadow 0.3s',
                '&:hover': {
                  transform: 'scale(1.05)',
                  boxShadow: 6,
                  outline: `2px solid`,
                  outlineColor: 'primary.main',
                  '& .icon, & .text': {
                    color: 'primary.dark',
                  },
                },
                backgroundColor: 'background.paper',
              }}
            >
              <CardContent sx={{ textAlign: 'center' }}>
                <Box
                  className="icon"
                  sx={{
                    color: 'primary.text',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}
                >
                  {card.icon}
                </Box>
                <Typography
                  variant="h6"
                  className="text"
                  sx={{
                    marginTop: 2,
                    fontWeight: 'bold',
                    color: 'primary.text',
                    fontSize: '1.5rem',
                  }}
                >
                  {card.title}
                </Typography>
                <Typography
                  variant="body2"
                  className="text"
                  sx={{
                    color: 'primary.text',
                    fontSize: '1rem',
                    maxWidth: '230px',
                    margin: '0 auto',
                  }}
                >
                  {card.description}
                </Typography>
              </CardContent>
            </Card>
          </Box>
        ))}
      </Stack>
    </Box>
  );
}
