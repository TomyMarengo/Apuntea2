// src/pages/Login/LoginPage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Visibility,
  VisibilityOff,
  Close as CloseIcon,
} from '@mui/icons-material';
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
import { useState } from 'react';
import { Helmet } from 'react-helmet-async';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { z } from 'zod';

import useLogin from '../../hooks/useLogin';

export default function LoginPage() {
  const { t } = useTranslation('loginPage');
  const navigate = useNavigate();
  const { loginUser } = useLogin();

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loginError, setLoginError] = useState<string | null>(null);

  const loginSchema = z.object({
    email: z.string().email(t('validation.emailInvalid')),
    password: z.string().min(4, t('validation.passwordMinLength')),
  });

  type LoginForm = z.infer<typeof loginSchema>;

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

  // Handle form submission
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

  // Toggle password visibility
  const handleTogglePassword = () => setShowPassword(!showPassword);

  // Clear specific form field
  const handleClearField = (fieldName: keyof LoginForm) => {
    reset({ ...watch(), [fieldName]: '' });
  };

  // Determine the page title based on the state
  let pageTitle = t('titlePage');
  if (loading) {
    pageTitle = t('loading');
  } else if (loginError) {
    pageTitle = t('errorFetching', {
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
              {t('login')}
            </Typography>

            <Box component="form" onSubmit={handleSubmit(onSubmit)}>
              {/* EMAIL FIELD */}
              <TextField
                label={t('email')}
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
                      <IconButton
                        onClick={() => handleClearField('email')}
                        tabIndex={-1}
                      >
                        <CloseIcon />
                      </IconButton>
                    </InputAdornment>
                  ) : null,
                }}
              />

              {/* PASSWORD FIELD */}
              <TextField
                label={t('password')}
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
                      <IconButton onClick={handleTogglePassword} tabIndex={-1}>
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                      {watch('password') && (
                        <IconButton
                          onClick={() => handleClearField('password')}
                          tabIndex={-1}
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
                {loading ? <CircularProgress size={24} /> : t('login')}
              </Button>
            </Box>

            <Typography variant="body2" align="center" sx={{ mt: 2 }}>
              {t('noAccount')}{' '}
              <RouterLink to="/register" style={{ color: '#1976d2' }}>
                {t('signup')}
              </RouterLink>
            </Typography>
            <Typography variant="body2" align="center" sx={{ mt: 1 }}>
              <RouterLink to="/forgotpassword" style={{ color: '#1976d2' }}>
                {t('forgotPassword')}
              </RouterLink>
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
