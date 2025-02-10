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

// Separate Zod schemas for validation
const emailSchema = z.object({
  email: z.string().email('validation.emailInvalid'),
});

const codeAndPasswordSchema = z.object({
  code: z
    .string()
    .nonempty('validation.codeRequired')
    .regex(/^\d{6}$/, 'validation.codeInvalid'),
  newPassword: z
    .string()
    .nonempty('validation.passwordRequired')
    .regex(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$/,
      'validation.passwordInvalid',
    )
    .min(4, 'validation.passwordMinLength')
    .max(50, 'validation.passwordMaxLength'),
});

function EmailForm({
  onSubmit,
  loading,
}: {
  onSubmit: (data: { email: string }) => void;
  loading: boolean;
}) {
  const { t } = useTranslation('forgotPasswordPage');
  const {
    control,
    handleSubmit,
    formState: { errors },
    setValue,
  } = useForm<{ email: string }>({
    resolver: zodResolver(emailSchema),
  });

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)}>
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
            helperText={errors.email ? t(errors.email.message as string) : ''}
            InputProps={{
              endAdornment: field.value ? (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setValue('email', '')}
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
  );
}

function CodeAndPasswordForm({
  onSubmit,
  loading,
}: {
  onSubmit: (data: { code: string; newPassword: string }) => void;
  loading: boolean;
}) {
  const { t } = useTranslation('forgotPasswordPage');
  const {
    control,
    handleSubmit,
    formState: { errors },
    setValue,
  } = useForm<{ code: string; newPassword: string }>({
    resolver: zodResolver(codeAndPasswordSchema),
  });
  const [showPassword, setShowPassword] = useState(false);

  const togglePasswordVisibility = () => setShowPassword((prev) => !prev);

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)}>
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
            helperText={errors.code ? t(errors.code.message as string) : ''}
            InputProps={{
              endAdornment: field.value ? (
                <InputAdornment position="end">
                  <IconButton
                    onClick={() => setValue('code', '')}
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
              errors.newPassword ? t(errors.newPassword.message as string) : ''
            }
            InputProps={{
              endAdornment: (
                <>
                  <InputAdornment position="end">
                    <IconButton
                      onClick={togglePasswordVisibility}
                      tabIndex={-1}
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                  {field.value && (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setValue('newPassword', '')}
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
  );
}

export default function ForgotPasswordPage() {
  const { t } = useTranslation('forgotPasswordPage');
  const navigate = useNavigate();
  const { handleEmailSubmit, handleCodeSubmit } = useForgotPassword();
  const [step, setStep] = useState<'email' | 'code'>('email');
  const [loading, setLoading] = useState(false);
  const [email, setEmail] = useState('');

  const onSubmitEmail = async (data: { email: string }) => {
    setLoading(true);
    try {
      const successEmail = await handleEmailSubmit(data.email);
      if (successEmail) {
        toast.success(t('emailSent'));
        setEmail(data.email);
        setStep('code');
      } else {
        toast.error(t('emailError'));
      }
    } catch {
      toast.error(t('emailError'));
    } finally {
      setLoading(false);
    }
  };

  const onSubmitCode = async (data: { code: string; newPassword: string }) => {
    setLoading(true);
    try {
      await handleCodeSubmit({ email, ...data });
      toast.success(t('passwordUpdated'));
      navigate('/login');
    } catch (error: any) {
      toast.error(
        t('codeError', {
          errorMessage: error.message,
        }),
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Helmet>
        <title>{t('titlePage')}</title>
      </Helmet>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '80vh',
        }}
      >
        <Card sx={{ p: 7 }}>
          <Typography variant="h4" gutterBottom align="center">
            {t('title')}
          </Typography>

          {step === 'email' && (
            <>
              <Typography variant="body1" align="center" sx={{ mb: 2 }}>
                {t('subtitleEmail')}
              </Typography>
              <EmailForm onSubmit={onSubmitEmail} loading={loading} />
            </>
          )}
          {step === 'code' && (
            <>
              <Typography variant="body1" align="center" sx={{ mb: 2 }}>
                {t('subtitleCode')}
              </Typography>
              <CodeAndPasswordForm onSubmit={onSubmitCode} loading={loading} />
            </>
          )}
        </Card>
      </Box>
    </>
  );
}
