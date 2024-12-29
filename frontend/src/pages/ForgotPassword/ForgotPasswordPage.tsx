import { useState } from 'react';
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
  Close as CloseIcon,
  Visibility,
  VisibilityOff,
} from '@mui/icons-material';
import { useTranslation } from 'react-i18next';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import useForgotPassword from '../../hooks/useForgotPassword';

const emailSchema = z
  .string()
  .email('forgotPasswordPage.validationErrors.invalidEmail');
const codeSchema = z
  .string()
  .min(1, 'forgotPasswordPage.validationErrors.codeRequired');
const passwordSchema = z
  .string()
  .min(4, 'forgotPasswordPage.validationErrors.passwordLength');

export default function ForgotPasswordPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { handleEmailSubmit, handleCodeSubmit } = useForgotPassword();

  const [step, setStep] = useState<'email' | 'code'>('email');
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false); // State to toggle password visibility

  const forgotPasswordSchema = z.object({
    email: emailSchema,
    code: step === 'code' ? codeSchema : z.string().optional(),
    newPassword: step === 'code' ? passwordSchema : z.string().optional(),
  });

  const {
    control,
    handleSubmit,
    formState: { errors },
    setValue,
    reset,
    getValues,
  } = useForm({
    resolver: zodResolver(forgotPasswordSchema),
  });

  const handleEmail = async (data: any) => {
    setLoading(true);
    try {
      await handleEmailSubmit(data.email);
      toast.success(t('forgotPasswordPage.emailSent'));
      setStep('code');
      reset({ email: data.email });
    } catch (error: any) {
      toast.error(t('forgotPasswordPage.emailError'));
    } finally {
      setLoading(false);
    }
  };

  const handleCode = async (data: any) => {
    setLoading(true);
    try {
      await handleCodeSubmit({
        email: getValues('email'),
        code: data.code,
        newPassword: data.newPassword,
      });
      toast.success(t('forgotPasswordPage.passwordUpdated'));
      navigate('/login');
    } catch (error: any) {
      toast.error(error?.message || t('forgotPasswordPage.codeError'));
    } finally {
      setLoading(false);
    }
  };

  return (
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
            {t('forgotPasswordPage.title')}
          </Typography>

          {step === 'email' && (
            <Box component="form" onSubmit={handleSubmit(handleEmail)}>
              <Controller
                name="email"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label={t('forgotPasswordPage.emailLabel')}
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    error={!!errors.email}
                    helperText={
                      <Box minHeight="1.5em">
                        {errors.email ? t(errors.email.message as string) : ''}
                      </Box>
                    }
                    InputProps={{
                      endAdornment: field.value ? (
                        <InputAdornment position="end">
                          <IconButton onClick={() => setValue('email', '')}>
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
                {loading ? (
                  <CircularProgress size={24} />
                ) : (
                  t('forgotPasswordPage.submitEmail')
                )}
              </Button>
            </Box>
          )}

          {step === 'code' && (
            <Box component="form" onSubmit={handleSubmit(handleCode)}>
              <Controller
                name="code"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label={t('forgotPasswordPage.codeLabel')}
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
                          <IconButton onClick={() => setValue('code', '')}>
                            <CloseIcon />
                          </IconButton>
                        </InputAdornment>
                      ) : null,
                    }}
                  />
                )}
              />

              <Controller
                name="newPassword"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label={t('forgotPasswordPage.newPasswordLabel')}
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
                          {field.value && (
                            <InputAdornment position="end">
                              <IconButton
                                onClick={() => setValue('newPassword', '')}
                              >
                                <CloseIcon />
                              </IconButton>
                            </InputAdornment>
                          )}
                          <InputAdornment position="end">
                            <IconButton
                              onClick={() => setShowPassword((prev) => !prev)}
                            >
                              {showPassword ? (
                                <VisibilityOff />
                              ) : (
                                <Visibility />
                              )}
                            </IconButton>
                          </InputAdornment>
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
                {loading ? (
                  <CircularProgress size={24} />
                ) : (
                  t('forgotPasswordPage.submitCode')
                )}
              </Button>
            </Box>
          )}
        </CardContent>
      </Card>
    </Box>
  );
}
