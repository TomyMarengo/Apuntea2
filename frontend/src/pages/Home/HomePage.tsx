// src/pages/Home/HomePage.tsx

import { Box, Typography, Card, CardContent, Stack } from '@mui/material';
import { Folder, Search, Person } from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { Trans, useTranslation } from 'react-i18next';
import { useSelector } from 'react-redux';
import { selectCurrentUser } from '../../store/slices/authSlice';
import { Helmet } from 'react-helmet-async';

export default function HomePage() {
  const { t } = useTranslation('homePage');
  const user = useSelector(selectCurrentUser);

  const isLoggedIn = !!user;

  const cards = [
    {
      title: t('cards.organizeNotes.title'),
      description: t('homePage.cards.organizeNotes.description'),
      icon: <Folder sx={{ fontSize: 100 }} />,
      link: isLoggedIn ? '/notes' : '/login',
    },
    {
      title: t('homePage.cards.findNotes.title'),
      description: t('homePage.cards.findNotes.description'),
      icon: <Search sx={{ fontSize: 100 }} />,
      link: '/search',
    },
    {
      title: isLoggedIn
        ? t('homePage.cards.editProfile.title')
        : t('homePage.cards.register.title'),
      description: isLoggedIn
        ? t('homePage.cards.editProfile.description')
        : t('homePage.cards.register.description'),
      icon: <Person sx={{ fontSize: 100 }} />,
      link: isLoggedIn ? '/profile' : '/register',
    },
  ];

  return (
    <>
      <Helmet>
        <title>{t('homePage.titlePage')}</title>
      </Helmet>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          textAlign: 'center',
          padding: 4,
          background: 'background.default',
          overflow: 'hidden',
        }}
      >
        {/* Welcome Message */}
        <Typography
          variant="h2"
          gutterBottom
          sx={{
            fontWeight: 'bold',
            color: 'primary.text',
            fontSize: isLoggedIn ? '3.5rem' : '5rem',
            marginBottom: 1,
          }}
        >
          {isLoggedIn ? (
            <Trans
              i18nKey="homePage.welcomeUser"
              values={{ username: user.username || user.email }}
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
          ) : (
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
          )}
        </Typography>

        {/* Tagline */}
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

        {/* Feature Cards */}
        <Stack
          direction={{ sm: 'column', md: 'row' }}
          spacing={12}
          justifyContent="center"
          alignItems="center"
          sx={{ maxWidth: '1200px', width: '100%' }}
        >
          {cards.map((card, index) => (
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
    </>
  );
}
