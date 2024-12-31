// src/pages/Login/LoginPage.tsx

import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  TextField,
  Button,
  IconButton,
  InputAdornment,
  Box,
  Typography,
  Card,
  CardContent,
  CircularProgress,
} from '@mui/material';
import {
  Visibility,
  VisibilityOff,
  Close as CloseIcon,
} from '@mui/icons-material';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import useLogin from '../../hooks/useLogin';
import { Helmet } from 'react-helmet-async';

const loginSchema = z.object({
  email: z.string().email('loginPage.invalidEmail'),
  password: z.string().min(4, 'loginPage.passwordMinLength'),
});

type LoginForm = z.infer<typeof loginSchema>;

export default function LoginPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { loginUser } = useLogin();

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    watch,
  } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    mode: 'onChange',
  });

  const onSubmit = async (data: LoginForm) => {
    try {
      setLoading(true);
      setLoginError(null);
      await loginUser({ email: data.email, password: data.password });
      navigate('/');
    } catch (err: any) {
      setLoginError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleTogglePassword = () => setShowPassword(!showPassword);
  const handleClearField = (fieldName: keyof LoginForm) => {
    reset({ ...watch(), [fieldName]: '' });
  };

  let pageTitle = t('loginPage.titlePage');
  if (loading) {
    pageTitle = t('loginPage.loading');
  } else if (loginError) {
    pageTitle = t('loginPage.errorFetching', {
      error: String(loginError),
    });
  }

  return (
    <>
      <Helmet>
        <title>{pageTitle}</title>
      </Helmet>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '80vh',
        }}
      >
        <Card sx={{ maxWidth: 400, width: '100%', mx: 2 }}>
          <CardContent>
            <Typography variant="h4" gutterBottom align="center">
              {t('loginPage.login')}
            </Typography>

            <Box component="form" onSubmit={handleSubmit(onSubmit)}>
              {/* EMAIL FIELD */}
              <TextField
                label={t('loginPage.email')}
                variant="outlined"
                fullWidth
                margin="normal"
                {...register('email')}
                error={!!errors.email}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.email ? t(errors.email.message as string) : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: watch('email') ? (
                    <InputAdornment position="end">
                      <IconButton onClick={() => handleClearField('email')}>
                        <CloseIcon />
                      </IconButton>
                    </InputAdornment>
                  ) : null,
                }}
              />

              {/* PASSWORD FIELD */}
              <TextField
                label={t('loginPage.password')}
                variant="outlined"
                fullWidth
                margin="normal"
                type={showPassword ? 'text' : 'password'}
                {...register('password')}
                error={!!errors.password}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.password
                      ? t(errors.password.message as string)
                      : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={handleTogglePassword}>
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                      {watch('password') && (
                        <IconButton
                          onClick={() => handleClearField('password')}
                        >
                          <CloseIcon />
                        </IconButton>
                      )}
                    </InputAdornment>
                  ),
                }}
              />

              {/* Overall Login Error */}
              {loginError && (
                <Typography variant="body2" color="error" sx={{ mt: 1 }}>
                  {loginError}
                </Typography>
              )}

              {/* SUBMIT BUTTON */}
              <Button
                type="submit"
                variant="contained"
                color="primary"
                fullWidth
                sx={{ mt: 2 }}
                disabled={loading}
              >
                {loading ? (
                  <CircularProgress size={24} />
                ) : (
                  t('loginPage.login')
                )}
              </Button>
            </Box>

            <Typography variant="body2" align="center" sx={{ mt: 2 }}>
              {t('loginPage.noAccount')}{' '}
              <RouterLink to="/register" style={{ color: '#1976d2' }}>
                {t('loginPage.signup')}
              </RouterLink>
            </Typography>
            <Typography variant="body2" align="center" sx={{ mt: 1 }}>
              <RouterLink to="/forgot-password" style={{ color: '#1976d2' }}>
                {t('loginPage.forgotPassword')}
              </RouterLink>
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
