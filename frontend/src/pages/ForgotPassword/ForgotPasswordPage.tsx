// src/pages/ForgotPassword/ForgotPasswordPage.tsx

import { zodResolver } from '@hookform/resolvers/zod';
import {
  Close as CloseIcon,
  Visibility,
  VisibilityOff,
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
import { useForm, Controller } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { z } from 'zod';

import useForgotPassword from '../../hooks/useForgotPassword';

export default function ForgotPasswordPage() {
  const { t } = useTranslation('forgotPasswordPage');
  const navigate = useNavigate();
  const { handleEmailSubmit, handleCodeSubmit } = useForgotPassword();

  const [step, setStep] = useState<'email' | 'code'>('email');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const forgotPasswordSchema = z.object({
    email: z.string().email('validation.emailInvalid'),
    code: z.string().length(6, 'validation.codeLength').optional(),
    newPassword: z.string().min(4, 'validation.passwordMinLength').optional(),
  });

  type ForgotPasswordForm = z.infer<typeof forgotPasswordSchema>;

  const {
    control,
    handleSubmit,
    formState: { errors },
    setValue,
    reset,
    getValues,
  } = useForm<ForgotPasswordForm>({
    resolver: zodResolver(forgotPasswordSchema),
  });

  // Handle email submission
  const onSubmitEmail = async (data: { email: string }) => {
    setLoading(true);
    try {
      await handleEmailSubmit(data.email);
      toast.success(t('emailSent'));
      setStep('code');
      reset({ email: data.email });
    } catch (error) {
      console.error('Failed to submit email:', error);
      toast.error(t('emailError'));
    } finally {
      setLoading(false);
    }
  };

  // Handle code and password submission
  const onSubmitCode = async (data: {
    code?: string;
    newPassword?: string;
  }) => {
    setLoading(true);
    try {
      await handleCodeSubmit({
        email: getValues('email'),
        code: data.code || '',
        newPassword: data.newPassword || '',
      });
      toast.success(t('passwordUpdated'));
      navigate('/login');
    } catch (error: any) {
      toast.error(error?.message || t('codeError'));
    } finally {
      setLoading(false);
    }
  };

  // Toggle password visibility
  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);

  // Clear specific form field
  const handleClearField = (fieldName: keyof ForgotPasswordForm) => {
    setValue(fieldName, '');
  };

  // Determine the page title based on the state
  let pageTitle = t('titlePage');
  if (loading) {
    pageTitle = t('loading');
  } else if (step === 'code' && (errors.code || errors.newPassword)) {
    pageTitle = t('errorFetching');
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
              {t('title')}
            </Typography>

            {step === 'email' && (
              <Box component="form" onSubmit={handleSubmit(onSubmitEmail)}>
                {/* Email Field */}
                <Controller
                  name="email"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label={t('emailLabel')}
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      error={!!errors.email}
                      helperText={
                        <Box minHeight="1.5em">
                          {errors.email
                            ? t(errors.email.message as string)
                            : ''}
                        </Box>
                      }
                      InputProps={{
                        endAdornment: field.value ? (
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
                  )}
                />

                <Button
                  variant="contained"
                  fullWidth
                  sx={{ mt: 2 }}
                  type="submit"
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={24} /> : t('submitEmail')}
                </Button>
              </Box>
            )}

            {step === 'code' && (
              <Box component="form" onSubmit={handleSubmit(onSubmitCode)}>
                {/* Code Field */}
                <Controller
                  name="code"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label={t('codeLabel')}
                      variant="outlined"
                      fullWidth
                      margin="normal"
                      error={!!errors.code}
                      helperText={
                        <Box minHeight="1.5em">
                          {errors.code ? t(errors.code.message as string) : ''}
                        </Box>
                      }
                      InputProps={{
                        endAdornment: field.value ? (
                          <InputAdornment position="end">
                            <IconButton
                              onClick={() => handleClearField('code')}
                              tabIndex={-1}
                            >
                              <CloseIcon />
                            </IconButton>
                          </InputAdornment>
                        ) : null,
                      }}
                    />
                  )}
                />

                {/* New Password Field */}
                <Controller
                  name="newPassword"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label={t('newPasswordLabel')}
                      variant="outlined"
                      type={showPassword ? 'text' : 'password'}
                      fullWidth
                      margin="normal"
                      error={!!errors.newPassword}
                      helperText={
                        <Box minHeight="1.5em">
                          {errors.newPassword
                            ? t(errors.newPassword.message as string)
                            : ''}
                        </Box>
                      }
                      InputProps={{
                        endAdornment: (
                          <>
                            <InputAdornment position="end">
                              <IconButton
                                onClick={togglePasswordVisibility}
                                tabIndex={-1}
                              >
                                {showPassword ? (
                                  <VisibilityOff />
                                ) : (
                                  <Visibility />
                                )}
                              </IconButton>
                            </InputAdornment>
                            {field.value && (
                              <InputAdornment position="end">
                                <IconButton
                                  onClick={() =>
                                    handleClearField('newPassword')
                                  }
                                  tabIndex={-1}
                                >
                                  <CloseIcon />
                                </IconButton>
                              </InputAdornment>
                            )}
                          </>
                        ),
                      }}
                    />
                  )}
                />

                <Button
                  variant="contained"
                  fullWidth
                  sx={{ mt: 2 }}
                  type="submit"
                  disabled={loading}
                >
                  {loading ? <CircularProgress size={24} /> : t('submitCode')}
                </Button>
              </Box>
            )}
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
