// src/pages/Register/RegisterPage.tsx

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
  MenuItem,
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
import {
  useGetInstitutionsQuery,
  useGetCareersQuery,
} from '../../store/slices/institutionsApiSlice';
import useRegister from '../../hooks/useRegister';
import { toast } from 'react-toastify';
import { Helmet } from 'react-helmet-async';

const registerSchema = z
  .object({
    email: z.string().email('registerPage.invalidEmail'),
    password: z.string().min(4, 'registerPage.passwordMinLength'),
    confirmPassword: z.string().min(4, 'registerPage.passwordMinLength'),
    institutionId: z.string().nonempty('registerPage.selectInstitution'),
    careerId: z.string().nonempty('registerPage.selectCareer'),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ['confirmPassword'],
    message: 'registerPage.passwordsDoNotMatch',
  });

type RegisterForm = z.infer<typeof registerSchema>;

export default function RegisterPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const { registerUser } = useRegister();

  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  // Local state for loading and errors
  const [loading, setLoading] = useState(false);
  const [registerError, setRegisterError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    watch,
    reset,
    formState: { errors },
  } = useForm<RegisterForm>({
    resolver: zodResolver(registerSchema),
    mode: 'onChange',
  });

  // Watch for institution to enable career selection
  const selectedInstitution = watch('institutionId');

  // Query institutions and careers
  const { data: institutions } = useGetInstitutionsQuery(undefined);
  const { data: careers } = useGetCareersQuery(
    { institutionId: selectedInstitution },
    { skip: !selectedInstitution },
  );

  /**
   * Submits the registration
   */
  const onSubmit = async (data: RegisterForm) => {
    try {
      setRegisterError(null);
      setLoading(true);

      // Pass ALL fields: email, password, institutionId, careerId
      await registerUser({
        email: data.email,
        password: data.password,
        institutionId: data.institutionId,
        careerId: data.careerId,
      });

      // Navigate away after success
      toast.success(t('registerPage.toast.success'));
      navigate('/');
    } catch (err: any) {
      toast.error(t('registerPage.toast.error'));
      setRegisterError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleTogglePassword = () => {
    setShowPassword((prev) => !prev);
  };

  const handleToggleConfirmPassword = () => {
    setShowConfirmPassword((prev) => !prev);
  };

  const handleClearField = (fieldName: keyof RegisterForm) => {
    reset({ ...watch(), [fieldName]: '' });
  };

  let pageTitle = t('registerPage.titlePage');
  if (loading) {
    pageTitle = t('registerPage.loading');
  } else if (registerError) {
    pageTitle = t('registerPage.errorFetching', {
      error: String(registerError),
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
        <Card sx={{ maxWidth: 500, width: '100%', mx: 2 }}>
          <CardContent>
            <Typography variant="h4" gutterBottom align="center">
              {t('registerPage.signup')}
            </Typography>

            <Box component="form" onSubmit={handleSubmit(onSubmit)}>
              {/* EMAIL */}
              <TextField
                label={t('registerPage.email')}
                variant="outlined"
                fullWidth
                margin="normal"
                {...register('email')}
                error={!!errors.email}
                // Reserve space for error
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

              {/* PASSWORD */}
              <TextField
                label={t('registerPage.password')}
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

              {/* CONFIRM PASSWORD */}
              <TextField
                label={t('registerPage.confirmPassword')}
                variant="outlined"
                fullWidth
                margin="normal"
                type={showConfirmPassword ? 'text' : 'password'}
                {...register('confirmPassword')}
                error={!!errors.confirmPassword}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.confirmPassword
                      ? t(errors.confirmPassword.message as string)
                      : ''}
                  </Box>
                }
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton onClick={handleToggleConfirmPassword}>
                        {showConfirmPassword ? (
                          <VisibilityOff />
                        ) : (
                          <Visibility />
                        )}
                      </IconButton>
                      {watch('confirmPassword') && (
                        <IconButton
                          onClick={() => handleClearField('confirmPassword')}
                        >
                          <CloseIcon />
                        </IconButton>
                      )}
                    </InputAdornment>
                  ),
                }}
              />

              {/* INSTITUTION */}
              <TextField
                select
                label={t('registerPage.institution')}
                variant="outlined"
                fullWidth
                margin="normal"
                {...register('institutionId')}
                error={!!errors.institutionId}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.institutionId
                      ? t(errors.institutionId.message as string)
                      : ''}
                  </Box>
                }
              >
                <MenuItem value="">
                  {t('registerPage.selectInstitution')}
                </MenuItem>
                {institutions?.map((inst: any) => (
                  <MenuItem key={inst.id} value={inst.id}>
                    {inst.name}
                  </MenuItem>
                ))}
              </TextField>

              {/* CAREER */}
              <TextField
                select
                label={t('registerPage.career')}
                variant="outlined"
                fullWidth
                margin="normal"
                {...register('careerId')}
                error={!!errors.careerId}
                helperText={
                  <Box minHeight="1.5em">
                    {errors.careerId
                      ? t(errors.careerId.message as string)
                      : ''}
                  </Box>
                }
                disabled={!selectedInstitution}
              >
                <MenuItem value="">{t('registerPage.selectCareer')}</MenuItem>
                {careers?.map((c: any) => (
                  <MenuItem key={c.id} value={c.id}>
                    {c.name}
                  </MenuItem>
                ))}
              </TextField>

              {/* Overall Registration Error */}
              {registerError && (
                <Typography variant="body2" color="error" sx={{ mt: 1 }}>
                  {registerError}
                </Typography>
              )}

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
                  t('registerPage.signup')
                )}
              </Button>
            </Box>

            <Typography variant="body2" align="center" sx={{ mt: 2 }}>
              {t('registerPage.alreadyHaveAccount')}{' '}
              <RouterLink to="/login" style={{ color: '#1976d2' }}>
                {t('loginPage.login')}
              </RouterLink>
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </>
  );
}
